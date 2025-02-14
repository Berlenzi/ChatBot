package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.DTO.*;

import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Repository.UserRepository;
import dev.marco.chatbot_site.Security.ChatProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ChatProvider {

    private final RestTemplate restTemplate;

    private final String pathBase;


    private String idConversation;
    private String idUser;
    private String botToken;
    private String botLanguage;




    ConfigService configService;

     ChatProperties chatProperties;

     @Autowired
    UserServices userServices;


    @Autowired
    public ChatProvider( @Qualifier("restTemplateWithInterceptor")RestTemplate restTemplate , ConfigService configService , ChatProperties chatProperties)
{
        this.restTemplate = restTemplate;
        this.configService = configService;
        this.chatProperties = chatProperties;
        this.pathBase = configService.getPathBase();
        this.botToken = chatProperties.getBotId();
        this.botLanguage = chatProperties.getDefaultLanguage();
    }



    public Chat startConversation(String botId, String language, Object startSettings , String token) {
        String url = configService.getPathBase() + "api/webchat/startConversation?language="+language +"&startSettings="+startSettings;

        int userId = userServices.findUserByToken(token);

        // Effettua la chiamata al backend del bot
        ResponseEntity<Chat> response = restTemplate.getForEntity(url , Chat.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            Chat chatResponse = response.getBody();
            for (ChatMessage cm : chatResponse.getChatMessages()) {
                if (cm.getMessage().contains("StageBot")) {
                    cm.setBot(true);
                    cm.setIdUser(String.valueOf(userId));
                }
            }
            return chatResponse;
        } else {
            throw new RuntimeException("Failed to start conversation: " + response.getStatusCode());
        }
    }

    public Chat chat(ChatBM chatMessage) {
        String url = pathBase + "api/webchat/chat";
        return restTemplate.postForObject(url, chatMessage, Chat.class);
    }


    public void chatOperator(ChatBM chatMessage) {
        String url = pathBase + "api/webchat/chatOperator";
        restTemplate.postForLocation(url, chatMessage);
    }


    public Chat getChatMessages(String idConversation, String idUser, boolean isOperator) {
        String url = pathBase+"api/webchat/getChatMessages?idConversation="+idConversation+"&idUser="+idUser+"&isOperator="+isOperator;
        return restTemplate.getForObject(url, Chat.class);
    }






    public Chat checkMessages(String idConversation, boolean isOperator) {
        String url = pathBase+"api/webchat/getChatMessages?idConversation="+idConversation+"&idUser="+idUser +"&isOperator="+isOperator;
        return restTemplate.getForObject(url,  Chat.class);
    }


    public void clearChat() {

    }




}
