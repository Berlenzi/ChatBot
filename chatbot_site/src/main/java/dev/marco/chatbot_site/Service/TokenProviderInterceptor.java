package dev.marco.chatbot_site.Service;



import dev.marco.chatbot_site.Security.ChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenProviderInterceptor implements ClientHttpRequestInterceptor {

    private final LoginService loginService;

    @Autowired
    ChatProperties chatProperties;

    @Autowired
    public TokenProviderInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        // Create a wrapper for the original request
        HttpRequest wrapper = new HttpRequestWrapper(request);

        // Get the current token
        String currentToken = loginService.getToken();
        if (currentToken != null) {
            // Add all required headers
            wrapper.getHeaders().setBearerAuth(currentToken);
            wrapper.getHeaders().add("Cache-Control", "no-cache");
            wrapper.getHeaders().add("Pragma", "no-cache");
            wrapper.getHeaders().add("Expires", "Sat, 01 Jan 2000 00:00:00 GMT");

            String botToken = chatProperties.getBotId();
            if (botToken != null) {
                wrapper.getHeaders().add("botToken", botToken);
            }
        }

        // Execute with the wrapped request
        ClientHttpResponse response = execution.execute(wrapper, body);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            loginService.clearToken();
        }

        return response;
    }
}