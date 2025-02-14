package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.DTO.PresetMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PresetMessageService {

    private final RestTemplate restTemplate;
     ConfigService configService;
    private final String pathBase;

    @Autowired
    public PresetMessageService(@Qualifier("restTemplateForLogin")RestTemplate restTemplate , ConfigService configService) {
        this.restTemplate = restTemplate;
        this.configService = configService;
        this.pathBase = configService.getPathBase();
    }


    public List<PresetMessage> getMessages() {
        String url = pathBase + "api/presetmessage/getMessages";


        PresetMessage[] responseArray =
                restTemplate.getForObject(url, PresetMessage[].class);


        if (responseArray == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(responseArray);
    }


    public List<PresetMessage> getMessagesFromIdConversation(String idConversation) {
        String url = pathBase
                + "api/presetmessage/getMessagesFromConversationId?conversationId="
                + idConversation;

        PresetMessage[] responseArray =
                restTemplate.getForObject(url, PresetMessage[].class);

        if (responseArray == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(responseArray);
    }
}