package dev.marco.chatbot_site.DTO;

import lombok.Getter;

@Getter
public class GetIdDTO {
    private final String idConversation;
    private  String token;

    public GetIdDTO(String idConversation , String token) {

        this.idConversation = idConversation;
        this.token = token;


    }
}