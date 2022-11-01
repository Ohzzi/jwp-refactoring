package kitchenpos.domain.table;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 단체_지정이_되어_있으면_빈_테이블_여부를_변경할_수_없다() {
        OrderTable orderTable1 = 주문_테이블을_생성한다(null, 1, true);
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 1, true);
        단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> orderTable1.changeEmpty(true)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블의_인원_수를_변경할_수_없다() {
        OrderTable orderTable = 주문_테이블을_생성한다(null, 0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹_지정을_해제한다() {
        OrderTable orderTable1 = 주문_테이블을_생성한다(null, 1, true);
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 1, true);
        단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        orderTable1.ungroup();

        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable1.isEmpty()).isFalse()
        );
    }
}
