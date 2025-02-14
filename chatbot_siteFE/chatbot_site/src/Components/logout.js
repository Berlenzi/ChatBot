import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import { ReactComponent as LogSvg }  from '../assets/logout.svg'
import { encryptData, decryptData } from '../Utils/cryptUtils';

export const Logout = ({isOpen}) => {
    const navigate = useNavigate();
    const { logout } = useContext(AuthContext);

    const handleLogout = async (e) => {
        e.preventDefault();
          const tokenTemp = localStorage.getItem('token');
          const token = decryptData(tokenTemp);

        console.log(token);
        try {
            const response = await fetch("http://localhost:8081/api/logout", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `Bearer ${token}`,
                },
                credentials: 'include',
            });
            if (response.ok) {
                
                navigate("/");
                localStorage.removeItem("token");
                logout();
                
                
            } else {
                alert("Logout fallito");
            }
        } catch (error) {
            alert("Errore durante il logout ", error);
        }
    };

    return (

        <button
            onClick={handleLogout}
            className={` p-4 rounded bg-accent hover:bg-hover w-full 
             font-poppins text-white  `}
            aria-label="Logout"
        >
            {isOpen ? ( <div className='flex items-center justify-center gap-x-2 '> Logout <LogSvg className="w-5 h-5 "  /></div>) : ( <div className='flex  justify-center'> <LogSvg className="w-6 h-6" /></div> )}
        </button>
        
    );
}

export default Logout;
