package dev.marco.chatbot_site.DTO;

import dev.marco.chatbot_site.ENUM.SenderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private String id;
    private String text;
    private SenderType sender;


    // Costruttori


    public MessageDTO(String id, String text, SenderType sender) {
        this.id = id;
        this.text = text;
        this.sender = sender;
    }

}