package dev.marco.chatbot_site.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marco.chatbot_site.DTO.UserLogin;
import dev.marco.chatbot_site.Security.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class LoginService {

    public static final String TOKEN_NAME = "webchatToken";

    private final RestTemplate restTemplate;
    private final StorageProvider storageProvider;
    ConfigService configService;
    private final  String pathBase;

    @Autowired
    public LoginService (@Qualifier("restTemplateForLogin")RestTemplate restTemplate, StorageProvider storageProvider , ConfigService configService) {
        this.restTemplate = restTemplate;
        this.storageProvider = storageProvider;
        this.configService = configService;
        this.pathBase = configService.getPathBase();
    }


    public void login(UserLogin userLogin) throws JsonProcessingException {



            String url = pathBase + "api/account/login";

            ResponseEntity<String> response = restTemplate.postForEntity(url, userLogin, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String receivedToken = response.getBody();
                String accessToken = extractAccessToken(receivedToken);
                if (accessToken != null && !accessToken.isEmpty()) {
                    setToken(accessToken);
                } else {
                    throw new RuntimeException("Login response has no token");
                }
            } else {
                throw new RuntimeException("Login failed: " + response.getStatusCode());
            }


    }


    public String getToken() {
        return storageProvider.getValue(TOKEN_NAME);
    }

    public void setToken(String token) {
        storageProvider.setValue(TOKEN_NAME, token);
    }

    public void clearToken() {
        storageProvider.removeValue(TOKEN_NAME);
    }

    public static String extractAccessToken(String jsonString) {
        String tokenKey = "\"accessToken\":\"";
        int startIndex = jsonString.indexOf(tokenKey);
        if (startIndex == -1) {
            // Token non trovato
            return null;
        }
        startIndex += tokenKey.length();
        int endIndex = jsonString.indexOf("\"", startIndex);
        if (endIndex == -1) {
            // Fine del token non trovato
            return null;
        }
        return jsonString.substring(startIndex, endIndex);
    }


}
