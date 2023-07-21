package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.domain.Order;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.exception.BadRequestException;
import app.bestseller.coffeestore.service.dto.OrderDTO;
import app.bestseller.coffeestore.service.dto.OrderResponseDTO;
import app.bestseller.coffeestore.repository.OrderRepository;
import app.bestseller.coffeestore.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Created by Abe with ❤️.
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderMapper orderMapper;
    private final ToDoubleFunction<List<Product>> discountService;

    /**
     * Create an order for a user.
     *
     * @param userId           the customer id.
     * @param requestOrderDtos the customer orders.
     * @return OrderResponseDto.
     */
    @Transactional
    public OrderResponseDTO createOrder(Long userId, List<OrderDTO> requestOrderDtos) {
        log.info(" ==> try to register customer order.");
        var userEntity = userService.loadUser(userId);
        var orderEntity = registerOrder(userEntity, requestOrderDtos);

        // prepare response for customer
        var response = orderMapper.toDto(orderEntity, requestOrderDtos);
        log.info(" <== prepared response for customer: {} .", response);
        return response;
    }


    private Order registerOrder(User user, List<OrderDTO> requests) {
        log.debug(" ==> The order of userId: {}, has been started to persist and check products (drinks/topping): {} .", user.getId(), requests);

        // products verifier and collector
        final var productOrders = requests.stream().map(OrderDTO::getProducts)
                .flatMap(productDTOS -> productDTOS.stream().map(productOrderDto -> productService.getProductById(productOrderDto.getId()))).toList();

        if (productOrders.isEmpty()) {
            throw new BadRequestException("There is no any order to persist.");
        }

        // discount applier
        final var discount = discountService.applyAsDouble(productOrders);

        // Prepare into the Data Model.
        final var orderEntity = orderMapper.mergeToOrderEntity(user, discount, productOrders);
        log.info(" Prepare customer orders: {} ", orderEntity);

        // persistence into db
        final var save = orderRepository.save(orderEntity);
        log.info(" <== The order has been persisted: ({}).", save);
        return orderEntity;
    }
}
