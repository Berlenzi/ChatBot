package dev.marco.chatbot_site.Controller;

import dev.marco.chatbot_site.DTO.ChangePasswordRequest;
import dev.marco.chatbot_site.DTO.ChangeUsernameRequest;
import dev.marco.chatbot_site.Model.MyAppUser;
import dev.marco.chatbot_site.Service.UserServices;

import dev.marco.chatbot_site.Service.UserStatusService;
import dev.marco.chatbot_site.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")


public class AuthenticatedController {
    @Autowired
    UserServices services;

    @Autowired
    UserStatusService statusService;


    @GetMapping("/api/users")
    public ResponseEntity<List<MyAppUser>> getUsers(){
        return ResponseEntity.ok(services.getAllUsers());
    }


    @PutMapping("/api/users/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable int userId,
            @Validated @RequestBody ChangePasswordRequest request){
        try{
            services.changePassword(userId , request.getPassword());
            return ResponseEntity.ok("Password cambiata con successo.");

        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel cambio della password.");
        }
    }

    @PutMapping("/api/users/{userId}/change-username")
    public ResponseEntity<String> changeUsername(
            @PathVariable int userId,
            @Validated @RequestBody ChangeUsernameRequest request){
        try{
            services.changeUsername(userId,request.getUsername());
            return ResponseEntity.ok("Username cambiato con successo");
        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel cambio dell'username.");
        }
    }

    @GetMapping("/api/users/status/{username}")
    public ResponseEntity<?> getUserStatus(@PathVariable String username){
        try{
            String status = statusService.getUserStatus(username);
            return ResponseEntity.ok(status);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
