package capyeats.web;

import capyeats.kitchen.service.KitchenService;
import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.service.UserService;
import capyeats.web.dto.KitchenOwnerRequest;
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
    private final KitchenService kitchenService;

    @Autowired
    public SignInController(UserService userService, KitchenService kitchenService) {
        this.userService = userService;
        this.kitchenService = kitchenService;
    }
     
    @GetMapping("/login")
    public String login (Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            User user = userService.login(loginRequest);
            session.setAttribute("userId", user.getId());
            return "redirect:/home";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
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

        if (userService.emailExists(registerUserRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.emailExists", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }


        userService.register(registerUserRequest, UserRole.USER);
        redirectAttributes.addFlashAttribute("message", "Registration Successful");
        return "redirect:/login";
    }

    @GetMapping("/signup-kitchen")
    public String signUpKitchenOwner(Model model){
        model.addAttribute("kitchenOwnerRequest", new KitchenOwnerRequest());
        return "signup-kitchen";
    }

    @PostMapping("/signup-kitchen")
    public String signUpKitchenOwner(@Valid @ModelAttribute("kitchenOwnerRequest") KitchenOwnerRequest kitchenOwnerRequest, BindingResult bindingResult) {
        if (userService.userExists(kitchenOwnerRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.userExists", "Username already exists");
        }

        if (kitchenService.existsKitchen(kitchenOwnerRequest.getKitchenName())){
            bindingResult.rejectValue("kitchenName", "error.kitchenExists", "Kitchen already exists");
        }

        if (userService.emailExists(kitchenOwnerRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.emailExists", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            return "signup-kitchen";
        }

        kitchenService.registerKitchenOwner(kitchenOwnerRequest);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
