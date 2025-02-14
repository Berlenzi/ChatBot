package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.ENUM.SenderType;
import dev.marco.chatbot_site.Model.Message;
import dev.marco.chatbot_site.Model.MyAppChat;
import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Repository.MessageRepository;
import dev.marco.chatbot_site.Repository.MyAppChatRepository;
import dev.marco.chatbot_site.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyAppChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public MyAppChat createChat(int userId) {
        MyAppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));


        MyAppChat chat = new MyAppChat(user);


        return chatRepository.save(chat);
    }

    public MyAppChat addMessageToChat(Long chatId, String content, SenderType senderType , String idConversation) {
        MyAppChat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat non trovata con ID: " + chatId));

        Message message = new Message(content, senderType , idConversation);
        chat.addMessage(message);


        return chatRepository.save(chat);
    }

    public Long findChatIdByUserId(int userId) {
        MyAppChat chat = chatRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Chat non trovata per l'utente con ID: " + userId));
        return chat.getId();
    }

    public List<Message> findMessagesByChatIdAndConversationId(Long chatId, String idConversation) {
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Chat non trovata con ID: " + chatId);
        }
        return messageRepository.findAllByChatIdAndIdConversation(chatId, idConversation);
    }

    public List<String> getAllIdConversationByChatId(long chatId) {
        return messageRepository.getAllIdConversationByChatId(chatId);
    }
}
