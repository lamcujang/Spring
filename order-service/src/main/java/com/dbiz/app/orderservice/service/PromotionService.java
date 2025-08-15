package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.request.ApplicablePromoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PromotionQueryRequest;

public interface PromotionService {

    GlobalReponsePagination getAllPromotion(PromotionQueryRequest paramDto);
    GlobalReponse save(PromotionDto paramDto);
    GlobalReponse delete(Integer id);

    GlobalReponse deleteAssignOrg(Integer assignOrgId);
    GlobalReponse deleteAssignBpartnerGroup(Integer partnerGroupId);

    GlobalReponsePagination getAllPromotionForCahiser(PromotionQueryRequest paramDto);

    GlobalReponse getApplicablePromos(ApplicablePromoReqDto reqDto);

}
