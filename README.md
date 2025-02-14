Panoramica del Progetto

L’applicazione realizzata consente di registrare utenti, gestire l’autenticazione
tramite JWT (Json Web Token) e fornire una chat integrata con un Bot remoto. Il
backend è sviluppato con Java e Spring Boot, mentre il frontend utilizza React,
con chiamate REST verso gli endpoint del server.
Scopo principale:
1. Autenticazione: l’utente si registra o effettua login (con username o email).
2. Gestione Stato: ad ogni login o logout, il sistema imposta l’utente come
“ONLINE” o “OFFLINE”.
3. Chat: l’utente, una volta autenticato, può avviare una conversazione con un
ChatBot. I messaggi e i relativi storici vengono memorizzati in un database.
4. Gestione Utente: presente una sezione per modificare password,
username e visualizzare lo stato di tutti gli utenti.


Tecnologie Utilizzate

1. Spring Boot
Spring Web: per i Controller REST (esposti sotto /api/...).
Spring Security + JWT: per l’autenticazione stateless. Il server non
mantiene sessioni, ma utilizza i token inviati dal client.
Spring Data JPA (Hibernate): gestione delle entità e comunicazione con il
database relazionale.
2. React
Gestisce il frontend con React Router per la navigazione (pagine /login,
/register, /chat, ecc.).
Context API: per conservare lo stato di autenticazione e il token JWT.
Fetch API: per effettuare richieste al server, inviando il token JWT nelle
intestazioni Authorization.
Tailwind CSS e Material UI: per lo stile e alcuni componenti grafici.
CryptoJS: per cifrare il token JWT conservato in localStorage, migliorando la
sicurezza sul client.
3. Database
Il database è in PostgresSQL il codice definisce le entità MyAppUser,
MyAppChat, Message, gestite tramite le Repository di Spring Data.


Funzionamento dell’Autenticazione

1. Registrazione
Endpoint: POST /api/register.
I dati inviati dal client (username, email, password) vengono salvati in una
nuova istanza di MyAppUser.
La password è criptata con BCrypt prima di essere memorizzata su DB.
2. Login
Endpoint: POST /api/login.
Se le credenziali sono valide, il server genera un JWT (tramite JwtUtils)
con scadenza configurata e lo restituisce al client.
Il server imposta inoltre lo stato dell’utente come ONLINE.
3. Protezione delle Rotte
Nel frontend, un componente PrivateRoute controlla se è presente un
token JWT valido (decifrato con CryptoJS). Se assente, reindirizza al login.
Nel backend, un filtro JwtAuthFilter intercetta ogni richiesta protetta,
estrae il token dall’header “Authorization: Bearer <token>”, lo verifica e
imposta l’oggetto di autenticazione in Spring Security.
4. Logout
Endpoint: POST /api/logout.
Imposta lo stato dell’utente come OFFLINE e il frontend rimuove il token
memorizzato, reimpostando lo stato a non autenticato.


Funzionamento della Chat

1. Avvio Chat (/api/chat)
Il frontend (in HomePage) invia una richiesta POST /api/chat col token JWT.
Il backend chiama un servizio remoto (mediante ChatProvider) per avviare
una conversazione col Bot.
Vengono salvati su DB eventuali messaggi iniziali (per esempio, messaggio
di benvenuto del Bot) associandoli ad un oggetto MyAppChat.
2. Invio e Ricezione Messaggi (/api/send)
Quando l’utente invia un messaggio, React fa POST /api/send con {
message, idConversation, sender, token }.
Il backend registra il messaggio nel DB (entità Message), poi invia il testo al
Bot remoto.
Il Bot risponde con un nuovo messaggio che viene nuovamente
memorizzato in DB come senderType = BOT.
L’insieme di questi messaggi è collegato alla stessa MyAppChat, grazie a un
attributo idConversation.
3. Storico Conversazioni (/api/history)
La sidebar del frontend chiama POST /api/history, ottenendo una lista di
“ConversationDTO”, ognuna contenente i messaggi scambiati in una
specifica conversazione.
L’utente può rivedere i vecchi scambi con il Bot e continuare una
conversazione pre-esistente.


Gestione Utente e Stato

Nel frontend è presente una pagina “Users” che consente di:
Visualizzare la lista di tutti gli utenti (con stato ONLINE/OFFLINE).
Cambiare password o username di un utente, con chiamate PUT
/api/users/{userId}/change-password e PUT
/api/users/{userId}/change-username nel controller.
Se l’utente non viene trovato viene lanciato un eccezione UserNotFoundExceptio

ENGLISH VERSION 

Project Overview

The developed application allows user registration, authentication management via JWT (Json Web Token), and provides an integrated chat with a remote Bot. The backend is developed with Java and Spring Boot, while the frontend uses React, with REST calls to server endpoints.

Main Purpose:

Authentication: Users can register or log in using a username or email.

State Management: On every login or logout, the system sets the user as “ONLINE” or “OFFLINE”.

Chat: Once authenticated, users can start a conversation with a ChatBot. Messages and chat histories are stored in a database.

User Management: A section is available for changing passwords, usernames, and viewing the status of all users.

Technologies Used:

Spring Boot

Spring Web: For REST controllers (exposed under /api/...).

Spring Security + JWT: For stateless authentication. The server does not maintain sessions but uses tokens sent by the client.

Spring Data JPA (Hibernate): Manages entities and communication with the relational database.

React

Manages the frontend with React Router for navigation (/login, /register, /chat, etc.).

Context API: Maintains authentication state and JWT tokens.

Fetch API: Makes server requests, sending JWT tokens in the Authorization headers.

Tailwind CSS and Material UI: For styling and UI components.

CryptoJS: Encrypts JWT tokens stored in localStorage for enhanced client-side security.

Database

PostgreSQL database with entities MyAppUser, MyAppChat, and Message managed via Spring Data repositories.

Authentication Flow:

Registration

Endpoint: POST /api/register.

Client-submitted data (username, email, password) is saved as a new MyAppUser instance. Passwords are encrypted with BCrypt before being stored.

Login

Endpoint: POST /api/login.

Valid credentials generate a JWT (via JwtUtils) with a configured expiration, returned to the client. The server sets the user status to ONLINE.

Route Protection

The frontend’s PrivateRoute component checks for a valid JWT token (decrypted with CryptoJS). If absent, redirects to login.

Backend JwtAuthFilter intercepts protected requests, extracts the token from the “Authorization: Bearer” header, verifies it, and sets the authentication object in Spring Security.

Logout

Endpoint: POST /api/logout.

Sets user status to OFFLINE, and the frontend removes the stored token, resetting the authentication state.

Chat Functionality:

Start Chat (/api/chat)

HomePage sends POST /api/chat with JWT token. The backend uses ChatProvider to call a remote service to start a conversation with the Bot. Initial messages (e.g., Bot welcome message) are saved in the database under MyAppChat.

Send and Receive Messages (/api/send)

Users send messages via POST /api/send with {message, idConversation, sender, token}. The backend saves messages in the Message entity, forwards the text to the Bot, and saves the Bot’s response as senderType = BOT under the same MyAppChat.

Chat History (/api/history)

Sidebar calls POST /api/history to get a list of ConversationDTOs with exchanged messages for each conversation, allowing users to revisit and continue previous chats.

User and State Management:

Users Page (Frontend)

Displays all users with ONLINE/OFFLINE status.

Allows changing user passwords or usernames via PUT /api/users/{userId}/change-password and PUT /api/users/{userId}/change-username endpoints.

Throws UserNotFoundException if the user is not found.

