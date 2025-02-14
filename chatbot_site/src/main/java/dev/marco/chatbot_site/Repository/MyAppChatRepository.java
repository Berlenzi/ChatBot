package dev.marco.chatbot_site.Repository;

import dev.marco.chatbot_site.Model.MyAppChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyAppChatRepository extends JpaRepository<MyAppChat, Long> {


    Optional<MyAppChat> findByUserId(int userId);
}