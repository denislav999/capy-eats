package capyeats.web;

import capyeats.address.service.AddressService;
import capyeats.order.service.OrderService;
import capyeats.review.service.ReviewService;
import capyeats.security.UserData;
import capyeats.web.dto.AddressRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    public ProfileController(OrderService orderService, ReviewService reviewService, AddressService addressService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
        this.addressService = addressService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserData userData, Model model) {
        model.addAttribute("activePage", "profile");
        model.addAttribute("totalReviews", reviewService.getReviewsByUser(userData.getUserId()).size());
        model.addAttribute("totalOrders", orderService.getAllOrders(userData.getUserId()));
        model.addAttribute("totalSpend", orderService.getTotalPrice(userData.getUserId()));
        return "profile";
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
    public String payments(Model model) {
        model.addAttribute("activePage", "payments");
        return "payment";
    }
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        return "settings";
    }
}
