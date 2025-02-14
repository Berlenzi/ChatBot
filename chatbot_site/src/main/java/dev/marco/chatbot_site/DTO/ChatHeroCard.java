package dev.marco.chatbot_site.DTO;



import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChatHeroCard {

    private String title;
    private String url;
    private List<ChatChoice> actions;

    public ChatHeroCard() {
    }

    public ChatHeroCard(String title, String url, List<ChatChoice> actions) {
        this.title = title;
        this.url = url;
        this.actions = actions;
    }


}
