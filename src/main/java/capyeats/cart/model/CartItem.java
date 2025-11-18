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
    private BigDecimal totalPrice;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.totalPrice = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
