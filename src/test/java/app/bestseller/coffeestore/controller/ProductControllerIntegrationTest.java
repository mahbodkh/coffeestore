package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
import app.bestseller.coffeestore.service.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest extends TestDataInitializer {
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;


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
    @DisplayName("Test get product Api then expected return ProductResponseDTO")
    void testGetProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/products/" + blackCoffee.getId() + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(blackCoffee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(blackCoffee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(blackCoffee.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(blackCoffee.getType().name()));
    }

    @Test
    @Order(2)
    @DisplayName("Test get all products Api then expected return pageable ProductResponseDTOs")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/products/all/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "10")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(4))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(blackCoffee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value(blackCoffee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].price").value(blackCoffee.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].type").value(blackCoffee.getType().name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(mocha.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name").value(mocha.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].price").value(mocha.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].type").value(mocha.getType().name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].id").value(milk.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].name").value(milk.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].price").value(milk.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].type").value(milk.getType().name()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].id").value(chocolateSauce.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].name").value(chocolateSauce.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].price").value(chocolateSauce.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[3].type").value(chocolateSauce.getType().name()));
    }


    @Test
    @Order(3)
    @DisplayName("Test create product Api then expected response isCreated")
    void testCreateProduct() throws Exception {
        ProductDTO americano = new ProductDTO();
        americano.setType("DRINK");
        americano.setName("Americano");
        americano.setPrice(3.0);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/products/admin/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(americano))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    @Order(4)
    @DisplayName("Test update product Api then expected return isOk")
    void testUpdateProduct() throws Exception {

        ProductDTO blackCoffeeUpdate = new ProductDTO();
        blackCoffeeUpdate.setPrice(8.0);
        blackCoffeeUpdate.setName("Black Coffee Double");
        blackCoffeeUpdate.setType("DRINK");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/products/admin/" + blackCoffee.getId() + "/edit/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blackCoffeeUpdate))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Test delete product Api then expected return isOk")
    void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/products/admin/" + milk.getId() + "/delete/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
