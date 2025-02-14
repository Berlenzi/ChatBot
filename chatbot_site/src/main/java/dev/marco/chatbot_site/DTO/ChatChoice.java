package dev.marco.chatbot_site.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatChoice {

    private String title;
    private String value;
    private String description;
    private Boolean url; // se rappresenta un flag, altrimenti String

    public ChatChoice() {
        // Costruttore vuoto
    }

    public ChatChoice(String title, String value, String description, Boolean url) {
        this.title = title;
        this.value = value;
        // Se la descrizione è null, impostiamola a ""
        this.description = (description == null) ? "" : description;
        this.url = url;
    }


}
