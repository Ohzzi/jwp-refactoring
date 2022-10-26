package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격은_음수일_수_없다() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격_동등_비교가_가능하다() {
        Price price1 = new Price(BigDecimal.ONE);
        Price price2 = new Price(BigDecimal.ZERO);

        boolean actual = price1.equals(price2);

        assertThat(actual).isFalse();
    }
}
