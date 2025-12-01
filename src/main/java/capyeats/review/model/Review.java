package capyeats.review.model;

import capyeats.common.BaseEntity;
import capyeats.kitchen.model.Kitchen;
import capyeats.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity(name = "reviews")
public class Review extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Kitchen restaurant;
    @Column(nullable = false)
    private Integer score;
    @Column
    private String comment;
}
