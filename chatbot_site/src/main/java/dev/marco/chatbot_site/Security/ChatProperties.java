package dev.marco.chatbot_site.Security;



import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat")
public class ChatProperties {


        private String imagePath;       // chat.image-path
        private String domainServer;    // chat.domain-server
        private String domainPort;      // chat.domain-port
        private String username;        // chat.username
        private String defaultLanguage; // chat.default-language
        private String botName;         // chat.bot-name
        private String botId;           // chat.bot-Id
        private String startImage;      // chat.start-image
        private boolean ssl;            // chat.ssl
        private String guestUsername;   // chat.guestUsername
        private String guestPassword;   // chat.guestPassword


        public String getImagePath() { return imagePath; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }

        public String getDomainServer() { return domainServer; }
        public void setDomainServer(String domainServer) { this.domainServer = domainServer; }

        public String getDomainPort() { return domainPort; }
        public void setDomainPort(String domainPort) { this.domainPort = domainPort; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getDefaultLanguage() { return defaultLanguage; }
        public void setDefaultLanguage(String defaultLanguage) { this.defaultLanguage = defaultLanguage; }

        public String getBotName() { return botName; }
        public void setBotName(String botName) { this.botName = botName; }

        public String getBotId() { return botId; }
        public void setBotId(String botId) { this.botId = botId; }

        public String getStartImage() { return startImage; }
        public void setStartImage(String startImage) { this.startImage = startImage; }

        public boolean isSsl() { return ssl; }
        public void setSsl(boolean ssl) { this.ssl = ssl; }

        public String getGuestUsername() { return guestUsername; }
        public void setGuestUsername(String guestUsername) { this.guestUsername = guestUsername; }

        public String getGuestPassword() { return guestPassword; }
        public void setGuestPassword(String guestPassword) { this.guestPassword = guestPassword; }
}
