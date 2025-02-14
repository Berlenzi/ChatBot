package dev.marco.chatbot_site.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationDTO {
    private String idConversation;
    @Getter
    private List<MessageDTO> messages;

    // Costruttori


    public ConversationDTO(String idConversation, List<MessageDTO> messages) {
        this.idConversation = idConversation;
        this.messages = messages;
    }

    public void setChatId(String idConversation) {
        this.idConversation = idConversation;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}

