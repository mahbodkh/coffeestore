package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.exception.BadRequestException;
import app.bestseller.coffeestore.service.dto.OrderDTO;
import app.bestseller.coffeestore.service.dto.ProductOrderDTO;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by Abe with ❤️.
 */


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceTest extends TestDataInitializer {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DiscountService discountService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;


    private User customer;
    private Product blackCoffee;
    private Product mocha;
    private Product milk;
    private Product chocolateSauce;


    @BeforeEach
    public void setup() throws Exception {
        super.setUp();

        customer = userRepository.save(getCustomer());
        blackCoffee = productRepository.save(getBlackCoffee());
        mocha = productRepository.save(getMocha());
        milk = productRepository.save(getMilk());
        chocolateSauce = productRepository.save(getChocolateSauce());
    }

    @Test
    void testCreateOrder_whenValidInput_thenReturnOrderResponseDTO() throws Exception {
        // given
        var orderRequest = getRequestOrderDtos();
        var order = orderService.createOrder(customer.getId(), orderRequest);
        //
        var saved = orderRepository.findByUserId(customer.getId()).orElseThrow();
        // assert
        assertThat(saved.getUser().getId()).isEqualTo(order.getUser());
    }


    @Test
    void testCreateOrder_whenSingleProduct_thenReturnResponseDto() {
        // given
        OrderDTO orderDTO = new OrderDTO();
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setId(blackCoffee.getId());
        orderDTO.setProducts(Collections.singletonList(productOrderDTO));
        // when
        var orderResponseDTO = orderService.createOrder(customer.getId(), Collections.singletonList(orderDTO));
        // then
        assertThat(orderResponseDTO.getOrderRequests()).hasSize(1);
        assertThat(orderResponseDTO.getOrderRequests().get(0).getProducts().get(0).getId())
                .isEqualTo(blackCoffee.getId());
        assertThat(orderResponseDTO.getTotal()).isEqualTo(blackCoffee.getPrice());
        assertThat(orderResponseDTO.getUser()).isEqualTo(customer.getId());
        assertThat(orderResponseDTO.getFinalPrice()).isEqualTo(blackCoffee.getPrice());
        assertThat(orderResponseDTO.getDiscount()).isEqualTo(0.0);
        assertThat(orderResponseDTO.getTrackingNumber()).isNotNull();
    }


    @Test
    void testCreateOrder_whenMultipleProductsOfSameProduct_expectedValidTotal() {
        // given
        var orderDTO = new OrderDTO();
        var productOrderDTOS = Arrays.asList(
                new ProductOrderDTO(blackCoffee.getId()),
                new ProductOrderDTO(blackCoffee.getId()),
                new ProductOrderDTO(blackCoffee.getId())
        );
        orderDTO.setProducts(productOrderDTOS);
        // when
        var orderResponseDTO = orderService.createOrder(customer.getId(), Collections.singletonList(orderDTO));
        // then
        assertThat(orderResponseDTO.getTotal())
                .isEqualTo(blackCoffee.getPrice() * productOrderDTOS.size());
    }

    @Test
    void testCreateOrder_whenMultipleProductsOfDifferentTypes_expectedValidTotal() {
        // given
        var orderDTO = new OrderDTO();
        var productOrderDTOS = Arrays.asList(
                new ProductOrderDTO(blackCoffee.getId()),
                new ProductOrderDTO(milk.getId()),
                new ProductOrderDTO(mocha.getId()),
                new ProductOrderDTO(chocolateSauce.getId())
        );
        orderDTO.setProducts(productOrderDTOS);

        // when
        var orderResponseDTO = orderService.createOrder(customer.getId(), Collections.singletonList(orderDTO));
        // then
        final double expectedTotal = productOrderDTOS.stream()
                .mapToDouble(dto -> productRepository.findById(dto.getId()).orElseThrow().getPrice())
                .sum();
        assertThat(orderResponseDTO.getTotal()).isEqualTo(expectedTotal);
    }


    @Test
    void testCreateOrder_whenNoProducts_thenThrowBadRequestException() {
        // given
        var orderDTO = new OrderDTO();
        orderDTO.setProducts(Collections.emptyList());
        // then
        assertThrows(
                BadRequestException.class,
                () -> orderService.createOrder(customer.getId(), Collections.singletonList(orderDTO)),
                "There is no any order to persist."
        );
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
