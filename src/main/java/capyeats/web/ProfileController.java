package capyeats.web;

import capyeats.address.service.AddressService;
import capyeats.order.service.OrderService;
import capyeats.review.service.ReviewService;
import capyeats.security.UserData;
import capyeats.user.model.User;
import capyeats.user.service.UserService;
import capyeats.web.dto.AddressRequest;
import capyeats.web.dto.ProfileUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final OrderService orderService;
    private final ReviewService reviewService;
    private final AddressService addressService;
    private final UserService userService;

    @Autowired
    public ProfileController(OrderService orderService, ReviewService reviewService, AddressService addressService, UserService userService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserData userData, Model model) {
        User user = userService.getUserById(userData.getUserId());
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName(user.getFullName());
        request.setPhoneNumber(user.getPhoneNumber());
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());

        model.addAttribute("profileUpdateRequest", request);
        addProfileStats(userData.getUserId(), model);
        return "profile";
    }

    private void addProfileStats(UUID userId, Model model) {
        model.addAttribute("activePage", "profile");
        model.addAttribute("totalReviews", reviewService.getReviewsByUser(userId).size());
        model.addAttribute("totalOrders", orderService.getAllOrders(userId));
        model.addAttribute("totalSpend", orderService.getTotalPrice(userId));
    }

    @PostMapping
    public String profile(@AuthenticationPrincipal UserData userData,
                          @Valid @ModelAttribute("profileUpdateRequest") ProfileUpdateRequest request,
                          BindingResult bindingResult, Model model,
                          HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        UUID userId = userData.getUserId();
        if (!bindingResult.hasFieldErrors("email") && !userService.isEmailAvailable(request.getEmail(), userId)) {
            bindingResult.rejectValue("email", "duplicate", "Email already in use");
        }
        if (!bindingResult.hasFieldErrors("username") && !userService.isUsernameAvailable(request.getUsername(), userId)) {
            bindingResult.rejectValue("username", "duplicate", "Username already in use");
        }
        if (bindingResult.hasErrors()) {
            addProfileStats(userId, model);
            model.addAttribute("profileEditing", true);
            return "profile";
        }

        userService.updateProfile(userId, request);
        if (!userData.getUsername().equals(request.getUsername())) {
            new SecurityContextLogoutHandler().logout(servletRequest, servletResponse,
                    SecurityContextHolder.getContext().getAuthentication());
            return "redirect:/login";
        }
        return "redirect:/profile";
    }

    @GetMapping("/addresses")
    public String addresses(Model model) {
        model.addAttribute("activePage", "addresses");
        model.addAttribute("addressRequest", new AddressRequest());
        return "addresses";
    }


    @PostMapping("/addresses")
    public String addAddress(@Valid @ModelAttribute("addressRequest") AddressRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserData userData,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "addresses");
            return "addresses";
        }

        UUID userId = userData.getUserId();

        addressService.createAddress(userId, request);

        return "redirect:/profile/addresses";
    }

    @GetMapping("/addresses/{addressId}/edit")
    public String editAddress(
            @PathVariable("addressId") UUID addressId,
            @AuthenticationPrincipal UserData userData,
            Model model) {

        UUID userId = userData.getUserId();

        AddressRequest request =
                addressService.getAddressForEdit(userId, addressId);

        model.addAttribute("activePage", "addresses");
        model.addAttribute("editAddressId", addressId);
        model.addAttribute("addressRequest", request);

        return "addresses";
    }


    @PostMapping("/addresses/{addressId}")
    public String updateAddress(
            @PathVariable("addressId") UUID addressId,
            @Valid @ModelAttribute("addressRequest") AddressRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserData userData,
            Model model) {

        UUID userId = userData.getUserId();

        if (bindingResult.hasErrors()) {
            addressService.getAddressForEdit(userId, addressId);

            model.addAttribute("activePage", "addresses");
            model.addAttribute("editAddressId", addressId);

            return "addresses";
        }

        addressService.updateAddress(userId, addressId, request);

        return "redirect:/profile/addresses";
    }

    @PostMapping("/addresses/{addressId}/delete")
    public String deleteAddress(
            @PathVariable("addressId") UUID addressId,
            @AuthenticationPrincipal UserData userData) {

        UUID userId = userData.getUserId();
        addressService.deleteAddress(userId, addressId);

        return "redirect:/profile/addresses";
    }

    @GetMapping("/orders")
    public String orders(Model model,@AuthenticationPrincipal UserData userData) {
        model.addAttribute("activePage", "orders");
        model.addAttribute("orders", orderService.getOrdersByUserId(userData.getUserId()));
        return "orders";
    }
    @GetMapping("/payments")
    public String payments() {
        return "redirect:/profile";
    }
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        return "settings";
    }
}
