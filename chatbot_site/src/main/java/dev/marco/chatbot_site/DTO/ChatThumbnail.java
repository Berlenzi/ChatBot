package dev.marco.chatbot_site.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChatThumbnail {

    private List<ChatHeroCard> heroCards;

    public ChatThumbnail() {
    }

    public ChatThumbnail(List<ChatHeroCard> heroCards) {
        this.heroCards = heroCards;
    }


}
