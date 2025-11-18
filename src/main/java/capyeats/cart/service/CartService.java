package capyeats.cart.service;


import capyeats.cart.model.Cart;
import capyeats.cart.model.CartItem;
import capyeats.menu.model.MenuItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {
    private final Cart cart = new Cart();

    public void addItem(MenuItem menuItem, int quantity) {
        for (CartItem item : cart.getItems()) {
            if (item.getMenuItem().equals(menuItem)) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                return;
            }
        }
        cart.getItems().add(new CartItem(menuItem, quantity));
    }

    public void removeItem(MenuItem menuItem) {
        cart.getItems().removeIf(item -> item.getMenuItem().equals(menuItem));
    }

    public void updateItemQuantity(MenuItem menuItem, int quantity) {
        for (CartItem item : cart.getItems()) {
            if (item.getMenuItem().equals(menuItem)) {
                item.setQuantity(quantity);
                item.setTotalPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
                return;
            }
        }
    }

    public BigDecimal getTotalPrice() {
        return cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Cart getCart() {
        return cart;
    }

    public void clearCart() {
        cart.getItems().clear();
    }
}

