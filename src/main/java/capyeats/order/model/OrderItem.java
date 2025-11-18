package capyeats.order.model;

import capyeats.menu.model.MenuItem;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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