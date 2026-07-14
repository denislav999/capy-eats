package capyeats.order.service;

import capyeats.order.model.Order;
import capyeats.order.model.OrderStatus;
import capyeats.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
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

    
    public void cancelOldPendingOrders() {
        Instant cutoff = Instant.now().minus(Duration.ofHours(10));

        List<Order> oldPendingOrders =
                orderRepository.findAllByOrderStatus_Preparing(cutoff);

        if (oldPendingOrders.isEmpty()) {
            return;
        }

        for (Order order : oldPendingOrders) {
            order.setOrderStatus(OrderStatus.CANCELED);
        }

        orderRepository.saveAll(oldPendingOrders);

        System.out.println("Auto-cancelled " + oldPendingOrders.size() + " old pending orders.");
    }
}
