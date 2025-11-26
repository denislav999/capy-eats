package capyeats.menu.model;

import capyeats.common.BaseEntity;
import capyeats.kitchen.model.Kitchen;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "manu_items")
public class MenuItem extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, name = "is_avaible")
    private boolean isAvailable = true;

    @ManyToOne
    private Kitchen kitchen;
}