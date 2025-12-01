package capyeats.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserSessionInterceptor implements HandlerInterceptor {
    public static final Set<String> UNAUTHENTICATED_ENDPOINTS  = Set.of("/","/about","/careers","/cookies","/login","/partner","/press","/privacy","/resources","/support","/terms","/signup","/signup-kitchen");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UNAUTHENTICATED_ENDPOINTS.contains(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }
        return true;
    }
}
