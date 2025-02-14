// OperatorChat.jsx
import React, { useState, useEffect } from 'react';
import Header from '../Components/header';
import Sidebar from '../Components/sidebar';
import Footer from '../Components/footer';
import MessageBody from '../Components/messageBody';
import { encryptData, decryptData } from '../Utils/cryptUtils';

function OperatorChat() {
  const [isOpen, setIsOpen] = useState(true);
  const [messages, setMessages] = useState([]);
  const [idConversation, setIdConversation] = useState('');
  const [socket, setSocket] = useState(null);
  const [connected, setConnected] = useState(false);
  const tokenTemp = localStorage.getItem('token')
  const token = decryptData(tokenTemp);
  

  // Gestione della sidebar reattiva
  useEffect(() => {
    if (!token) return;

    startNewConversation();

    const handleResize = () => {
      setIsOpen(window.innerWidth >= 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);

    return () => window.removeEventListener('resize', handleResize);
  }, [token]);

  useEffect(() => {
    if (idConversation && !connected) {
      connect();
    }
    return () => {
      disconnect();
    };
  }, [idConversation, connected]); // Aggiungiamo connected qui
  
  
  const connect = () => {
    if (!idConversation || socket?.readyState === WebSocket.OPEN) return;
  
    const wsUrl = new URL('wss://chatbot-core-staging.citelgroup.it/chatbot/api/websocket/connect');
    wsUrl.searchParams.append('id', idConversation);
    
    const ws = new WebSocket(wsUrl);
  
    ws.onopen = () => {
      console.log('WebSocket connesso');
      setConnected(true);
    };
  
    ws.onmessage = (event) => {
      const message = JSON.parse(event.data);
      setMessages(prev => [...prev, {
        id: Date.now(),
        text: message.content,
        sender: 'BOT',
        idConversation
      }]);
    };
  
    ws.onerror = (error) => {
      console.error('Errore WebSocket:', error);
      setConnected(false);
    };
  
    ws.onclose = (event) => {
      console.log('WebSocket disconnesso', event.code, event.reason);
      setConnected(false);
      // Tentativo di riconnessione se la chiusura non è volontaria
      if (event.code !== 1000) {
        setTimeout(() => {
          if (!connected) connect();
        }, 3000);
      }
    };
  
    setSocket(ws);
  };
  

  const sendMessage = (text) => {
    if (!socket || socket.readyState !== WebSocket.OPEN) {
      console.log('Socket non connesso, riconnessione...');
      connect();
      // Memorizza il messaggio da inviare dopo la riconnessione
      setTimeout(() => {
        if (socket?.readyState === WebSocket.OPEN) {
          socket.send(JSON.stringify({ content: text }));
        }
      }, 1000);
      return;
    }
    
    try {
      socket.send(JSON.stringify({ content: text }));
    } catch (error) {
      console.error('Errore nell\'invio del messaggio:', error);
      setConnected(false);
      connect();
    }
  };
  

  const disconnect = () => {
    if (socket) {
      try {
        socket.close(1000, "Disconnessione volontaria");
      } catch (error) {
        console.error('Errore durante la disconnessione:', error);
      }
      setSocket(null);
      setConnected(false);
    }
  };

  const startNewConversation = async () => {
    try {
      const res = await fetch(`http://localhost:8081/api/chat`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token }),
      });

      if (!res.ok) {
        throw new Error('Errore durante la creazione di una nuova conversazione');
      }

      const data = await res.json();
      const botMessages = data.chatMessages.map((cm) => ({
        id: cm.id,
        text: cm.message,
        sender: 'BOT',
        idConversation: cm.idConversation,
      }));

      if (botMessages.length > 0) {
        const newIdConversation = botMessages[0].idConversation;
        setIdConversation(newIdConversation);
        setMessages((prev) => [...prev, ...botMessages]);
      }
    } catch (error) {
      console.error('Errore durante la creazione di una nuova conversazione:', error);
      const errorMsg = {
        id: Date.now(),
        text: 'Mi dispiace, si è verificato un errore nella creazione della nuova conversazione.',
        sender: 'BOT',
      };
      setMessages((prev) => [...prev, errorMsg]);
    }
  };

  const handleSend = (text) => {
    if (text.trim() === '') return;

    const newMsg = {
      id: Date.now(),
      text,
      sender: 'USER',
      idConversation,
    };
    setMessages((prev) => [...prev, newMsg]);

    sendMessage(text);
  };

  const toggleSidebar = () => {
    setIsOpen((prev) => !prev);
  };

  return (
    <div className="min-h-screen bg-background flex">
      <Sidebar
        isOperator={true}
        isOpen={isOpen}
        toggleSidebar={toggleSidebar}
        setMessages={setMessages}
        token={token}
        onConversationChange={(id) => setIdConversation(id)}
      />
      <div className="flex-1 flex flex-col">
        <Header isOperator={true} isSidebarOpen={isOpen} />
        <main className="flex flex-1">
          <MessageBody messages={messages} isSidebarOpen={isOpen} />
        </main>
        <Footer
          isSidebarOpen={isOpen}
          onSend={handleSend}
          idConversation={idConversation}
        />
      </div>
    </div>
  );
}

export default OperatorChat;