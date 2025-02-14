// src/main/java/dev/marco/chatbot_site/Service/UserStatusService.java
package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Repository.UserRepository;
import dev.marco.chatbot_site.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserStatusService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public void setUserStatus(String username, String status) throws UserNotFoundException {
        MyAppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato: " + username));
        user.setStatus(status);
        if ("ONLINE".equals(status)) {
            user.setLastActivity(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateLastActivity(String username) throws UserNotFoundException {
        MyAppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato: " + username));
        user.setLastActivity(LocalDateTime.now());
        userRepository.save(user);
    }

    public List<MyAppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<MyAppUser> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    public String getUserStatus(String username) throws UserNotFoundException {
        MyAppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato: " + username));
        return user.getStatus();
    }


}
