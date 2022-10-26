package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴_상품을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuServiceTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>());
        MenuProduct menuProduct = 메뉴_상품을_생성한다(menu, productId, 1);
        menuProduct.setMenu(menu);

        Menu savedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price", "menuProducts")
                        .isEqualTo(savedMenu),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
                        .usingElementComparatorIgnoringFields("seq")
                        .containsOnly(menuProduct)
        );
    }

    @Test
    void 메뉴_가격이_0원_미만이면_예외를_반환한다() {
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_반환한다() {
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.ZERO, 0L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이_메뉴에_포함되어_있으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(2_000), menuGroupId, new ArrayList<>());
        MenuProduct menuProduct = 메뉴_상품을_생성한다(menu, 0L, 1);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_크면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(2_000), menuGroupId, new ArrayList<>());
        MenuProduct menuProduct = 메뉴_상품을_생성한다(menu, productId, 1);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Menu menu1 = menuService.create(
                메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, List.of()));
        Menu menu2 = menuService.create(
                메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, List.of()));

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("price", "menuProducts")
                .containsExactly(menu1, menu2);
    }
}
