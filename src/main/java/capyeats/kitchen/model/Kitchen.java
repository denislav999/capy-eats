package capyeats.kitchen.model;

import capyeats.common.BaseEntity;
import capyeats.menu.model.MenuItem;
import capyeats.order.model.Order;
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
    @Column(nullable = false)
    private String address;
    //location :D
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Float rating;

    @Column
    private Integer reviewsCount;

    @Column
    private Integer minDeliveryTime;

    @Column
    private Integer maxDeliveryTime;

    @Column
    private Double deliveryFee;

    @OneToMany(mappedBy="kitchen")
    private List<MenuItem> menu;

    @OneToMany(mappedBy="kitchen")
    private List<Order> orders;
}