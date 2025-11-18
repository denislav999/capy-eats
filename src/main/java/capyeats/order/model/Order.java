package capyeats.order.model;

import capyeats.kitchen.model.Kitchen;
import capyeats.user.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Kitchen kitchen;

    @OneToMany(mappedBy="order")
    private Set<OrderItem> orderItems;

    @Column(nullable = false)
    private BigDecimal totalPrice;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column(nullable = false, name = "creeated_ad")
    private LocalDateTime createdAt;
}