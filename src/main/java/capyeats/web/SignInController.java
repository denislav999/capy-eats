package capyeats.web;

import capyeats.user.model.User;
import capyeats.user.service.UserService;
import capyeats.web.dto.LoginRequest;
import capyeats.web.dto.RegisterUserRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class SignInController {

    private final UserService userService;

    @Autowired
    public SignInController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public ModelAndView login () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        session.setAttribute("userId", user.getId());
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/signup")
    public ModelAndView signUp(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        modelAndView.addObject("registerRequest", new RegisterUserRequest());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signUp(@Valid @ModelAttribute("registerRequest")RegisterUserRequest registerUserRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (userService.userExists(registerUserRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.userExists", "Username already exists");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("signup");
        }
        userService.register(registerUserRequest);
        redirectAttributes.addFlashAttribute("message", "Registration Successful");
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/signup-kitchen")
    public String signUpKitchen(){
        return "signup-kitchen";
    }

    @GetMapping("/home")
    public ModelAndView home(HttpSession session ) {
        User user = userService.getUserById((UUID)session.getAttribute("userId"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
