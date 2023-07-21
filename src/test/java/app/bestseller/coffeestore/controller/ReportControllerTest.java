package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Order;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * Created by Abe with ❤️.
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportControllerTest extends TestDataInitializer {
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;


    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;


    Product milk;
    Product chocolateSauce;
    Product hazelnutSyrup;
    Product lemon;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }


    @Test
    void testMostUsedTopping() throws Exception {
        prepareOrders();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/reports/admin/topping/most-used/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(4))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(milk.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value(milk.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].type").value(Product.Type.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(chocolateSauce.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name").value(chocolateSauce.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].type").value(Product.Type.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].id").value(hazelnutSyrup.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].name").value(hazelnutSyrup.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].type").value(Product.Type.TOPPING.name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].id").value(lemon.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].name").value(lemon.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].type").value(Product.Type.TOPPING.name()));
    }


    /**
     * milk = 5 | chocolate_sauce = 4 | hazelnut_syrup = 3 | lemon = 2
     */
    private void prepareOrders() {
        milk = productRepository.save(getMilk());
        chocolateSauce = productRepository.save(getChocolateSauce());
        hazelnutSyrup = productRepository.save(getHazelnutSyrup());
        lemon = productRepository.save(getLemon());

        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce, hazelnutSyrup))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, hazelnutSyrup, lemon))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce, hazelnutSyrup, lemon))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce))).build());
    }

}
