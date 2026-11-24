package capyeats.user.service;

import capyeats.security.UserData;
import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.repository.UserRepository;
import capyeats.web.dto.LoginRequest;
import capyeats.web.dto.ProfileUpdateRequest;
import capyeats.web.dto.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private static final String DEFAULT_PROFILE_PICTURE = "https://avatars.githubusercontent.com/u/237266975?v=4";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return this.userRepository.existsByEmailIgnoreCase(email);
    }

    public User register(RegisterUserRequest registerUserRequest, UserRole role) {
        Optional<User> optionalUser = userRepository.findByUsername(registerUserRequest.getUsername());

        if (optionalUser.isPresent()) {
            throw new RuntimeException(String.format("Username %s already exists", registerUserRequest.getUsername()));
        }

        User user = User.builder()
                .username(registerUserRequest.getUsername())
                .profilePicture(DEFAULT_PROFILE_PICTURE)
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .address(null)
                .role(role)
                .phoneNumber(null)
                .active(true)
                .build();

        userRepository.saveAndFlush(user);
        return user;
    }


    public Set<User> findAllByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }

    public User getUserById(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserData(user.getId(),username, user.getPassword(),user.getRole(), user.isActive());
    }

    public boolean isUsernameAvailable(String username, UUID userId) {
        return userRepository.findByUsername(username)
                .map(user -> user.getId().equals(userId))
                .orElse(true);
    }

    public boolean isEmailAvailable(String email, UUID userId) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> user.getId().equals(userId))
                .orElse(true);
    }

    public void updateProfile(UUID userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFullName(request.getFullName());
        userRepository.saveAndFlush(user);
    }
}
