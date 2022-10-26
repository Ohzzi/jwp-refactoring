package kitchenpos.ui.mapper;

import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreateRequest;

public interface MenuMapper {

    Menu toMenu(MenuCreateRequest menuCreateRequest);
}
