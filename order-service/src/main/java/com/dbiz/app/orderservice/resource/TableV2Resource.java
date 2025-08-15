package com.dbiz.app.orderservice.resource;

import com.dbiz.app.orderservice.service.TableService;
import com.dbiz.app.orderservice.service.TableV2Service;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewTable;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/tables")
@Slf4j
@RequiredArgsConstructor
public class TableV2Resource {
    private final TableV2Service entityService;

    @GetMapping("/findAll")
    @JsonView(JsonViewTable.viewFindAll.class)
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final TableQueryRequest entityQueryRequest) {
        log.info("*** Table, resource; fetch Table all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    @JsonView(JsonViewTable.viewSaveAndUpdate.class)
    public ResponseEntity<GlobalReponse> save(@RequestBody TableDto entityDto) {
        log.info("*** Table, resource; save Table *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer id) {
        log.info("*** Table, resource; delete Table *");
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/findAllTableAndReservationByDate")
    @JsonView(JsonViewTable.viewJsonTableAndReservation.class)
    public ResponseEntity<GlobalReponsePagination> findAllTableAndReservationByDate(@ModelAttribute final TableQueryRequest entityQueryRequest) {
        log.info("*** Table, resource; fetch Table and Reservation by date *");
        return ResponseEntity.ok(this.entityService.findAllTableAndReservationByDate(entityQueryRequest));
    }
}
