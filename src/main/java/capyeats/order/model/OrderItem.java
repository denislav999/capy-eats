package capyeats.order.model;

import capyeats.common.BaseEntity;
import capyeats.menu.model.MenuItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne
    private Order order;

    @ManyToOne
    private MenuItem menuItem;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;
}