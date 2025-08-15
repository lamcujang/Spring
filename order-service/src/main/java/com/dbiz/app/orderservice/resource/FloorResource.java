package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.FloorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/floors")
@Slf4j
@RequiredArgsConstructor
public class FloorResource {
    private final FloorService entityService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final FloorQueryRequest  entityQueryRequest) {
        log.info("*** Floor, resource; fetch Floor all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody FloorDto  entityDto)
    {
        log.info("*** Floor, resource; save Floor *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody FloorDto entityDto)
    {
        log.info("*** Floor, resource; update Floor *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** Floor, resource; delete by id Floor {} *",id);
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** Floor, resource; fetch Floor by id {}*",id);
        return ResponseEntity.ok(this.entityService.findById(id));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSaveAll(@RequestBody final List<FloorDto> entityDtos)
    {
        log.info("*** Floor, resource; int save all Floor *");
        GlobalReponse globalReponse = new GlobalReponse();
        try {
                globalReponse = this.entityService.intSaveAll(entityDtos);
        }catch (Exception e) {
            e.printStackTrace();
            globalReponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            globalReponse.setErrors(e.getMessage());
            return ResponseEntity.ok(globalReponse);
        }
        return ResponseEntity.ok(globalReponse);
    }
}
