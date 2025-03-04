import React from 'react';
import './index.css';
import ReactDOM from 'react-dom';
import App from './App';
import { AuthProvider } from './contexts/AuthContext';


ReactDOM.render(
  <React.StrictMode>
    <div className='bg-background'>
      <AuthProvider>      
        <App />
      </AuthProvider>
    </div>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals

