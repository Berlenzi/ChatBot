package dev.marco.chatbot_site.Security;

import dev.marco.chatbot_site.Service.TokenProviderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateInterceptorConfig {





    @Bean
    public RestTemplate restTemplateForLogin() {
        return new RestTemplate();
    }


    @Bean
    public RestTemplate restTemplateWithInterceptor(TokenProviderInterceptor tokenProviderInterceptor) {
        RestTemplate rt = new RestTemplate();
        rt.getInterceptors().add(tokenProviderInterceptor);
        return rt;
    }
}

