package app.bestseller.coffeestore.service;


import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.exception.UserNotFoundException;
import app.bestseller.coffeestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User loadUser(Long userId) {
        log.info(" ==> Fetching user with ID: {}", userId);
        return Optional.of(userRepository.findById(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> {
                    log.warn("userId with ID {} not found.", userId);
                    return new UserNotFoundException("User id: (" + userId + ") not found.");
                });
    }
}
