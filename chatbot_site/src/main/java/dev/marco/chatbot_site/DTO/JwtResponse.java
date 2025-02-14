package dev.marco.chatbot_site.DTO;

public class JwtResponse {

    private String token;
    private String message;
    private boolean success;
    private long expiresIn;



    public JwtResponse(String token, String message, boolean success , long expiresIn) {
        this.token = token;
        this.message = message;
        this.success = success;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
    public long getExpiresIn() {
        return expiresIn;
    }




    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + token + '\'' +
                "expiresIn='" + expiresIn + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
