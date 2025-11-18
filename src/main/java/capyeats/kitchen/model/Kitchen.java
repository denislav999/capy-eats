package capyeats.kitchen.model;

import capyeats.menu.model.MenuItem;
import capyeats.order.model.Order;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "kitchens")
public class Kitchen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy="kitchen")
    private List<MenuItem> menu;

    @OneToMany(mappedBy="kitchen")
    private List<Order> orders;
}