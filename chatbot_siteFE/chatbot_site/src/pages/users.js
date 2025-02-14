import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Users() {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [editingUsernameIds, setEditingUsernameIds] = useState([]);
    const [editingPasswordIds, setEditingPasswordIds] = useState([]);
    const [passwords, setPasswords] = useState({});
    const [usernames, setUsernames] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const url = "http://localhost:8081/api/users";
        fetch(url, {
            credentials: 'include'
        })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText);
            })
            .then(data => {
                setUsers(data);
            })
            .catch(err => {
                setError(err.message);
            });
    }, []);

    const handleChangeUsername = (userId) => {
        setEditingUsernameIds(prev => {
            if (prev.includes(userId)) {
                const updated = prev.filter(id => id !== userId);
                const updatedUsernames = { ...usernames };
                delete updatedUsernames[userId];
                setUsernames(updatedUsernames);
                return updated;
            } else {
                setEditingPasswordIds(prevPwd => prevPwd.filter(id => id !== userId));
                return [...prev, userId];
            }
        });
    };

    const handleChangePwd = (userId) => {
        setEditingPasswordIds(prev => {
            if (prev.includes(userId)) {
                const updated = prev.filter(id => id !== userId);
                const updatedPasswords = { ...passwords };
                delete updatedPasswords[userId];
                setPasswords(updatedPasswords);
                return updated;
            } else {
                setEditingUsernameIds(prevUser => prevUser.filter(id => id !== userId));
                return [...prev, userId];
            }
        });
    };

    const handleUsernameChange = (userId, username) => {
        setUsernames(prev => ({
            ...prev,
            [userId]: username
        }));
    };

    const handlePasswordChange = (userId, newPassword) => {
        setPasswords(prev => ({
            ...prev,
            [userId]: newPassword
        }));
    };

    const handleSavePassword = async (userId) => {
        const newPassword = passwords[userId];
        if (!newPassword) {
            alert("Inserisci una nuova password.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8081/api/users/${userId}/change-password`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ password: newPassword }),
            });

            if (response.ok) {
                alert("Password cambiata con successo.");
                setEditingPasswordIds(prev => prev.filter(id => id !== userId));
                setPasswords(prev => {
                    const updated = { ...prev };
                    delete updated[userId];
                    return updated;
                });
            } else {
                const errorData = await response.json();
                alert(`Errore: ${errorData.message || response.statusText}`);
            }
        } catch (error) {
            alert(`Errore: ${error.message}`);
        }
    };

    const handleSaveUsername = async (userId) => {
        const newUsername = usernames[userId];
        if (!newUsername) {
            alert("Inserisci un nuovo username.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8081/api/users/${userId}/change-username`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username: newUsername }),
            });

            if (response.ok) {
                alert("Username cambiato con successo.");
                setEditingUsernameIds(prev => prev.filter(id => id !== userId));
                setUsernames(prev => {
                    const updated = { ...prev };
                    delete updated[userId];
                    return updated;
                });
            } else {
                const errorData = await response.json();
                alert(`Errore: ${errorData.message || response.statusText}`);
            }
        } catch (error) {
            alert(`Errore: ${error.message}`);
        }
    };

    if (error) return <div className="text-center text-red-500 font-poppins">Errore: {error}</div>;

    return (
        <div className="min-h-screen flex items-center justify-center bg-background p-4 sm:p-8">
            <div className="bg-primary sm:p-6 p-4 rounded shadow-xl w-full max-w-[100%] sm:max-w-[80vw]">
                <button
                    className="bg-[#ff5f56] w-6 h-6 rounded-full flex items-center justify-center hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400 transition-colors duration-200"
                    onClick={() => navigate('/chat')}
                    aria-label="Torna alla Chat"
                >
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-white" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                    </svg>
                </button>

                <div className="hidden sm:grid grid-cols-4 gap-2 font-bold mt-4">
                    <div>Status</div>
                    <div>Username</div>
                    <div>Email</div>
                    <div>Azioni</div>
                </div>


                <ul className="mt-4 space-y-4">
                    {users.map((user, index) => (
                        <li
                            className={`grid grid-cols-1 sm:grid-cols-4 gap-2 p-4 rounded ${
                                index % 2 === 0 ? 'bg-gray-200' : 'bg-white'
                            }`}
                            key={user.id}
                        >
                            <div className={user.status === 'ONLINE' ? 'text-green-500 font-poppins font-bold ' : 'text-red-500 font-poppins font-bold'}>
                                {user.status}
                            </div>
                            <div className='font-poppins'>{user.username}</div>
                            <div className='font-poppins'>{user.email}</div>
                            <div className="flex flex-col space-y-2 sm:flex-row sm:space-y-0 sm:space-x-2">
                                {editingUsernameIds.includes(user.id) ? (
                                    <>
                                        <input
                                            type="text"
                                            placeholder="Nuovo Username"
                                            value={usernames[user.id] || ''}
                                            onChange={(e) => handleUsernameChange(user.id, e.target.value)}
                                            className="flex-grow font-poppins p-2 rounded border border-gray-300"
                                        />
                                        <button
                                            onClick={() => handleSaveUsername(user.id)}
                                            className="bg-green-500 text-white p-2 rounded hover:bg-green-600"
                                        >
                                            Salva
                                        </button>
                                    </>
                                ) : editingPasswordIds.includes(user.id) ? (
                                    <>
                                        <input
                                            type="password"
                                            placeholder="Nuova Password"
                                            value={passwords[user.id] || ''}
                                            onChange={(e) => handlePasswordChange(user.id, e.target.value)}
                                            className="flex-grow p-2 font-poppins rounded border border-gray-300"
                                        />
                                        <button
                                            onClick={() => handleSavePassword(user.id)}
                                            className="bg-green-500 text-white p-2 rounded hover:bg-green-600"
                                        >
                                            Salva
                                        </button>
                                    </>
                                ) : (
                                    <div className="flex flex-col sm:flex-row space-y-2 sm:space-y-0 sm:space-x-2">
                                        <button
                                            onClick={() => handleChangePwd(user.id)}
                                            className="bg-red-500 text-white p-2 rounded hover:bg-red-600"
                                        >
                                            Cambia Password
                                        </button>
                                        <button
                                            onClick={() => handleChangeUsername(user.id)}
                                            className="bg-red-500 text-white p-2 rounded hover:bg-red-600"
                                        >
                                            Cambia Username
                                        </button>
                                    </div>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default Users;
