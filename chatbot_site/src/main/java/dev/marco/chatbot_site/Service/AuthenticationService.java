package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.DTO.LoginRequest;
import dev.marco.chatbot_site.DTO.RegisterRequest;
import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private ChatService chatService;




    public MyAppUser signup(RegisterRequest input) {
        MyAppUser user = new MyAppUser();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        userRepository.save(user);
        chatService.createChat(user.getId());
        return userRepository.save(user);
    }

    public MyAppUser authenticate(LoginRequest input) {
      Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}