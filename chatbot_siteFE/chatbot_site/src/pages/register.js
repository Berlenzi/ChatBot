//Password : adminPassword1

//Utente : admin


import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import LoginPage from '../pages/login.js';
import { Link } from 'react-router-dom';
function RegisterPage() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({});
  const[serverError,setServerError] = useState('')
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();


  const validation = () => {
    const errors = {};
    console.log(formData.username, formData.password);
    if(formData.username == ""){
      errors.username = "Username obbligatorio";
    }
 
    


    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email) {
      errors.email = "L'email è obbligatoria.";
    } else if (!emailRegex.test(formData.email)) {
      errors.email = "Inserisci un'email valida.";
    }
    
    if (formData.password == "") {
      errors.password = "La password è obbligatoria.";
    } else if (formData.password.length < 8) {
      errors.password = "La password deve essere almeno 8 caratteri.";
    }

    if (!formData.confirmPassword) {
      errors.confirmPassword = "Conferma la password è obbligatoria.";
    } else if (formData.confirmPassword!== formData.password) {
      errors.confirmPassword = "Le password non coincidono.";
    }

    setErrors(errors);

    return Object.keys(errors).length === 0;

  }


  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
};


  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const isValid = validation();

    if(!isValid) {
        return;
    }

    const credentials = { 
      username: formData.username
      ,email : formData.email
      ,password : formData.password
    };


    try {
      // Invia la richiesta al backend
      const response = await fetch('http://localhost:8081/api/register', {
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
          // Autenticazione riuscita
          login(); 
          navigate('/'); 
        } else {
          // Autenticazione fallita
          alert(data.message);
        }
      } else {
        
        setServerError('Username o/e email già in uso ' + response.statusText);
      }
    } catch (error) {
      // Errore di rete o altro
      console.error('Errore durante la regisrazione:', error);
      alert('Errore durante la registrazione. Riprova più tardi.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="w-full md:max-w-md p-4 md:p-8 rounded md:shadow-md  md:bg-white ">
            <h2 className="text-2xl font-bold mb-6 text-center md:text-black text-white font-poppins ">Registrati</h2>
            <form onSubmit={handleSubmit} className="md:space-y-4 space-y-8">
            {errors.username && <p className="text-red-500 font-poppins">{errors.username}</p>}
                <input
                    type="text"
                    id='username'
                    name='username'
                    placeholder="Nome utente"
                    value={formData.username}
                    onChange={handleChange}
                    className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
                    
                />
                {errors.email && <p className="text-red-500 font-poppins">{errors.email}</p>}
                 <input
                    type="email"
                    id='email'
                    name='email'
                    placeholder="email"
                    value={formData.email}
                    onChange={handleChange}
                    className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
                    
                />
                {errors.password && <p className="text-red-500 font-poppins">{errors.password}</p>}
                <input
                    type="password"
                    id='password'
                    name='password'
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
                    
                />
                {errors.confirmPassword && <p className="text-red-500 font-poppins">{errors.confirmPassword}</p>}
                <input
                    type="password"
                    id = "confirmPassword"
                    name='confirmPassword'
                    placeholder="Confirm Password"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    className="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-primary font-poppins"
                    
                />
                <button
                    type="submit"
                    className="w-full bg-accent text-white p-3 rounded font-semibold hover:bg-primary-700 transition duration-200"
                >
                Registrati
                </button>
                {serverError && <p className="text-red-500 font-poppins">{serverError}</p>}


            </form>
            <div className=' flex flex-row items-center justify-center gap-12 mt-3 '>
                <button onClick={() => navigate("/")} className=" text-center text-sm text-secondary underline font-poppins "> hai un account? Clicca qui</button>
            </div>
        </div>
    </div>
     
      
    
  );
}

export default RegisterPage;
