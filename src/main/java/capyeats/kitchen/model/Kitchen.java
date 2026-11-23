package capyeats.kitchen.model;

import capyeats.common.BaseEntity;
import capyeats.menu.model.MenuItem;
import capyeats.order.model.Order;
import capyeats.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "kitchens")
public class Kitchen extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Float rating;

    @Column
    private Integer reviewsCount;

    @Column
    private Integer deliveryTime;

    @Column(nullable = false)
    private boolean isOpen;

    @Column
    private Double deliveryFee;

    @OneToMany(mappedBy="kitchen")
    private List<MenuItem> menu;

    @OneToMany(mappedBy="kitchen")
    private List<Order> orders;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private User owner;
}