package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.NumberOfGuestsChangeRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.TableEmptyChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTableUpdateResponse;
import kitchenpos.dto.mapper.OrderTableMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class TableRestControllerTest extends RestControllerTest {

    @Autowired
    private OrderTableMapper orderTableMapper;

    @Test
    void 주문_테이블_생성에_성공한다() throws Exception {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, true);
        OrderTable expectedOrderTable = new OrderTable(1L, null, 0, true);
        when(tableService.create(orderTableMapper.toOrderTable(orderTableCreateRequest)))
                .thenReturn(expectedOrderTable);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderTableCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 주문_테이블_목록_조회에_성공한다() throws Exception {
        OrderTable expectedOrderTable = new OrderTable(1L, null, 0, true);
        when(tableService.list()).thenReturn(List.of(expectedOrderTable));

        MvcResult mvcResult = mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<OrderTableResponse> orderTableResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<OrderTableResponse>>() {
                });
        assertThat(orderTableResponses).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경에_성공한다() throws Exception {
        TableEmptyChangeRequest tableEmptyChangeRequest = new TableEmptyChangeRequest(true);
        OrderTable expectedOrderTable = new OrderTable(1L, null, 0, true);
        when(tableService.changeEmpty(1L, true)).thenReturn(expectedOrderTable);

        MvcResult mvcResult = mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tableEmptyChangeRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        OrderTableUpdateResponse orderTableUpdateResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), OrderTableUpdateResponse.class
        );
        assertThat(orderTableUpdateResponse.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_인원_수_변경에_성공한다() throws Exception {
        NumberOfGuestsChangeRequest numberOfGuestsChangeRequest = new NumberOfGuestsChangeRequest(1);
        OrderTable expectedOrderTable = new OrderTable(1L, null, 1, true);
        when(tableService.changeNumberOfGuests(1L, 1)).thenReturn(expectedOrderTable);

        MvcResult mvcResult = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(numberOfGuestsChangeRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        OrderTableUpdateResponse orderTableUpdateResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), OrderTableUpdateResponse.class
        );
        assertThat(orderTableUpdateResponse.getNumberOfGuests()).isOne();
    }
}
