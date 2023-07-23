package app.bestseller.coffeestore.service.mapper;

import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.service.dto.ProductResponseDTO;
import app.bestseller.coffeestore.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "changed", ignore = true)
    Product toEntity(ProductDTO dto);

    ProductResponseDTO toDto(Product entity);

//    List<ProductResponseDTO> toDto(List<Product> entity);

    default Page<ProductResponseDTO> toPage(Page<Product> entities) {
        final var productResponseDtos = entities.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(productResponseDtos, entities.getPageable(), entities.getTotalElements());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "changed", ignore = true)
    Product copyProductDtoToEntity(ProductDTO dto, @MappingTarget Product product);
}
