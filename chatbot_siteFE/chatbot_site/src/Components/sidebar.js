import React, { useState, useEffect } from 'react';
import Logout from './logout';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as LogSvg } from '../assets/logout.svg';
import { ReactComponent as UserSvg } from '../assets/User.svg';
import {ReactComponent as ChatSvg } from '../assets/chat.svg';
import './CustomCSS/sidebar.css';

function Sidebar({isOperator , isOpen, toggleSidebar, setMessages, token, onConversationChange }) {
  const navigate = useNavigate();

  const [activeConversation, setActiveConversation] = useState(null);
  const [conversations, setConversations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch delle conversazioni disponibili
    fetch('http://localhost:8081/api/history', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ token, id: null }),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Errore nel recupero delle conversazioni');
        }
        return res.json();
      })
      .then((data) => {
        setConversations(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Errore nel recupero delle conversazioni:', error);
        setError('Errore nel recupero delle conversazioni.');
        setLoading(false);
      });
  }, [token]);

  const handleConversationClick = (conversation) => {
   setActiveConversation(conversation?.idConversation || null);
  setMessages(conversation?.messages || []);
  onConversationChange(conversation?.idConversation || null);
  };

  return (
    <>
      {/* Bottone per aprire la sidebar */}
      {!isOpen && (
        <button
          onClick={toggleSidebar}
          className="fixed top-4 left-4 text-white z-50 md:hidden focus:outline-none"
          aria-label="Open Sidebar"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      )}

      {/* Overlay per chiudere la sidebar su dispositivi mobili */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"
          onClick={toggleSidebar}
          aria-label="Chiudi Sidebar"
        ></div>
      )}

      {/* Sidebar */}
      <div
      className={`fixed top-0 left-0 h-full overflow-y-scroll Sidebar z-50 text-white transition-transform duration-300 bg-accent ${
        isOpen ? 'w-64' : 'md:w-20 w-0'
      } md:translate-x-0 translate-x-0  flex flex-col justify-between`}
    >
      {/* Header della Sidebar */}
      <div className="flex items-center justify-between p-4">
        {isOpen && <h1 className="text-2xl font-bold font-poppins">ChatBot</h1>}
        <button
          onClick={toggleSidebar}
          className="text-white focus:outline-none"
          aria-label="Toggle Sidebar"
        >
          {isOpen ? (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          ) : (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          )}
        </button>
      </div>

      {/* Etichetta Cronologia */}
      {isOpen && (
        <div className="px-4 pb-2">
          <span className="text-sm text-gray-300">Cronologia</span>
        </div>
      )}

      {/* Elenco delle Conversazioni */}
      <div className="flex-1 px-4 overflow-y-scroll Sidebar">
        {loading && (
          <div className="p-2 text-center text-sm text-gray-300">Caricamento conversazioni...</div>
        )}
        {error && (
          <div className="p-2 text-center text-red-500 text-sm">{error}</div>
        )}
        {!loading &&
          !error &&
          conversations.map((convo, index) => (
            <button
              key={convo.idConversation}
              onClick={() => handleConversationClick(convo)}
              className={`w-full mb-2 text-left px-3 py-2 hover:bg-hover rounded ${
                activeConversation === convo.idConversation ? 'bg-hover' : ''
              }`}
            >
              {isOpen ? (
                <span className="text-sm">Conversazione #{index + 1}</span>
              ) : (
                <span className="text-sm">C#{index + 1}</span>
              )}
            </button>
          ))}
      </div>

      {/* Footer della Sidebar */}
      <div className="px-4 pb-2">
        {isOpen && <span className="text-sm text-gray-300">Impostazioni</span>}
      </div>
      <div className="px-4 pb-4">
        {isOpen ? (
          <div className="mb-2">
            <button
              onClick={() => navigate('/users')}
              className="bg-accent hover:bg-hover w-full p-4 rounded flex items-center justify-center gap-x-3 font-poppins"
            >
              Gestisci Account
              <UserSvg className="w-5 h-5" />
            </button>
          </div>
        ) : (
          <div className="mb-2 flex justify-center hover:bg-hover rounded">
            <button
              onClick={() => navigate('/users')}
              className="hover:bg-hover rounded p-2"
            >
              <UserSvg className="w-5 h-5" />
            </button>
          </div>
        )}
        

        <div className="mt-2">
          <Logout isOpen={isOpen} />
        </div>
      </div>
    </div>
    </>
  );
}

export default Sidebar;
