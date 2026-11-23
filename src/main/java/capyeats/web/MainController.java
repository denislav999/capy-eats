package capyeats.web;

import capyeats.cart.model.Cart;
import capyeats.cart.service.CartService;
import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.service.KitchenService;
import capyeats.menu.model.MenuItem;
import capyeats.menu.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class MainController {
    private final KitchenService kitchenService;
    private final MenuItemService menuItemService;
    private final CartService cartService;

    public MainController(KitchenService kitchenService, MenuItemService menuItemService, CartService cartService) {
        this.kitchenService = kitchenService;
        this.menuItemService = menuItemService;
        this.cartService = cartService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("newestKitchens", kitchenService.getNewestKitchens());
        return "home";
    }

    @GetMapping("/kitchens/{id}")
    public String kitchen(@PathVariable("id") UUID kitchenId, Model model) {
        model.addAttribute("kitchen", kitchenService.getKitchenById(kitchenId));
        model.addAttribute("menuItems", menuItemService.getAvailableMenuItems(kitchenId));
        return "customer-kitchen";
    }

    @PostMapping("/kitchens/{kitchenId}/items/{itemId}/cart")
    public String addToCart(@PathVariable("kitchenId") UUID kitchenId,
                            @PathVariable("itemId") UUID itemId,
                            RedirectAttributes redirectAttributes) {
        Kitchen kitchen = kitchenService.getKitchenById(kitchenId);
        MenuItem menuItem = menuItemService.getMenuItem(kitchenId, itemId);

        if (!kitchen.isOpen()) {
            redirectAttributes.addFlashAttribute("cartError", "This kitchen is paused.");
        } else if (!menuItem.isAvailable()) {
            redirectAttributes.addFlashAttribute("cartError", "This dish is unavailable.");
        } else if (!cartService.addItem(menuItem, 1)) {
            redirectAttributes.addFlashAttribute("cartError", "Your cart contains dishes from another kitchen. Remove them before adding this dish.");
        } else {
            redirectAttributes.addFlashAttribute("cartMessage", "Added to cart.");
        }

        return "redirect:/kitchens/" + kitchenId;
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }

    @PostMapping("/cart/items/{itemId}/remove")
    public String removeFromCart(@PathVariable("itemId") UUID itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }
}
