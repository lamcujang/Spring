package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.PromotionClientService;
import com.dbiz.app.proxyclient.business.order.service.TableClientService;
import com.dbiz.app.proxyclient.config.mapper.SkipNulls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.orderDto.request.ApplicablePromoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PromotionQueryRequest;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotion")
@RequiredArgsConstructor
@Slf4j
public class PromotionController {
    private final PromotionClientService entityClientService;

    @GetMapping("/getAll")
    @SkipNulls
    public ResponseEntity<GlobalReponsePagination>getAllPromotion(@SpringQueryMap PromotionQueryRequest request){
        log.info("*** Promotion, controller; get all Promotion *");
        return ResponseEntity.ok(this.entityClientService.getAllPromotion(request).getBody());
    }
    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody PromotionDto entityDto) {
        log.info("*** Promotion, controller; save Promotion *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody PromotionDto entityDto) {
        log.info("*** Promotion, controller; update Promotion *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("*** Promotion, controller; delete promotion *");
        return ResponseEntity.ok(this.entityClientService.delete(id).getBody());
    }

    @DeleteMapping("/deletePromoMethod/{id}")
    public ResponseEntity<GlobalReponse> deletePromoMethod(@PathVariable Integer id) {
        log.info("*** Promotion, controller; delete Promotion *");
        return ResponseEntity.ok(this.entityClientService.deletePromoMethod(id).getBody());
    }


    @DeleteMapping("/deletePromoTime/{id}")
    public ResponseEntity<GlobalReponse> deletePromoTime(@PathVariable Integer id) {
        log.info("*** Promotion, controller; delete Promotion *");
        return ResponseEntity.ok(this.entityClientService.deletePromoTime(id).getBody());
    }

    @DeleteMapping("/deleteAssignOrg/{assignOrgId}")
    public ResponseEntity<GlobalReponse> deleteAssignOrg(@PathVariable Integer assignOrgId) {
        log.info("*** Promotion, controller; delete Assign Org *");
        return ResponseEntity.ok(this.entityClientService.deleteAssignOrg(assignOrgId).getBody());
    }

    @DeleteMapping("/deleteAssignBpartnerGroup/{partnerGroupId}")
    public ResponseEntity<GlobalReponse> deleteAssignBpartnerGroup(@PathVariable Integer partnerGroupId) {
        log.info("*** Promotion, controller; delete Assign Bpartner Group *");
        return ResponseEntity.ok(this.entityClientService.deleteAssignBpartnerGroup(partnerGroupId).getBody());
    }

    @GetMapping("/fromCashier")
    public ResponseEntity<GlobalReponsePagination> fromCashier(@SpringQueryMap PromotionQueryRequest request) {
        log.info("*** Promotion, controller; from Cashier *");
        return ResponseEntity.ok(this.entityClientService.fromCashier(request).getBody());
    }

    @PostMapping("/apply")
    @SkipNulls
    public ResponseEntity<GlobalReponse> getApplicablePromos(@RequestBody ApplicablePromoReqDto reqDto) {
        log.info("*** Promotion, controller; get Applicable promo *");
        return ResponseEntity.ok(this.entityClientService.getApplicablePromos(reqDto).getBody());
    }

}
