package dev.marco.chatbot_site.Service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlacklistService {


    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();


    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }


}
