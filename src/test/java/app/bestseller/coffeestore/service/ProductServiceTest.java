package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Order;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.exception.ProductNotFoundException;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.service.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Abe with ❤️.
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest extends TestDataInitializer {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;


    private Product blackCoffee;


    @BeforeEach
    public void setup() throws Exception {
        super.setUp();

        blackCoffee = getBlackCoffee();
    }

    @Test
    void testCreateProduct() throws Exception {
        // given
        var productDto = buildDtoDrinkBlackCoffee();
        // when
        var createdProduct = productService.createProduct(productDto);
        // then
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
        var productById = productRepository.findById(createdProduct.getId()).orElseThrow();
        assertThat(createdProduct.getId()).isEqualTo(productById.getId());
        assertThat(createdProduct.getName()).isEqualTo(productById.getName());
        assertThat(createdProduct.getType()).isEqualTo(Product.Type.DRINK);
        assertThat(createdProduct.getPrice()).isEqualTo(productById.getPrice());
    }

    @Test
    void testGetProductById() {
        // given
        var product = productRepository.save(getBlackCoffee());
        // when
        var foundProduct = productService.getProductById(product.getId());
        // then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        assertThat(foundProduct.getType()).isEqualTo(Product.Type.DRINK);
        assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    void testGetProductById_whenIsNotExistProduct_thenReturnException() {
        // given
        Long nonExistentProductId = 1L;
        // when and then
        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(nonExistentProductId),
                String.format("The product %s not found.", nonExistentProductId));
    }

    @Test
    void testUpdateProduct() {
        // given
        var product = productRepository.save(getBlackCoffee());
        // prepare the dto for update
        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("Updated Product");
        updatedProductDTO.setPrice(15.0);
        updatedProductDTO.setType("TOPPING");

        // when
        var updatedProduct = productService.updateProduct(product.getId(), updatedProductDTO);

        // then
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getId()).isEqualTo(product.getId());
        assertThat(updatedProduct.get().getName()).isEqualTo("Updated Product");
        assertThat(updatedProduct.get().getPrice()).isEqualTo(15.0);
        assertThat(updatedProduct.get().getType()).isEqualTo(Product.Type.TOPPING);
    }

    @Test
    void testDeleteProduct() {
        // given
        var product = productRepository.save(getBlackCoffee());
        // when
        productService.deleteProduct(product.getId());
        // then
        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void testGetMostUsedTopping() {
        // given
        prepareOrders();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Product> resultPage = productService.getMostUsedTopping(pageable);

        // then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(4);
        assertThat(resultPage.getContent().get(0).getName()).isEqualTo("Milk");
        assertThat(resultPage.getContent().get(1).getName()).isEqualTo("Chocolate Sauce");
        assertThat(resultPage.getContent().get(2).getName()).isEqualTo("Hazelnut Syrup");
        assertThat(resultPage.getContent().get(3).getName()).isEqualTo("Lemon");
    }

    // ==============================================
    //                PREPARE DTOs
    // ==============================================

    ProductDTO buildDtoDrinkBlackCoffee() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(blackCoffee.getName());
        productDTO.setPrice(blackCoffee.getPrice());
        productDTO.setType("DRINK");
        return productDTO;
    }

    /**
     * milk = 5 | chocolate_sauce = 4 | hazelnut_syrup = 3 | lemon = 2
     */
    private void prepareOrders() {
        var milk = productRepository.save(getMilk());
        var chocolateSauce = productRepository.save(getChocolateSauce());
        var hazelnutSyrup = productRepository.save(getHazelnutSyrup());
        var lemon = productRepository.save(getLemon());

        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce, hazelnutSyrup))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, hazelnutSyrup, lemon))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce, hazelnutSyrup, lemon))).build());
        orderRepository.save(Order.builder().products(new ArrayList<>(List.of(milk, chocolateSauce))).build());
    }


}
