package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Repository.UserRepository;
import dev.marco.chatbot_site.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@AllArgsConstructor
@Primary
public class UserServices implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyAppUser> user = repository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public int findUserByToken(String token){
        String replaceTokenString = token.replace('"', ',');
        String[] newToken = replaceTokenString.split(",");
        String username = jwtUtils.extractUsername(newToken[3]);

        Optional<MyAppUser> findUser = repository.findByUsername(username);
        return findUser.get().getId();

    }

    public int findByToken(String token){
        String username = jwtUtils.extractUsername(token);
        Optional<MyAppUser> findUser = repository.findByUsername(username);
        return findUser.get().getId();
    }

    public List<MyAppUser> getAllUsers(){
        return repository.findAll();
    }

    @Transactional
    public void changePassword(int userId, String newPassword) throws UserNotFoundException {
        MyAppUser user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));


        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        repository.save(user);

    }
    @Transactional
    public void changeUsername(int userId, String newUsername) throws UserNotFoundException {
        MyAppUser user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));

        user.setUsername(newUsername);
        repository.save(user);

    }
}

