package dev.marco.chatbot_site.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChatMessage {

    private String idConversation;
    private String message;
    private String idUser;
    private List<ChatChoice> choices;
    private ChatAttachment attachment;
    private ChatThumbnail thumbnail;
    private String error;
    private boolean isSent;
    private boolean isBot;
    private long date;

    // Costruttore
    public ChatMessage() {

        this.isSent = true;
        this.isBot = false;
        this.date = System.currentTimeMillis();
    }

    public ChatMessage(String idConversation, String message) {
        this.idConversation = idConversation;
        this.message = message;
        this.isSent = true;
        this.isBot = false;
        this.date = System.currentTimeMillis();
    }



}

