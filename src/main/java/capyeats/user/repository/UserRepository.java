package capyeats.user.repository;

import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Set<User> findAllByRole(UserRole role);

    boolean existsByEmailIgnoreCase(String email);


    Optional<User> findByEmailIgnoreCase(String email);
}
