import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import { encryptData, decryptData } from '../Utils/cryptUtils';
function PrivateRoute({ children }) {
  const { isAuthenticated } = useContext(AuthContext);
  const tokenTemp = localStorage.getItem('token');
  const token = decryptData(tokenTemp);


  return token ? children : <Navigate to="/" />;
}

export default PrivateRoute;
