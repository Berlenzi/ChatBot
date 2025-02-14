package dev.marco.chatbot_site.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

    private String usernameOrEmail;

    private String password;


}
