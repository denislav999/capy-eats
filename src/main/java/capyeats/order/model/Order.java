package capyeats.order.model;

import capyeats.common.BaseEntity;
import capyeats.kitchen.model.Kitchen;
import capyeats.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {

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
}