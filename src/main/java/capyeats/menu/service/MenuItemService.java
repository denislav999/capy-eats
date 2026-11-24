package capyeats.menu.service;

import capyeats.kitchen.model.Kitchen;
import capyeats.menu.model.MenuItem;
import capyeats.menu.repository.MenuItemRepository;
import capyeats.order.repository.OrderItemRepository;
import capyeats.web.dto.MenuItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<MenuItem> getAllMenuItems(UUID kitchenId) {
        return this.menuItemRepository.findAllByKitchenId_OrderByCreatedAtAsc(kitchenId);
    }

    public List<MenuItem> getAvailableMenuItems(UUID kitchenId) {
        return menuItemRepository.findAllByKitchen_IdAndIsAvailableTrueOrderByCreatedAtAsc(kitchenId);
    }

    public void createMenuItem(Kitchen kitchen, MenuItemRequest request) {
        MenuItem item = MenuItem.builder()
                .name(request.getName().trim())
                .price(request.getPrice())
                .isAvailable(request.isAvailable())
                .kitchen(kitchen)
                .build();

        menuItemRepository.save(item);
    }

    public MenuItem getMenuItem(UUID kitchenId, UUID itemId) {
        return menuItemRepository.findByIdAndKitchen_Id(itemId, kitchenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    @Transactional
    public void updateMenuItem(UUID kitchenId, UUID itemId, MenuItemRequest request) {
        MenuItem item = getMenuItem(kitchenId, itemId);
        item.setName(request.getName().trim());
        item.setPrice(request.getPrice());
        item.setAvailable(request.isAvailable());
    }

    @Transactional
    public void deleteMenuItem(UUID kitchenId, UUID itemId) {
        MenuItem item = getMenuItem(kitchenId, itemId);
        if (orderItemRepository.existsByMenuItem_Id(itemId)) {
            return;
        }

        menuItemRepository.delete(item);
    }

    public int getActiveDishesCount(UUID kitchenId) {
        return menuItemRepository.countAllByIsAvailableTrueAndKitchen_Id(kitchenId);
    }
}
