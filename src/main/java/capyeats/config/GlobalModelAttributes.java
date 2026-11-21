package capyeats.config;

import capyeats.security.UserData;
import capyeats.user.model.User;
import capyeats.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    @Autowired
    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User addUserModel(@AuthenticationPrincipal UserData userData) {
        return userData == null ? null : userService.getUserById(userData.getUserId());
    }
}
