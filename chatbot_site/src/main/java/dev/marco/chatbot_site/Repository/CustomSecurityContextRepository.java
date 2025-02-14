package dev.marco.chatbot_site.Repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityContextRepository implements SecurityContextRepository {

    private static final String SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";



    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        // Recupera il SecurityContext dalla sessione
        SecurityContext context = (SecurityContext) requestResponseHolder.getRequest().getSession().getAttribute(SECURITY_CONTEXT_KEY);
        if (context == null) {
            context = SecurityContextHolder.createEmptyContext();
        }
        return context;
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        return SecurityContextRepository.super.loadDeferredContext(request);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(SECURITY_CONTEXT_KEY, context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return request.getSession().getAttribute(SECURITY_CONTEXT_KEY) != null;
    }
}
