package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
import app.bestseller.coffeestore.service.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * Created by Abe with ❤️.
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderControllerTest extends TestDataInitializer {
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

    User customer;
    Product blackCoffee;
    Product mocha;
    Product milk;
    Product chocolateSauce;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();

        customer = userRepository.save(getCustomer());
        blackCoffee = productRepository.save(getBlackCoffee());
        mocha = productRepository.save(getMocha());
        milk = productRepository.save(getMilk());
        chocolateSauce = productRepository.save(getChocolateSauce());
    }


    @Test
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
                        .filter(product -> product.getType().equals(Product.Type.DRINK))).toList();
        assertThat(drinks).hasSize(2);
        var toppings = orders.stream()
                .flatMap(order -> order.getProducts().stream()
                        .filter(product -> product.getType().equals(Product.Type.TOPPING))).toList();
        assertThat(toppings).hasSize(2);
    }


    private List<OrderDTO> getRequestOrderDtos() {
        OrderDTO firstOrderDto = new OrderDTO();

        ProductOrderDTO blackCoffeeDto = new ProductOrderDTO();
        blackCoffeeDto.setId(this.blackCoffee.getId());

        ProductOrderDTO milkDto = new ProductOrderDTO();
        milkDto.setId(this.milk.getId());
        firstOrderDto.setProducts(new ArrayList<>(List.of(blackCoffeeDto, milkDto)));
        //
        OrderDTO secondOrderDto = new OrderDTO();
        ProductOrderDTO mochaDto = new ProductOrderDTO();
        mochaDto.setId(this.mocha.getId());

        ProductOrderDTO chocolateSauceDto = new ProductOrderDTO();
        chocolateSauceDto.setId(this.chocolateSauce.getId());
        secondOrderDto.setProducts(new ArrayList<>(List.of(mochaDto, chocolateSauceDto)));

        return new ArrayList<>(new ArrayList<>(List.of(firstOrderDto, secondOrderDto)));
    }


}
