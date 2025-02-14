package dev.marco.chatbot_site.Repository;

import dev.marco.chatbot_site.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatIdAndIdConversation(Long chatId, String idConversation);



    @Query("SELECT DISTINCT m.idConversation from  Message m WHERE  m.chat.id = :chatId ")
    List<String> getAllIdConversationByChatId(long chatId);

}