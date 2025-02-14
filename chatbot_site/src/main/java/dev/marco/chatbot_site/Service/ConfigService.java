package dev.marco.chatbot_site.Service;

import dev.marco.chatbot_site.Security.ChatProperties;
import org.springframework.stereotype.Service;


@Service
public class ConfigService {

    private final ChatProperties chatProperties;


    private final String sslProtocol;
    private final String wslProtocol;
    private final String domain;
    private final String pathBase;
    private final String wsBase;

    public ConfigService(ChatProperties chatProperties) {
        this.chatProperties = chatProperties;


        this.sslProtocol = chatProperties.isSsl()? "https" : "http";
        this.wslProtocol = chatProperties.isSsl() ? "wss" : "ws";


        this.domain = chatProperties.getDomainServer();


        this.pathBase = this.sslProtocol + "://" + this.domain + "/";


        this.wsBase = this.wslProtocol + "://" + this.domain + "/";
    }


    public ChatProperties getChatProperties() {
        return chatProperties;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public String getWslProtocol() {
        return wslProtocol;
    }

    public String getDomain() {
        return domain;
    }

    public String getPathBase() {
        return pathBase;
    }

    public String getWsBase() {
        return wsBase;
    }


}
