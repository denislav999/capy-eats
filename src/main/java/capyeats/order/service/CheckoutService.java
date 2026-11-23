package capyeats.order.service;

import capyeats.address.model.Address;
import capyeats.address.repository.AddressRepository;
import capyeats.cart.model.Cart;
import capyeats.cart.model.CartItem;
import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.repository.KitchenRepository;
import capyeats.menu.model.MenuItem;
import capyeats.menu.service.MenuItemService;
import capyeats.order.model.Order;
import capyeats.order.model.OrderItem;
import capyeats.order.model.OrderStatus;
import capyeats.order.model.PaymentMethod;
import capyeats.order.model.PaymentStatus;
import capyeats.order.repository.OrderItemRepository;
import capyeats.order.repository.OrderRepository;
import capyeats.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final KitchenRepository kitchenRepository;
    private final MenuItemService menuItemService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CheckoutService(KitchenRepository kitchenRepository, MenuItemService menuItemService,
                           AddressRepository addressRepository, UserRepository userRepository,
                           OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.kitchenRepository = kitchenRepository;
        this.menuItemService = menuItemService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public Quote quote(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new CheckoutException("Your cart is empty.");
        }

        UUID kitchenId = cart.getItems().get(0).getMenuItem().getKitchen().getId();
        Kitchen kitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new CheckoutException("This kitchen is no longer available."));
        if (!kitchen.isOpen()) {
            throw new CheckoutException("This kitchen is paused and cannot accept orders right now.");
        }

        Map<UUID, MenuItem> availableItems = menuItemService.getAvailableMenuItems(kitchenId).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
        List<CartItem> items = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            MenuItem currentItem = availableItems.get(cartItem.getMenuItem().getId());
            if (currentItem == null) {
                throw new CheckoutException("Remove " + cartItem.getMenuItem().getName()
                        + " from your cart to continue. It is no longer available.");
            }
            if (cartItem.getQuantity() < 1) {
                throw new CheckoutException("Remove the item with an invalid quantity from your cart to continue.");
            }
            items.add(new CartItem(currentItem, cartItem.getQuantity()));
        }

        BigDecimal subtotal = items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deliveryFee = kitchen.getDeliveryFee() == null ? BigDecimal.ZERO : kitchen.getDeliveryFee();
        return new Quote(kitchen, List.copyOf(items), subtotal, deliveryFee, subtotal.add(deliveryFee));
    }

    @Transactional
    public Order placeOrder(UUID userId, UUID addressId, Cart cart, BigDecimal quotedTotal) {
        if (addressId == null) {
            throw new CheckoutException("Choose a delivery address.");
        }
        Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new CheckoutException("Choose one of your saved delivery addresses."));
        Quote quote = quote(cart);
        if (quotedTotal == null || quote.total().compareTo(quotedTotal) != 0) {
            throw new CheckoutException("Your total has changed. Review it before placing your order.");
        }

        Order order = orderRepository.save(Order.builder()
                .user(userRepository.getReferenceById(userId))
                .kitchen(quote.kitchen())
                .totalPrice(quote.total())
                .deliveryFee(quote.deliveryFee())
                .deliveryAddress(formatAddress(address))
                .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                .orderStatus(OrderStatus.CREATED)
                .paymentStatus(PaymentStatus.PENDING)
                .build());

        orderItemRepository.saveAll(quote.items().stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .name(item.getMenuItem().getName())
                        .unitPrice(item.getMenuItem().getPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList());
        return order;
    }

    private String formatAddress(Address address) {
        String street = address.getStreet() + " " + address.getStreetNumber();
        if (StringUtils.hasText(address.getApartment())) {
            street += ", apt. " + address.getApartment();
        }
        return street + ", " + address.getPostalCode() + " " + address.getCity();
    }

    public record Quote(Kitchen kitchen, List<CartItem> items, BigDecimal subtotal,
                        BigDecimal deliveryFee, BigDecimal total) {
    }

    public static class CheckoutException extends RuntimeException {
        public CheckoutException(String message) {
            super(message);
        }
    }
}
