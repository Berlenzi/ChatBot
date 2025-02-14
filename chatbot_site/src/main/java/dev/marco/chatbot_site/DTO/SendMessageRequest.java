
package dev.marco.chatbot_site.DTO;

import dev.marco.chatbot_site.ENUM.SenderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {


    private String message;

    private String idConversation;

    private SenderType sender;

    private String token;

    // Constructors
    public SendMessageRequest() {}

    public SendMessageRequest(String message, String idConversation , SenderType sender , String token) {
        this.message = message;
        this.idConversation = idConversation;
        this.sender = sender;
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
