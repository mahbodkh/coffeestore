package app.bestseller.coffeestore.controller.validator;


import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginationParams {
    @Min(value = 0, message = "page value must greater than or equal 0")
    Integer page = 0;
    @Min(value = 1, message = "size value must greater than 1")
    Integer size = 10;
}
