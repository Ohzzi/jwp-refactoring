package kitchenpos.ui.mapper;

import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", expression = "java(new Price(productCreateRequest.getPrice()))")
    Product toProduct(ProductCreateRequest productCreateRequest);
}
