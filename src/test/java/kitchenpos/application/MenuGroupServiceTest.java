package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메뉴 그룹");

        MenuGroupCreateResponse menuGroupCreateResponse = menuGroupService.create(menuGroupCreateRequest);

        assertAll(
                () -> assertThat(menuGroupCreateResponse.getId()).isNotNull(),
                () -> assertThat(menuGroupCreateResponse.getName()).isEqualTo(menuGroupCreateRequest.getName())
        );
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        Long menuGroup1Id = menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹1"))
                .getId();
        Long menuGroup2Id = menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹2"))
                .getId();

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(menuGroup1Id, menuGroup2Id);
    }
}
