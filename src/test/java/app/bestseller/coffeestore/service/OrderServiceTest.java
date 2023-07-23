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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
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

        // prepare a customer ( user ) in db
        customer = userRepository.save(getCustomer());

        // prepare some products ( drink / topping ) in db
        blackCoffee = productRepository.save(getBlackCoffee());
        mocha = productRepository.save(getMocha());
        milk = productRepository.save(getMilk());
        chocolateSauce = productRepository.save(getChocolateSauce());
    }

    @Test
    @Order(1)
    @DisplayName("testCreateOrder_whenValidData_thenExpectedPersistAndReturnOrderResponseDTO")
    void testCreateOrder_whenValidInput_thenReturnOrderResponseDTO() throws Exception {
        // given
        var orderRequest = getRequestOrderDtos();
        var order = orderService.createOrder(customer.getId(), orderRequest);
        //
        var saved = orderRepository.findByUserId(customer.getId()).orElseThrow();
        // assert
        assertThat(saved.getUser().getId()).isEqualTo(order.getUser());
        assertThat(saved.getTotal()).isEqualTo(order.getTotal());
        assertThat(saved.getDiscount()).isEqualTo(order.getDiscount());
        assertThat(saved.getFinalPrice()).isEqualTo(order.getFinalPrice());
        assertThat(saved.getProducts().get(0).getId()).isEqualTo(this.blackCoffee.getId());
        assertThat(saved.getProducts().get(1).getId()).isEqualTo(this.milk.getId());
        assertThat(saved.getProducts().get(2).getId()).isEqualTo(this.mocha.getId());
        assertThat(saved.getProducts().get(3).getId()).isEqualTo(this.chocolateSauce.getId());
    }


    @Test
    @Order(2)
    @DisplayName("testCreateOrder_whenSingleProduct_thenExpectedPersistOrderAndReturnOrderResponseDto")
    void testCreateOrder_whenSingleProduct_thenReturnResponseDto() {
        // given | prepare order dto
        OrderDTO orderDTO = new OrderDTO();
        ProductOrderDTO productOrderDTO = new ProductOrderDTO(this.blackCoffee.getId());
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
    @Order(3)
    @DisplayName("testCreateOrder_whenMultipleProductWithSameProductType_thenExpectedReturnValidAmount")
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
    @Order(4)
    @DisplayName("testCreateOrder_whenMultipleProductsWithDifferentProductTypes_thenExpectedReturnValidAmount")
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
        final var orderResponseDTO = orderService.createOrder(customer.getId(), Collections.singletonList(orderDTO));
        // then
        final var expectedTotal = productOrderDTOS.stream()
                .mapToDouble(dto -> productRepository.findById(dto.getId()).orElseThrow().getPrice())
                .sum();
        assertThat(orderResponseDTO.getTotal()).isEqualTo(expectedTotal);
    }


    @Test
    @Order(5)
    @DisplayName("testCreateOrder_whenValidCustomerWithNoProduct_thenExpectedThrowBadRequestException")
    void testCreateOrder_whenNoProducts_thenThrowBadRequestException() {
        // given
        var orderDTO = new OrderDTO();
        orderDTO.setProducts(Collections.emptyList());

        final var customerId = customer.getId();
        final var emptyOrderDtos = Collections.singletonList(orderDTO);
        // then
        assertThrows(
                BadRequestException.class,
                () -> orderService.createOrder(customerId, emptyOrderDtos),
                "There is no any order to persist."
        );
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
