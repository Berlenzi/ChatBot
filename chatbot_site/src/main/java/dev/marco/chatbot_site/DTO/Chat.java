package dev.marco.chatbot_site.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class Chat {

    private List<ChatMessage> chatMessages;
    private int notificationsReceived;
    private boolean chatLocked;

    public Chat() {
        this.chatLocked = false;
    }

    public Chat(List<ChatMessage> chatMessages, int notificationsReceived) {
        this.chatMessages = chatMessages;
        this.notificationsReceived = notificationsReceived;
        this.chatLocked = false;
    }


}
