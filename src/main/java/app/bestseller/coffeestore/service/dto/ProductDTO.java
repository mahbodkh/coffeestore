package app.bestseller.coffeestore.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Size(min = 5, max = 50, message = "name must be lower that 50 character.")
    @NotBlank
    private String name;
    @Digits(integer = 18, fraction = 18)
    @Positive
    private Double price;
    @Size(min = 5, max = 50, message = "type must be lower that 50 character. it should be 'DRINK' or 'TOPPING'. ")
    @NotBlank
    private String type;
}