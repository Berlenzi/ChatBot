package dev.marco.chatbot_site.Listener;

import dev.marco.chatbot_site.Service.UserStatusService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionListener implements HttpSessionListener {

    @Autowired
    private UserStatusService statusService;



    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("Session created: {}", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SecurityContext securityContext = (SecurityContext) se.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                try {
                    statusService.setUserStatus(username, "OFFLINE");
                    log.info("User {} set to OFFLINE due to session destruction.", username);
                } catch (Exception e) {
                    log.error("Error setting user status to OFFLINE for user {}: {}", username, e.getMessage());
                }
            }
        } else {
            log.info("No security context found in session {} during destruction.", se.getSession().getId());
        }
    }
}