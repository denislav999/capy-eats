package capyeats.web;

import capyeats.user.model.User;
import capyeats.user.service.UserService;
import capyeats.web.dto.LoginRequest;
import capyeats.web.dto.RegisterUserRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignInController {

    private final UserService userService;

    @Autowired
    public SignInController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login (Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        session.setAttribute("userId", user.getId());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("registerRequest", new RegisterUserRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("registerRequest")RegisterUserRequest registerUserRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (userService.userExists(registerUserRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.userExists", "Username already exists");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.register(registerUserRequest);
        redirectAttributes.addFlashAttribute("message", "Registration Successful");
        return "redirect:/login";
    }

    @GetMapping("/signup-kitchen")
    public String signUpKitchen(){
        return "signup-kitchen";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
