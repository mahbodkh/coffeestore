package app.bestseller.coffeestore.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOrderDTO {
    @NotNull
    private long id;
}
