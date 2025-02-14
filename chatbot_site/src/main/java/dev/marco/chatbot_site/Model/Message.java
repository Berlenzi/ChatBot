package dev.marco.chatbot_site.Model;

import dev.marco.chatbot_site.ENUM.SenderType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private MyAppChat chat;

    @Column(name = "id_conversation", nullable = false)
    private String idConversation;

    public Message(String content, SenderType senderType , String idConversation) {
        this.content = content;
        this.senderType = senderType;
        this.idConversation = idConversation;
    }
}