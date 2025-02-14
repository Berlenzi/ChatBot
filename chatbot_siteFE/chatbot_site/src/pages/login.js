// src/pages/LoginPage.js
import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [serverError, setServerError] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const credentials = { username, password };

    try {
      // Invia la richiesta al backend
      const response = await fetch('http://localhost:8081/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      // Verifica la risposta
      if (response.ok) {
        const data = await response.json();

        if (data.success) {
          // Autenticazione riuscita, salva il token crittografato
          login(data.token);
          navigate('/chat');
        } else {
          // Autenticazione fallita
          alert(data.message);
        }
      } else {
        setServerError("Password sbagliata");
      }
    } catch (error) {
      // Errore di rete o altro
      console.error('Errore durante il login:', error);
      alert('Errore durante il login. Riprova più tardi.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-full md:max-w-md p-4 md:p-8 rounded md:shadow-md  md:bg-white ">
        <h2 className="text-2xl font-bold mb-6 text-center md:text-black text-white ">Welcome Back</h2>
        <form onSubmit={handleSubmit} className="md:space-y-4 space-y-8">
          <input
            type="text"
            placeholder="Nome utente"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
          />
          
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
          />
           {serverError && <p className="text-red-500 font-poppins">{serverError}</p>}
          <button
            type="submit"
            className="w-full bg-accent text-white p-3 rounded font-semibold hover:bg-primary-700 transition duration-200 font-poppins"
          >
          Accedi
          </button>
          
        </form>
        <div className='flex flex-row items-center justify-center gap-4 mt-4'>
          <p className="text-sm   md:text-secondary text-white  underline font-poppins">
            Ricorda Password
          </p>
          <button 
            onClick={() => navigate("/register")} 
            className="text-sm md:text-secondary text-white underline font-poppins"
          >
            Non hai un account? Registrati
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
