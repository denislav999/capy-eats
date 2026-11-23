package capyeats.cart.model;

import capyeats.menu.model.MenuItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItem {
    private MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
