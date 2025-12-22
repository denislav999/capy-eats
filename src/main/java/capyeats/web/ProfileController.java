package capyeats.web;

import capyeats.order.service.OrderService;
import capyeats.review.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final OrderService orderService;
    private final ReviewService reviewService;

    @Autowired
    public ProfileController(OrderService orderService, ReviewService reviewService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String profile(HttpSession session, Model model) {
        model.addAttribute("activePage", "profile");
        model.addAttribute("totalReviews", reviewService.getReviewsByUser((UUID)session.getAttribute("userId")).size());
        model.addAttribute("totalOrders", orderService.getAllOrders((UUID)session.getAttribute("userId")));
        model.addAttribute("totalSpend", orderService.getTotalPrice((UUID)session.getAttribute("userId")));
        return "profile";
    }

    @GetMapping("/addresses")
    public String addresses(Model model) {
        model.addAttribute("activePage", "addresses");
        return "addresses";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("activePage", "orders");
        return "orders";
    }
    @GetMapping("/payments")
    public String payments(Model model) {
        model.addAttribute("activePage", "payments");
        return "payment";
    }
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        return "settings";
    }
}
