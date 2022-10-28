package kitchenpos.ui;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupCreateResponse;
import kitchenpos.dto.mapper.TableGroupDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupDtoMapper tableGroupDtoMapper;
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupDtoMapper tableGroupDtoMapper,
                                    final TableGroupService tableGroupService) {
        this.tableGroupDtoMapper = tableGroupDtoMapper;
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupCreateResponse> create(
            @RequestBody final TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroupCreateResponse created = tableGroupDtoMapper.toTableGroupCreateResponse(
                tableGroupService.create(tableGroupCreateRequest.getOrderTables())
        );
        URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
