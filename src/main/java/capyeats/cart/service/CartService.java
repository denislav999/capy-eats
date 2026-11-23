package capyeats.cart.service;


import capyeats.cart.model.Cart;
import capyeats.cart.model.CartItem;
import capyeats.menu.model.MenuItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;
import java.util.function.Function;

@Service
@SessionScope
public class CartService {
    private final Cart cart = new Cart();


    public synchronized boolean addItem(MenuItem menuItem, int quantity) {
        var items = cart.getItems();

        if (!items.isEmpty()) {
            UUID currentKitchen = items.get(0).getMenuItem().getKitchen().getId();
            if (!currentKitchen.equals(menuItem.getKitchen().getId())) {
                return false;
            }
        }


        for (CartItem existingItem : items) {
            if (existingItem.getMenuItem().getId().equals(menuItem.getId())) {
                int newQuantity = existingItem.getQuantity() + quantity;
                existingItem.setQuantity(newQuantity);
                existingItem.setMenuItem(menuItem);
                return true;
            }
        }
        items.add(new CartItem(menuItem, quantity));

        return true;
    }

    public synchronized void removeItem(UUID itemId) {
        cart.getItems().removeIf(item -> item.getMenuItem().getId().equals(itemId));
    }

    public synchronized Cart getCart() {
        Cart snapshot = new Cart();
        snapshot.setItems(cart.getItems().stream()
                .map(item -> new CartItem(item.getMenuItem(), item.getQuantity()))
                .toList());
        return snapshot;
    }

    public synchronized <T> T checkout(Function<Cart, T> placeOrder) {
        T order = placeOrder.apply(getCart());
        cart.getItems().clear();
        return order;
    }
}

