package capyeats.web;

import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.service.KitchenService;
import capyeats.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KitchenOwnerController {
    private final KitchenService kitchenService;

    public KitchenOwnerController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @GetMapping("/kitchen-overview")
    public String overview(@AuthenticationPrincipal UserData userData, Model model) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);
        return  "kitchen-overview";
    }

    @GetMapping("/kitchen-menu")
    public String menu(@AuthenticationPrincipal UserData userData, Model model) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);
        return "kitchen-menu";
    }

    @GetMapping("/kitchen-orders")
    public String orders(@AuthenticationPrincipal UserData userData, Model model) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);
        return "kitchen-orders";
    }

    @GetMapping("/kitchen-profile")
    public String reviews(@AuthenticationPrincipal UserData userData, Model model) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);
        return "kitchen-profile";
    }

    @GetMapping("/kitchen-settings")
    public String settings(@AuthenticationPrincipal UserData userData, Model model) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);
        return "kitchen-settings";
    }
}
