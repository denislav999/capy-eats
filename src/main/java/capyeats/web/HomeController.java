package capyeats.web;

import capyeats.security.UserData;
import capyeats.user.model.UserRole;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserData userData) {
        if (userData == null) {
            return "index";
        }
        return userData.getRole() == UserRole.KITCHEN_OWNER ? "redirect:/kitchen-overview" : "redirect:/home";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/press")
    public String press() {
        return "press";
    }

    @GetMapping("/careers")
    public String careers() {
        return "careers";
    }
    @GetMapping("/resources")
    public String resources(){
        return "resources";
    }

    @GetMapping("/partner")
    public String partner(){
        return "partner";
    }

    @GetMapping("/support")
    public String support(){
        return "support";
    }
    @GetMapping("/privacy")
    public String privacy(){
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms(){
        return "terms";
    }

    @GetMapping("/cookies")
    public String cookies(){
        return "cookies";
    }
}
