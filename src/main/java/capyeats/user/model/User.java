package capyeats.user.model;

import capyeats.common.BaseEntity;
import capyeats.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;
    @Column(name = "profile_picture", nullable = false)
    private String profilePicture;
    @Column
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(nullable = false)
    private boolean active;

    //location :D
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    @OrderBy("createdAt")
    private Set<Order> orders = new HashSet<>();
}