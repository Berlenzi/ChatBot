// src/contexts/AuthContext.js
import React, { createContext, useState, useEffect } from 'react';
import { encryptData, decryptData } from '../Utils/cryptUtils';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Carica lo stato di autenticazione dal localStorage al montare del componente
  useEffect(() => {
    const encryptedToken = localStorage.getItem('token');
    if (encryptedToken) {
      const decryptedToken = decryptData(encryptedToken);
      if (decryptedToken) {
        setIsAuthenticated(true);
      }
    }
  }, []);

  // Funzione per effettuare il login
  const login = (token) => {
    const encryptedToken = encryptData(token);
    localStorage.setItem('token', encryptedToken);
    setIsAuthenticated(true);
  };

  // Funzione per effettuare il logout
  const logout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
