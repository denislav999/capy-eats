package capyeats.web;

import capyeats.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final OrderService orderService;

    @Autowired
    public ProfileController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public ModelAndView profile(HttpSession session) {
        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("activePage", "profile");
        mv.addObject("totalOrders", orderService.getAllOrders((UUID)session.getAttribute("userId")));
        mv.addObject("totalSpend", orderService.getTotalPrice((UUID)session.getAttribute("userId")));
        return mv;
    }

    @GetMapping("/addresses")
    public ModelAndView addresses() {
        ModelAndView mv = new ModelAndView("addresses");
        mv.addObject("activePage", "addresses");
        return mv;
    }

    @GetMapping("/orders")
    public ModelAndView orders() {
        ModelAndView mv = new ModelAndView("orders");
        mv.addObject("activePage", "orders");
        return mv;
    }
    @GetMapping("/payments")
    public ModelAndView payments() {
        ModelAndView mv = new ModelAndView("payment");
        mv.addObject("activePage", "payments");
        return mv;
    }
    @GetMapping("/settings")
    public ModelAndView settings() {
        ModelAndView mv = new ModelAndView("settings");
        mv.addObject("activePage", "settings");
        return mv;
    }
}
