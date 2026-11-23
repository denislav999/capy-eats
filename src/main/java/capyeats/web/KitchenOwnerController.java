package capyeats.web;

import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.service.KitchenService;
import capyeats.menu.service.MenuItemService;
import capyeats.security.UserData;
import capyeats.web.dto.KitchenSettingsRequest;
import capyeats.web.dto.MenuItemRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class KitchenOwnerController {
    private final KitchenService kitchenService;
    private final MenuItemService menuItemService;

    public KitchenOwnerController(KitchenService kitchenService, MenuItemService menuItemService) {
        this.kitchenService = kitchenService;
        this.menuItemService = menuItemService;
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
        populateMenu(kitchen, model);
        model.addAttribute("menuItemRequest", new MenuItemRequest());
        return "kitchen-menu";
    }

    @PostMapping("/kitchen-menu")
    public String addMenuItem(
            @Valid @ModelAttribute("menuItemRequest") MenuItemRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserData userData,
            Model model) {

        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());

        if (bindingResult.hasErrors()) {
            populateMenu(kitchen, model);
            return "kitchen-menu";
        }

        menuItemService.createMenuItem(kitchen, request);
        return "redirect:/kitchen-menu";
    }

    @PostMapping("/kitchen-menu/{itemId}")
    public String editMenuItem(
            @PathVariable("itemId") UUID itemId,
            @Valid @ModelAttribute("editMenuItemRequest") MenuItemRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserData userData,
            Model model) {

        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        if (bindingResult.hasErrors()) {
            menuItemService.getMenuItem(kitchen.getId(), itemId);
            populateMenu(kitchen, model);
            model.addAttribute("menuItemRequest", new MenuItemRequest());
            model.addAttribute("editItemId", itemId);
            return "kitchen-menu";
        }

        menuItemService.updateMenuItem(kitchen.getId(), itemId, request);
        return "redirect:/kitchen-menu";
    }

    @PostMapping("/kitchen-menu/{itemId}/delete")
    public String deleteMenuItem(@PathVariable("itemId") UUID itemId,
                                 @AuthenticationPrincipal UserData userData) {
        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        menuItemService.deleteMenuItem(kitchen.getId(), itemId);

        return "redirect:/kitchen-menu";
    }

    private void populateMenu(Kitchen kitchen, Model model) {
        model.addAttribute("kitchen", kitchen);
        model.addAttribute("menuItems", menuItemService.getAllMenuItems(kitchen.getId()));
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
        model.addAttribute("kitchenSettingsRequest", new KitchenSettingsRequest(
                kitchen.getName(), kitchen.getDescription(), kitchen.getDeliveryTime(),
                kitchen.getDeliveryFee(), kitchen.isOpen()));
        return "kitchen-settings";
    }

    @PostMapping("/kitchen-settings")
    public String updateSettings(
            @Valid @ModelAttribute("kitchenSettingsRequest") KitchenSettingsRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserData userData,
            Model model,
            RedirectAttributes redirectAttributes) {

        Kitchen kitchen = kitchenService.getKitchenByUserId(userData.getUserId());
        model.addAttribute("kitchen", kitchen);

        if (bindingResult.hasErrors()) {
            return "kitchen-settings";
        }

        if (kitchenService.existsKitchenName(request.getName().strip(), userData.getUserId())){
            bindingResult.rejectValue("name", "kitchen.name.duplicate", "Kitchen name already exists");
            return "kitchen-settings";
        }

        kitchenService.updateSettings(userData.getUserId(), request);
        redirectAttributes.addFlashAttribute("settingsMessage", "Kitchen settings saved.");
        return "redirect:/kitchen-settings";
    }
}
