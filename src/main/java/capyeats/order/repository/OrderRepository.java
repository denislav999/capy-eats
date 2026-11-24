package capyeats.order.repository;

import capyeats.order.model.Order;
import capyeats.order.model.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findOrderById(UUID id);

    @Query("""
            SELECT COUNT(o) FROM Order o
            WHERE o.user.id = :userId
              AND o.orderStatus = capyeats.order.model.OrderStatus.DELIVERED
            """)
    int countDeliveredOrders(UUID userId);

    @Query("""
            SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o
            WHERE o.user.id = :userId
              AND o.paymentStatus = capyeats.order.model.PaymentStatus.PAID
              AND o.paymentMethod = capyeats.order.model.PaymentMethod.CASH_ON_DELIVERY
              AND o.orderStatus = capyeats.order.model.OrderStatus.DELIVERED
            """)
    BigDecimal getTotalPrice(UUID userId);

    List<Order> findAllByUser_IdOrderByCreatedAtDesc(UUID userId);

    List<Order> findTop3ByKitchen_IdAndOrderStatusInOrderByCreatedAtDesc(UUID kitchenId, Collection<OrderStatus> orderStatuses);

    List<Order> findAllByKitchen_IdOrderByCreatedAtDesc(UUID kitchenId);

    int countByKitchen_IdAndOrderStatus(UUID kitchenId, OrderStatus status);
}
