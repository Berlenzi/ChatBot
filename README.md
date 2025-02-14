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
