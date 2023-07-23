package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.ProductType;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportControllerIntegrationTest extends TestDataInitializer {
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;


    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;


    private Product milk;
    private Product chocolateSauce;
    private Product hazelnutSyrup;
    private Product lemon;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();

        milk = productRepository.save(getMilk());
        chocolateSauce = productRepository.save(getChocolateSauce());
        hazelnutSyrup = productRepository.save(getHazelnutSyrup());
        lemon = productRepository.save(getLemon());
    }


    @Test
    @Order(1)
    @DisplayName("Test most used Topping Api then return success.")
    void testGetMostUsedTopping() throws Exception {
        prepareOrders();
        //
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/reports/admin/topping/most-used/")
                        .param("size", "10")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(4))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(milk.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value(milk.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].type").value(ProductType.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(chocolateSauce.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name").value(chocolateSauce.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].type").value(ProductType.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].id").value(hazelnutSyrup.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].name").value(hazelnutSyrup.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].type").value(ProductType.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].id").value(lemon.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].name").value(lemon.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].type").value(ProductType.TOPPING.name()));
    }


    /**
     * milk = 5 | chocolate_sauce = 4 | hazelnut_syrup = 3 | lemon = 2
     */
    private void prepareOrders() {
        orderRepository.save(app.bestseller.coffeestore.domain.Order.builder().products(new ArrayList<>(List.of(this.milk, this.chocolateSauce))).build());
        orderRepository.save(app.bestseller.coffeestore.domain.Order.builder().products(new ArrayList<>(List.of(this.milk, this.chocolateSauce, this.hazelnutSyrup))).build());
        orderRepository.save(app.bestseller.coffeestore.domain.Order.builder().products(new ArrayList<>(List.of(this.milk, this.hazelnutSyrup, this.lemon))).build());
        orderRepository.save(app.bestseller.coffeestore.domain.Order.builder().products(new ArrayList<>(List.of(this.milk, this.chocolateSauce, this.hazelnutSyrup, this.lemon))).build());
        orderRepository.save(app.bestseller.coffeestore.domain.Order.builder().products(new ArrayList<>(List.of(this.milk, this.chocolateSauce))).build());
    }

}
