package dev.marco.chatbot_site.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatAttachment {


    private String name;
    private String url;

    public ChatAttachment() {
    }

    public ChatAttachment(String name, String url) {
        this.name = name;
        this.url = url;
    }


}
