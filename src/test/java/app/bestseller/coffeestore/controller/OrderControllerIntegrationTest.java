package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.ProductType;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
import app.bestseller.coffeestore.service.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderControllerIntegrationTest extends TestDataInitializer {
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ToDoubleFunction<List<Product>> discountService;

    private User customer;
    private Product blackCoffee;
    private Product mocha;
    private Product milk;
    private Product chocolateSauce;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();

        // prepare a customer ( user ) in db
        this.customer = userRepository.save(getCustomer());
        // prepare some products ( drink / topping ) in db
        this.blackCoffee = productRepository.save(getBlackCoffee());
        this.mocha = productRepository.save(getMocha());
        this.milk = productRepository.save(getMilk());
        this.chocolateSauce = productRepository.save(getChocolateSauce());
    }


    @Test
    @Order(1)
    @DisplayName("Test order register Api then expected return isCreated")
    void testOrderCreationWithValidInput() throws Exception {
        var orderRequest = getRequestOrderDtos();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/orders/finalize/" + customer.getId() + "/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Verify that the order was created
        // blackCoffee, mocha, milk, chocolateSauce
        var orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);
        var drinks = orders.stream()
                .flatMap(order -> order.getProducts().stream()
                        .filter(product -> product.getType().equals(ProductType.DRINK))).toList();
        assertThat(drinks).hasSize(2);
        var toppings = orders.stream()
                .flatMap(order -> order.getProducts().stream()
                        .filter(product -> product.getType().equals(ProductType.TOPPING))).toList();
        assertThat(toppings).hasSize(2);
    }


    /**
     * prepare an OrderDto with a couple of drinks / toppings
     *
     * @return a list of OrderDTO.
     */
    private List<OrderDTO> getRequestOrderDtos() {
        OrderDTO firstOrderDto = new OrderDTO();

        ProductOrderDTO blackCoffeeDto = new ProductOrderDTO(this.blackCoffee.getId());
        ProductOrderDTO milkDto = new ProductOrderDTO(this.milk.getId());

        firstOrderDto.setProducts(new ArrayList<>(List.of(blackCoffeeDto, milkDto)));
        //
        OrderDTO secondOrderDto = new OrderDTO();

        ProductOrderDTO mochaDto = new ProductOrderDTO(this.mocha.getId());
        ProductOrderDTO chocolateSauceDto = new ProductOrderDTO(this.chocolateSauce.getId());

        secondOrderDto.setProducts(new ArrayList<>(List.of(mochaDto, chocolateSauceDto)));

        return new ArrayList<>(List.of(firstOrderDto, secondOrderDto));
    }


}
