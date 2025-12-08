package capyeats.config;

import capyeats.user.model.User;
import capyeats.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    @Autowired
    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User addUserModel(HttpSession session) {
        Object obj = session.getAttribute("userId");
        return obj != null ? userService.getUserById((UUID) obj)  : null;
    }
}
