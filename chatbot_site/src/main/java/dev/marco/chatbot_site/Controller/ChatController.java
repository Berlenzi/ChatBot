package dev.marco.chatbot_site.Controller;

import dev.marco.chatbot_site.DTO.*;
import dev.marco.chatbot_site.ENUM.SenderType;
import dev.marco.chatbot_site.Model.Message;
import dev.marco.chatbot_site.Security.ChatProperties;
import dev.marco.chatbot_site.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class ChatController {


    private String guestUsername;


    private String guestPassword;


    private String botId;


    private String defaultLanguage;




    @Autowired
    private final LoginService loginService;

    @Autowired
     private UserServices userServices;

    @Autowired
    private ChatService chatService;



    ChatProperties chatProperties;
    private final ChatProvider chatProvider;




    public ChatController(LoginService loginService, ChatProvider chatProvider , ChatProperties chatProperties ) {
        this.loginService = loginService;
        this.chatProvider = chatProvider;
        this.chatProperties = chatProperties;
        this.guestUsername = chatProperties.getGuestUsername();
        this.guestPassword = chatProperties.getGuestPassword();
        this.botId = chatProperties.getBotId();
        this.defaultLanguage = chatProperties.getDefaultLanguage();


    }


    @PostMapping( value ="/api/chat")
    public ResponseEntity<Chat> startChat(@RequestBody String tokenLogin) {
        try {

            UserLogin userLogin = new UserLogin();
            userLogin.setUsernameOrEmail(this.guestUsername);
            userLogin.setPassword(this.guestPassword);


            loginService.login(userLogin);


            Chat chat = chatProvider.startConversation(
                    botId,
                    defaultLanguage,
                    null,
                    tokenLogin
            );



            return ResponseEntity.ok(chat);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    @PostMapping(value = "/api/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chat> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            String message = request.getMessage();
            String idConversation = request.getIdConversation();
            String token = request.getToken();

            SenderType sender = request.getSender();
            int userid = userServices.findByToken(token);
            Long chatId = chatService.findChatIdByUserId(userid);

            ChatBM chatBM = new ChatBM(idConversation, message, null, null , chatProperties);
            Chat chat = chatProvider.chat(chatBM);

            ChatMessage botResponse = chat.getChatMessages().getFirst();


            chatService.addMessageToChat(chatId , request.getMessage() , sender , idConversation);
            chatService.addMessageToChat(chatId, botResponse.getMessage(), SenderType.BOT , idConversation);


            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    @PostMapping(value = "/api/history", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<ConversationDTO>> retrieveMessage(@RequestBody RetreieveRequest request ){
        try {
            // 1. Recupera l'userId tramite il token
            int userId = userServices.findByToken(request.getToken());


            long chatId = chatService.findChatIdByUserId(userId);

            // 3. Recupera tutte le idConversation associate al chatId
            List<String> idConversations = chatService.getAllIdConversationByChatId(chatId);

            // 4. Per ogni idConversation, recupera i messaggi e crea un ConversationDTO
            List<ConversationDTO> conversations = idConversations.stream().map(idConversation -> {
                List<Message> messages = chatService.findMessagesByChatIdAndConversationId(chatId, idConversation);
                List<MessageDTO> messageDTOs = messages.stream()
                        .map(msg -> new MessageDTO(String.valueOf(msg.getId()), msg.getContent(), msg.getSenderType()))
                        .collect(Collectors.toList());
                return new ConversationDTO(idConversation, messageDTOs);
            }).collect(Collectors.toList());

            // 5. Restituisce la lista di ConversationDTO
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
