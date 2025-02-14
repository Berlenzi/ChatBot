package dev.marco.chatbot_site.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Boolean success;
    private String message;
}
