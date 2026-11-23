package capyeats.kitchen.service;

import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.repository.KitchenRepository;
import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.service.UserService;
import capyeats.web.dto.KitchenOwnerRequest;
import capyeats.web.dto.RegisterUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Service
public class KitchenService {
    private final UserService userService;
    private final KitchenRepository kitchenRepository;

    public KitchenService(UserService userService, KitchenRepository kitchenRepository) {
        this.userService = userService;
        this.kitchenRepository = kitchenRepository;
    }

    @Transactional
    public void registerKitchenOwner(KitchenOwnerRequest kitchenOwnerRequest) {

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername(kitchenOwnerRequest.getUsername());
        registerUserRequest.setPassword(kitchenOwnerRequest.getPassword());
        registerUserRequest.setEmail(kitchenOwnerRequest.getEmail());

        User owner = userService.register(registerUserRequest, UserRole.KITCHEN_OWNER);

        Kitchen kitchen = Kitchen.builder()
                .name(kitchenOwnerRequest.getKitchenName())
                .owner(owner)
                .build();

        kitchenRepository.saveAndFlush(kitchen);
    }

    public boolean existsKitchen(String kitchenName) {
        return kitchenRepository.findByName(kitchenName).isPresent();
    }

    public Kitchen getKitchenByUserId(UUID userId) {
        return kitchenRepository.findByOwner_Id(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen not found"));
    }
}
