package capyeats.security;

import capyeats.user.model.User;
import capyeats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class UserSessionInterceptor implements HandlerInterceptor {
    private static final String INACTIVE_USER_MESSAGE = "inactive user";
    public static final Set<String> LANDING_PAGE_ENDPOINTS  = Set.of("/","/about","/careers","/cookies","/login","/partner","/press","/privacy","/resources","/support","/terms","/signup","/signup-kitchen");

    private final UserService userService;

    @Autowired
    public UserSessionInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (LANDING_PAGE_ENDPOINTS.contains(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/login");
            return false;
        }
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("/login");
            return false;
        }
        User user = userService.getUserById((UUID)userId);
        if (!user.isActive()){
            session.invalidate();
            response.sendRedirect("/login?loginAttempt="+INACTIVE_USER_MESSAGE);
            return false;
        }

        return true;
    }

}
