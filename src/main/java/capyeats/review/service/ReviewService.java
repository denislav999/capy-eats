package capyeats.review.service;

import capyeats.review.model.Review;
import capyeats.review.repository.ReviewRepository;
import capyeats.user.model.User;
import capyeats.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ReviewService {

    private final UserService userService;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(UserService userService, ReviewRepository reviewRepository) {
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    @CacheEvict(value = "reviewsByUser", key = "#userId")
    public void addReview(UUID userId){
        System.out.println("Write review");
    }

    @Cacheable(value = "reviewsByUser", key = "#userId")
    public Set<Review> getReviewsByUser(UUID userId) {
        User user = userService.getUserById(userId);
        return reviewRepository.getAllByUser(user);
    }
}
