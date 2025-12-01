package capyeats.user.service;

import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.repository.UserRepository;
import capyeats.web.dto.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
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

    public User register(RegisterUserRequest registerUserRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(registerUserRequest.getUsername());

        if (optionalUser.isPresent()) {
            throw new RuntimeException(String.format("Username %s already exists", registerUserRequest.getUsername()));
        }

        User user = User.builder()
                .username(registerUserRequest.getUsername())
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .address(null)
                .role(UserRole.USER)
                .phoneNumber(null)
                .build();


        userRepository.saveAndFlush(user);
        return user;
    }


    public User login (String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return  user;
            }
        }
        return null;
    }

    public User getUserById(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

}
