package capyeats.order.service;

import capyeats.order.repository.OrderRepository;
import capyeats.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public int getAllOrders(UUID userId) {
        return this.orderRepository.countCompletedOrders(userId);
    }
    public BigDecimal getTotalPrice(UUID userId) {
        return this.orderRepository.getTotalPrice(userId);
    }
}
