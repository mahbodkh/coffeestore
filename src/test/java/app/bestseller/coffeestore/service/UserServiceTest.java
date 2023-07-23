package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.exception.UserNotFoundException;
import app.bestseller.coffeestore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest extends TestDataInitializer {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    User customer;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }


    @Test
    @Order(1)
    @DisplayName("test get user when user is persisted then expected return valid data")
    void testGetUser_whenValidUserId_thenReturn() throws Exception {
        // given
        customer = userRepository.save(getCustomer());
        // when
        var result = userService.loadUser(customer.getId());
        // then
        assertThat(result.getId()).isEqualTo(customer.getId());
    }

    @Test
    @Order(2)
    @DisplayName("test get user when is not persisted then expected throw UserNotFoundException")
    void testGetUser_whenInvalidData_expectedThrowException() throws Exception {
        // given
        final var customer = 1L;
        // then
        assertThrows(UserNotFoundException.class,
                () -> userService.loadUser(customer),
                "User id: 1 not found."
        );
    }


}
