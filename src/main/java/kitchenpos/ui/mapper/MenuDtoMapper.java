package kitchenpos.ui.mapper;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuResponse;

public interface MenuDtoMapper {

    MenuCreateResponse toMenuCreateResponse(Menu menu);

    List<MenuResponse> toMenuResponses(List<Menu> menus);
}
