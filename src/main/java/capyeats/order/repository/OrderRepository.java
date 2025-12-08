package capyeats.order.repository;

import capyeats.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id= :userId AND o.orderStatus = 'COMPLETED'")
    int countCompletedOrders(UUID userId);
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.user.id= :userId AND o.orderStatus = 'COMPLETED'")
    BigDecimal getTotalPrice(UUID userId);

}
