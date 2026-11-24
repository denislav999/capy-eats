package capyeats.order.service;

import capyeats.order.model.Order;
import capyeats.order.model.OrderStatus;
import capyeats.order.model.PaymentMethod;
import capyeats.order.model.PaymentStatus;
import capyeats.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
        return this.orderRepository.countDeliveredOrders(userId);
    }

    public BigDecimal getTotalPrice(UUID userId) {
        return this.orderRepository.getTotalPrice(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return this.orderRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getOrdersByKitchenId(UUID kitchenId){
        return this.orderRepository.findAllByKitchen_IdOrderByCreatedAtDesc(kitchenId);
    }

    public List<Order> getLast3OrdersByKitchenId(UUID kitchenId) {
        return this.orderRepository.findTop3ByKitchen_IdAndOrderStatusInOrderByCreatedAtDesc(kitchenId,List.of(OrderStatus.PREPARING,OrderStatus.CREATED));
    }

    @Transactional
    public void changeOrderStatus(UUID ownerId, UUID orderId, OrderStatus status) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getKitchen().getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this kitchen");
        }
        boolean allowed = switch (order.getOrderStatus()) {
            case CREATED -> status == OrderStatus.PREPARING || status == OrderStatus.CANCELED;
            case PREPARING -> status == OrderStatus.DELIVERED;
            default -> false;
        };
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That status change is no longer available");
        }

        order.setOrderStatus(status);
        if (status == OrderStatus.DELIVERED && order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }
    }

    public int getPreparingOrdersCountByKitchenId(UUID kitchenId) {
        return orderRepository.countByKitchen_IdAndOrderStatus(kitchenId, OrderStatus.PREPARING);
    }

    public int getNewOrdersCountByKitchenId(UUID kitchenId) {
        return orderRepository.countByKitchen_IdAndOrderStatus(kitchenId, OrderStatus.CREATED);
    }
}
