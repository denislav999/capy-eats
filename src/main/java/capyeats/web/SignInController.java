package capyeats.web;

import capyeats.user.service.UserService;
import capyeats.web.dto.LoginRequest;
import capyeats.web.dto.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignInController {

    private final UserService userService;

    @Autowired
    public SignInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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
            ModelAndView mv = new ModelAndView("signup");
            mv.addObject(registerUserRequest);
            return mv;
        }
        userService.register(registerUserRequest);
        redirectAttributes.addFlashAttribute("message", "Registration Successful");
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/signup-kitchen")
    public String signUpKitchen(){
        return "signup-kitchen";
    }
}
