package capyeats.web;

import capyeats.address.repository.AddressRepository;
import capyeats.cart.service.CartService;
import capyeats.order.model.Order;
import capyeats.order.service.CheckoutService;
import capyeats.order.service.CheckoutService.CheckoutException;
import capyeats.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
public class CheckoutController {
    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final AddressRepository addressRepository;

    public CheckoutController(CartService cartService, CheckoutService checkoutService,
                              AddressRepository addressRepository) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserData userData, Model model) {
        populateCheckout(userData.getUserId(), model);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@AuthenticationPrincipal UserData userData,
                             @RequestParam(name = "addressId", required = false) UUID addressId,
                             @RequestParam(name = "quotedTotal", required = false) BigDecimal quotedTotal,
                             Model model, RedirectAttributes redirectAttributes) {
        UUID userId = userData.getUserId();
        try {
            Order order = cartService.checkout(cart -> checkoutService.placeOrder(userId, addressId, cart, quotedTotal));
            redirectAttributes.addFlashAttribute("orderId", order.getId());
            return "redirect:/checkout/complete";
        } catch (CheckoutException exception) {
            model.addAttribute("checkoutError", exception.getMessage());
            model.addAttribute("selectedAddressId", addressId);
            populateCheckout(userId, model);
            return "checkout";
        }
    }

    @GetMapping("/checkout/complete")
    public String complete(Model model) {
        return model.containsAttribute("orderId") ? "checkout-complete" : "redirect:/cart";
    }

    private void populateCheckout(UUID userId, Model model) {
        try {
            model.addAttribute("quote", checkoutService.quote(cartService.getCart()));
            model.addAttribute("addresses", addressRepository.findAllByUser_IdOrderByLabelAsc(userId));
        } catch (CheckoutException exception) {
            model.addAttribute("checkoutError", exception.getMessage());
        }
    }
}
