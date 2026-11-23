package capyeats.menu.repository;

import capyeats.menu.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findAllByKitchenId_OrderByCreatedAtAsc(UUID kitchenId);

    Optional<MenuItem> findByIdAndKitchen_Id(UUID id, UUID kitchenId);
}
