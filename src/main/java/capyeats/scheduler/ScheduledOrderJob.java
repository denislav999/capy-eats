package capyeats.scheduler;

import capyeats.order.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledOrderJob {

    private final OrderService orderService;

    public ScheduledOrderJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 60000)   // every minute
    public void cancelOldUnpaidPreperingJob() {
        orderService.cancelOldPendingOrders();
    }
}

