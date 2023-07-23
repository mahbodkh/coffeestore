package app.bestseller.coffeestore.service.mapper;

import app.bestseller.coffeestore.domain.Order;
import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import app.bestseller.coffeestore.service.dto.OrderDTO;
import app.bestseller.coffeestore.service.dto.OrderResponseDTO;
import org.mapstruct.*;

import java.util.Date;
import java.util.List;



@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {

    @Mapping(target = "user", source = "order.user.id")
    @Mapping(target = "orderTime", source = "order.created")
    @Mapping(target = "orderRequests", source = "requests")
    OrderResponseDTO toDto(Order order, List<OrderDTO> requests);


    @Mapping(target = "products", source = "productOrders")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "total", expression = "java(calculateSumPrices(productOrders))")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "finalPrice", expression = "java(calculateFinalPrice(calculateSumPrices(productOrders), discount))")
    @Mapping(target = "trackingNumber", expression = "java(generateTrackingNumber())")
    Order mergeToOrderEntity(User user, Double discount, List<Product> productOrders);


    default Double calculateFinalPrice(Double total, Double discount) {
        return total - discount;
    }


    default Double calculateSumPrices(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice).sum();
    }

    /**
     * Generate a tracking number by timestamp!
     *
     * @return a String like this will be generated: TN1631383563087.
     */
    default String generateTrackingNumber() {
        return "TN" + new Date().getTime();
    }
}
