package dev.marco.chatbot_site.DTO;
import dev.marco.chatbot_site.Security.ChatProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class ChatBM {

    private String botId;
    private String chatID;
    private String chatMessage;
    private Resource chatResource;
    private String value;

    ChatProperties chatProperties;


    public ChatBM(String chatID, String chatMessage, Resource chatResource, String value , ChatProperties chatProperties) {
        this.chatID = chatID;
        this.chatMessage = chatMessage;
        this.chatResource = chatResource;
        this.value = value;
        this.chatProperties = chatProperties;
        this.botId = chatProperties.getBotId();
    }

}