package dev.marco.chatbot_site.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyAppChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key verso MyAppUser
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private MyAppUser user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    // Se servono altre colonne (es. data di creazione, stato della chat, ecc.)
    // private LocalDateTime createdAt;

    // Costruttore per comodità
    public MyAppChat(MyAppUser user) {
        this.user = user;
    }

    /**
     * Helper method per aggiungere un messaggio alla chat
     */
    public void addMessage(Message message) {
        messages.add(message);
        message.setChat(this);
    }

    /**
     * Helper method per rimuovere un messaggio dalla chat
     */
    public void removeMessage(Message message) {
        messages.remove(message);
        message.setChat(null);
    }
}
