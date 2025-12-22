package capyeats.review.repository;

import capyeats.review.model.Review;
import capyeats.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Set<Review> getAllByUser(User user);
}
