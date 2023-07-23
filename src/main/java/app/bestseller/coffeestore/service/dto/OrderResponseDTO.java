package app.bestseller.coffeestore.service.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long user;
    private Double total;
    private Double discount;
    private Double finalPrice;
    private List<OrderDTO> orderRequests;
    private Instant orderTime;
    private String trackingNumber;
}