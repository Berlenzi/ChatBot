package dev.marco.chatbot_site.DTO;

import dev.marco.chatbot_site.ENUM.SenderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetreieveRequest {

    private String idConversation;
    private String token;

    public RetreieveRequest(String idConversation , String token) {

        this.idConversation = idConversation;
        this.token = token;
    }




}
