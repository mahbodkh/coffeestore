package app.bestseller.coffeestore.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private String type;
    private Date created;
}