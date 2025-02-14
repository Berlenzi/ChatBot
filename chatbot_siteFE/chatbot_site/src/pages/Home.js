import React, { useState, useEffect } from 'react';
import Header from '../Components/header';
import Sidebar from '../Components/sidebar';
import Footer from '../Components/footer';
import MessageBody from '../Components/messageBody';
import { encryptData, decryptData } from '../Utils/cryptUtils';
import { v4 as uuidv4 } from 'uuid'; 

function HomePage() {
  const [isOpen, setIsOpen] = useState(true);
  const [messages, setMessages] = useState([]);
  const [idConversation, setIdConversation] = useState("");
  const tokenTemp = localStorage.getItem('token');
  const token = decryptData(tokenTemp);

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

  const startNewConversation = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/chat', {
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
      console.log('Data ricevuta da /api/chat:', data); // Log per debug

      const botMessages = data.chatMessages
        .filter(cm => cm.message && cm.message.trim() !== '') // Filtra i messaggi vuoti
        .map((cm) => ({
          id: uuidv4(), // Usa uuid per ID unici
          text: cm.message,
          sender: 'BOT',
          idConversation: cm.idConversation,
        }));

      console.log('Bot Messages:', botMessages); // Log per debug

      if (botMessages.length > 0) {
        const newIdConversation = botMessages[0].idConversation;
        setIdConversation(newIdConversation);
        setMessages((prev) => [...prev, ...botMessages]);
        return newIdConversation;
      }
    } catch (error) {
      console.error('Errore durante la creazione di una nuova conversazione:', error);
      const errorMsg = {
        id: uuidv4(),
        text: 'Mi dispiace, si è verificato un errore nella creazione della nuova conversazione.',
        sender: 'BOT',
      };
      setMessages((prev) => [...prev, errorMsg]);
      return null;
    }
  };

  const handleSend = async (text) => {
    if (text.trim() === '') return;

    const newMessage = {
      id: uuidv4(),
      text,
      sender: 'USER',
    };
    setMessages((prev) => [...prev, newMessage]);

    let currentIdConversation = idConversation;

    if (!currentIdConversation) {
      currentIdConversation = await startNewConversation();
      if (!currentIdConversation) {
        // Gestisci l'errore se la conversazione non è stata creata
        return;
      }
    }

    try {
      const res = await fetch('http://localhost:8081/api/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: text,
          idConversation: currentIdConversation,
          sender: 'USER',
          token,
        }),
      });

      if (!res.ok) {
        throw new Error('Errore durante l’invio del messaggio');
      }

      const data = await res.json();
      const botMessages = data.chatMessages
        .filter(cm => cm.message && cm.message.trim() !== '') // Filtra i messaggi vuoti
        .map((cm) => ({
          id: uuidv4(),
          text: cm.message,
          sender: 'BOT',
          idConversation: cm.idConversation,
        }));

      setMessages((prev) => [...prev, ...botMessages]);

      if (!idConversation && botMessages.length > 0) {
        setIdConversation(botMessages[0].idConversation);
      }
    } catch (error) {
      console.error('Errore durante l’invio del messaggio:', error);
      const errorMsg = {
        id: uuidv4(),
        text: 'Mi dispiace, si è verificato un errore durante l’invio del messaggio.',
        sender: 'BOT',
      };
      setMessages((prev) => [...prev, errorMsg]);
    }
  };

  const toggleSidebar = () => {
    setIsOpen((prev) => !prev);
  };

  return (
    <div className="min-h-screen bg-background flex">
      <Sidebar
        isOperator={false}
        isOpen={isOpen}
        toggleSidebar={toggleSidebar}
        setMessages={setMessages}
        token={token}
        onConversationChange={(id) => setIdConversation(id)}
      />
      <div className="flex-1 flex flex-col">
        <Header isOperator={false} isSidebarOpen={isOpen} />
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

export default HomePage;