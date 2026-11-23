package capyeats.kitchen.service;

import capyeats.kitchen.model.Kitchen;
import capyeats.kitchen.repository.KitchenRepository;
import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.service.UserService;
import capyeats.web.dto.KitchenOwnerRequest;
import capyeats.web.dto.KitchenSettingsRequest;
import capyeats.web.dto.RegisterUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class KitchenService {
    private static final String KITCHEN_OWNER_ROLE = "KITCHEN_OWNER";

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

        User owner = userService.register(registerUserRequest, UserRole.valueOf(KITCHEN_OWNER_ROLE));

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

    public Kitchen getKitchenById(UUID kitchenId) {
        return kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen not found"));
    }

    public boolean existsKitchenName(String kitchenName, UUID ownerId) {
        Optional<Kitchen> kitchen = kitchenRepository.findByName(kitchenName);
        if (kitchen.isPresent()){
            return !kitchen.get().getOwner().getId().equals(ownerId);
        }
        return false;
    }

    @Transactional
    public void updateSettings(UUID ownerId, KitchenSettingsRequest request) {
        Kitchen kitchen = getKitchenByUserId(ownerId);
        String description = request.getDescription();
        kitchen.setName(request.getName().strip());
        kitchen.setDescription(StringUtils.hasText(description) ? description.strip() : null);
        kitchen.setDeliveryTime(request.getDeliveryTime());
        kitchen.setDeliveryFee(request.getDeliveryFee());
        kitchen.setOpen(request.getOpen());
    }

    public List<Kitchen> getNewestKitchens() {
        return kitchenRepository.findTop6ByOrderByCreatedAtDesc();
    }
}
