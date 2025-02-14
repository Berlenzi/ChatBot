// src/components/MessageBody.jsx

import React, { useEffect, useRef } from 'react';
import Message from './message';

function MessageBody({ messages ,isSidebarOpen }) {
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  return (
    <div
    className={`mt-7 flex items-center justify-center flex-1 transition-all duration-300 ${
      isSidebarOpen ? 'hidden md:flex md:ml-20' : 'flex md:ml-55'
    }`}
  >
      <div className=" w-full max-w-2xl rounded-lg p-4  ">
        {messages.map((msg) => (
          <Message key={msg.id} text={msg.text} sender={msg.sender} />
        ))}
        <div ref={messagesEndRef} className='mb-20' />
      </div>
    </div>
  );
}

export default MessageBody;
