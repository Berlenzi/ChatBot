package dev.marco.chatbot_site.Controller;


import dev.marco.chatbot_site.DTO.*;
import dev.marco.chatbot_site.Model.*;
import dev.marco.chatbot_site.Service.*;
import dev.marco.chatbot_site.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private BlacklistService blacklistService;


    JwtUtils jwtUtils;


    AuthenticationService authenticationService;

    UserStatusService statusService;







    public AuthController( JwtUtils jwtUtils, AuthenticationService authenticationService , UserStatusService statusService) {
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
        this.statusService = statusService;
    }



    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        MyAppUser registeredUser = authenticationService.signup(registerRequest);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        MyAppUser authenticatedUser = authenticationService.authenticate(loginRequest);


        String jwtToken = jwtUtils.generateToken(authenticatedUser);

        statusService.setUserStatus(authenticatedUser.getUsername(), "ONLINE");


        return ResponseEntity.ok(new JwtResponse(jwtToken, "Login successful!", true , jwtUtils.getExpirationTime()));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) throws UserNotFoundException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            String username = jwtUtils.extractUsername(token);

            //blacklistService.blacklistToken(token);

            statusService.setUserStatus(username, "OFFLINE");
            return ResponseEntity.ok(new JwtResponse(null, "Logout successful!", true, jwtUtils.getExpirationTime()));
        } else {
            // Non c’è token nell’header
            return ResponseEntity.badRequest()
                    .body(new JwtResponse(null, "No JWT token found in request", false , jwtUtils.getExpirationTime()));
        }
    }
}
