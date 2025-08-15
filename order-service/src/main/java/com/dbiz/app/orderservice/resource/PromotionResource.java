package com.dbiz.app.orderservice.resource;

import com.dbiz.app.orderservice.service.PromotionMethodService;
import com.dbiz.app.orderservice.service.PromotionService;
import com.dbiz.app.orderservice.service.PromotionTimeService;
import com.dbiz.app.orderservice.service.TableService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.orderDto.request.ApplicablePromoReqDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewTable;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PromotionQueryRequest;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotion")
@Slf4j
@RequiredArgsConstructor
public class PromotionResource {
    private final PromotionService entityService;

    private final PromotionMethodService promotionMethodService;

    private final PromotionTimeService promotionTimeService;

    @GetMapping("/getAll")
    public ResponseEntity<GlobalReponsePagination>getAllPromotion(@ModelAttribute PromotionQueryRequest request){
        log.info("*** Promotion, resource; get all Promotion *");
        return ResponseEntity.ok(this.entityService.getAllPromotion(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody PromotionDto entityDto) {
        log.info("*** Promotion, resource; save Promotion *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody PromotionDto entityDto) {
        log.info("*** Promotion, resource; update Promotion *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("*** Promotion, resource; delete promotion *");
        return ResponseEntity.ok(this.entityService.delete(id));
    }

    @DeleteMapping("/deletePromoMethod/{id}")
    public ResponseEntity<GlobalReponse> deletePromoMethod(@PathVariable Integer id) {
        log.info("*** Promotion, resource; delete promotion method *");
        return ResponseEntity.ok(this.promotionMethodService.delete(id));
    }


    @DeleteMapping("/deletePromoTime/{id}")
    public ResponseEntity<GlobalReponse> deletePromoTime(@PathVariable Integer id) {
        log.info("*** Promotion, resource; delete Promotion Time *");
        return ResponseEntity.ok(this.promotionTimeService.delete(id));
    }

    @DeleteMapping("/deleteAssignOrg/{assignOrgId}")
    public ResponseEntity<GlobalReponse> deleteAssignOrg(@PathVariable Integer assignOrgId) {
        log.info("*** Promotion, resource; delete Assign Org *");
        return ResponseEntity.ok(this.entityService.deleteAssignOrg(assignOrgId));
    }

    @DeleteMapping("/deleteAssignBpartnerGroup/{partnerGroupId}")
    public ResponseEntity<GlobalReponse> deleteAssignBpartnerGroup(@PathVariable Integer partnerGroupId) {
        log.info("*** Promotion, resource; delete Assign Bpartner Group *");
        return ResponseEntity.ok(this.entityService.deleteAssignBpartnerGroup(partnerGroupId));
    }

    @GetMapping("/fromCashier")
    public ResponseEntity<GlobalReponsePagination> fromCashier(@ModelAttribute PromotionQueryRequest request) {
        log.info("*** Promotion, resource; from Cashier *");
        return ResponseEntity.ok(this.entityService.getAllPromotionForCahiser(request));
    }

    @PostMapping("/apply")
    public ResponseEntity<GlobalReponse> getApplicablePromos(@RequestBody ApplicablePromoReqDto reqDto) {
        log.info("*** Promotion, resource; get Applicable promo *");
        return ResponseEntity.ok(this.entityService.getApplicablePromos(reqDto));
    }

}
