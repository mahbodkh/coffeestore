package app.bestseller.coffeestore.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private List<ProductOrderDTO> products;
}

