package app.bestseller.coffeestore.service.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long user;
    private Double total;
    private Double discount;
    private Double finalPrice;
    private List<OrderDTO> orderRequests;
    private Date orderTime;
    private String trackingNumber;
}