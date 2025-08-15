package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.domain.KitchenOrderLine;
import com.dbiz.app.orderservice.domain.PosOrderline;
import com.dbiz.app.orderservice.repository.KitchenOrderlineRepository;
import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.repository.PosOrderRepository;
import com.dbiz.app.orderservice.repository.PosOrderlineRepository;
import com.dbiz.app.orderservice.service.PosOrderLineService;
import com.dbiz.app.orderservice.specification.PosOrderLineSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.dto.orderDto.OrderLineDto;
import org.common.dbiz.dto.orderDto.PosOrderLineDetailDto;
import org.common.dbiz.dto.orderDto.PosOrderlineDto;
import org.common.dbiz.dto.orderDto.response.ProductDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.exception.wrapper.PerstingObjectException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelPosOrderRequest;
import org.common.dbiz.request.orderRequest.PosOrderLineQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PosOrderLineSerivceImpl implements PosOrderLineService {
    private final KitchenOrderlineRepository kitchenOrderlineRepository;

    private final PosOrderlineRepository entityRepository;

    private final PosOrderRepository posOrderRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final EntityManager entityManager;

    private final MessageSource messageSource;

    private final RestTemplate restTemplate;

    @Override
    public GlobalReponsePagination findAll(PosOrderLineQueryRequest query) {
        log.info("*** PosOrderLine List, service; fetch all PosOrderLine *");

        Pageable pageable = requestParamsUtils.getPageRequest(query);


        Specification<PosOrderline> spec = PosOrderLineSpecification.getEntitySpecification(query);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PosOrderline> entityList = entityRepository.findAll(spec, pageable);
        List<PosOrderlineDto> listData = new ArrayList<>();
        for (PosOrderline item : entityList.getContent()) {
            PosOrderlineDto lineDto = modelMapper.map(item, PosOrderlineDto.class);
            listData.add(lineDto);
        }
        response.setMessage("PosOrderLine fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }


    @Override
    public GlobalReponse save(PosOrderlineDto paramDto) {
        log.info("*** PosOrderLine, service; save PosOrderLine ***");
        GlobalReponse response = new GlobalReponse();
        PosOrderline entitySave = modelMapper.map(paramDto, PosOrderline.class);
        List<PosOrderLineDetailDto> lineDetailResponse = new ArrayList<>();
        if (entitySave.getId() != null) // update
        {
            entitySave = this.entityRepository.findById(paramDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

            // luu line detail
            if (paramDto.getLineDetails() != null) {
                Integer posOrderLineId = entitySave.getId();
                Integer orgId = entitySave.getOrgId();
                Integer PosOrderId = entitySave.getPosOrderId();
                paramDto.getLineDetails().forEach(
                        (item) -> {
                            PosOrderline itemSave = null;
                            if(item.getId() != null){
                                itemSave = entityRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
                                modelMapper.map(item, itemSave);
                            }else{
                                itemSave = modelMapper.map(item, PosOrderline.class);
                            }

//                                                PosOrderline itemSave = modelMapper.map(item, PosOrderline.class);
                            itemSave.setParentId(posOrderLineId);
                            itemSave.setOrgId(orgId);
                            itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            itemSave.setPosOrderId(PosOrderId);
                            itemSave = entityRepository.saveAndFlush(itemSave);
                            item = modelMapper.map(itemSave, PosOrderLineDetailDto.class);
                            item.setPosOrderLineId(posOrderLineId);
                            item.setId(itemSave.getId());
                            String productName = entityManager.createNativeQuery("SELECT name FROM d_product WHERE d_product_id = " + item.getProductId()).getSingleResult().toString();
                            item.setProductName(productName);
                            lineDetailResponse.add(item);
                        }
                );
            }

            modelMapper.map(paramDto, entitySave);
            this.entityRepository.save(entitySave);
            if (paramDto.getCancelReasonId() != null) {

//                kitchenOrderlineRepository.findByPosOrderLineId(paramDto.getId()).ifPresent(kitchenOrderLine -> {
//
//                    kitchenOrderLine.setCancelReasonId(paramDto.getCancelReasonId());
//
//                    kitchenOrderLine.setOrderlineStatus(AppConstant.KitchenStatus.KITCHEN_CANCEL);
//
//                    kitchenOrderlineRepository.save(kitchenOrderLine);
//
//                });

                List<KitchenOrderLine> kitchenOrderLines = kitchenOrderlineRepository.findAllByPosOrderLineId(paramDto.getId());

                for (KitchenOrderLine kitchenOrderLine : kitchenOrderLines) {

                    kitchenOrderLine.setCancelReasonId(paramDto.getCancelReasonId());
                    kitchenOrderLine.setOrderlineStatus(AppConstant.KitchenStatus.KITCHEN_CANCEL);
                    kitchenOrderlineRepository.save(kitchenOrderLine);
                }

            }

            response.setStatus(HttpStatus.OK.value());
            response.setMessage(messageSource.getMessage("posorder_line_update", null, "PosOrderLine updated successfully", LocaleContextHolder.getLocale()));
        } else {
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave = this.entityRepository.saveAndFlush(entitySave);
            // luu line detail
            if (paramDto.getLineDetails() != null) {
                Integer posOrderLineId = entitySave.getId();
                Integer posOrderId = entitySave.getPosOrderId();
                Integer orgId = entitySave.getOrgId();
                paramDto.getLineDetails().forEach(
                        (item) -> {
                            PosOrderline itemSave = modelMapper.map(item, PosOrderline.class);
                            itemSave.setParentId(posOrderLineId);
                            itemSave.setOrgId(orgId);
                            itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            itemSave.setPosOrderId(posOrderId);
                            itemSave = entityRepository.saveAndFlush(itemSave);
                            item = modelMapper.map(itemSave, PosOrderLineDetailDto.class);
                            item.setPosOrderLineId(posOrderLineId);
                            item.setId(itemSave.getId());
                            String productName = entityManager.createNativeQuery("SELECT name FROM d_product WHERE d_product_id = " + item.getProductId()).getSingleResult().toString();
                            item.setProductName(productName);
                            lineDetailResponse.add(item);
                        }
                );
            }
            response.setMessage(messageSource.getMessage("posorder_line_created", null, "PosOrderLine saved successfully", LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());

        }

        PosOrderlineDto dto = modelMapper.map(entitySave, PosOrderlineDto.class);
        dto.setLineDetails(lineDetailResponse);
        response.setData(dto);
        log.info("PosOrderlineDto saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete PosOrderLine by id *");
        GlobalReponse response = new GlobalReponse();

        StringBuilder sql = new StringBuilder("select count(1) from d_pos_orderline dpo " +
                "join d_kitchen_orderline dko on dpo.d_kitchen_orderline_id = dko.d_kitchen_orderline_id " +
                " where dko.orderline_status in ('WTP', 'BPR', 'PRD', 'PAI') ");

        sql.append(" and dpo.d_pos_orderline_id = :posOrderLineId limit 1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("posOrderLineId", id);

        Long count = ((Number) query.getSingleResult()).longValue();

        if(count > 0){ //Khoong cho phep xoa

            throw new PosException(messageSource.getMessage("posorder.line.not.delete", null, LocaleContextHolder.getLocale()));
        }else {
            Optional<PosOrderline> entityDelete = this.entityRepository.findById(id);
            if (entityDelete.isEmpty()) {
                response.setMessage("PriceListProduct not found with id: " + id);
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return response;
            }
            entityDelete.get().setIsActive("N");
            entityRepository.save(entityDelete.get());
            List<PosOrderline> listExtra = entityRepository.findAllByParentId(id);
            if (!listExtra.isEmpty()) {
                listExtra.forEach(extra -> {
                    extra.setIsActive("N");
                    entityRepository.save(extra);
                });
            }
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, "PosOrderLine deleted successfully", LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse deleteWithReason(PosOrderlineDto paramDto) {

        log.info("*** Void, service; delete PosOrderLine with cancel reason *");
        GlobalReponse response = new GlobalReponse();

        StringBuilder sql = new StringBuilder("select count(1) from d_pos_orderline dpo " +
                "join d_kitchen_orderline dko on dpo.d_kitchen_orderline_id = dko.d_kitchen_orderline_id " +
                " where dko.orderline_status in ('WTP', 'BPR', 'PRD', 'PAI') ");

        sql.append(" and dpo.d_pos_orderline_id = :posOrderLineId limit 1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("posOrderLineId", paramDto.getId());

        Long count = ((Number) query.getSingleResult()).longValue();

        if(count > 0){ //Khoong cho phep xoa

            throw new PosException(messageSource.getMessage("posorder.line.not.delete", null, LocaleContextHolder.getLocale()));
        }else {

            Optional<PosOrderline> entityDelete = this.entityRepository.findById(paramDto.getId());

            if (entityDelete.isEmpty()) {
                response.setMessage("PriceListProduct not found with id: " + paramDto);
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return response;
            }

            entityDelete.get().setIsActive("N");
            entityDelete.get().setCancelReasonId(paramDto.getCancelReasonId());
            entityDelete.get().setCancelReasonMessage(paramDto.getCancelReasonMessage());

            entityRepository.save(entityDelete.get());
            List<PosOrderline> listExtra = entityRepository.findAllByParentId(paramDto.getId());
            if (!listExtra.isEmpty()) {
                listExtra.forEach(extra -> {
                    extra.setIsActive("N");
                    entityRepository.save(extra);
                });
            }
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, "PosOrderLine deleted successfully", LocaleContextHolder.getLocale()))
                .build();

//        response.setMessage(messageSource.getMessage("successfully", null, "PosOrderLine deleted successfully", LocaleContextHolder.getLocale()));
//        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** PosOrderLine, service; fetch PosOrderLine by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.entityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id))), PosOrderlineDto.class));
        response.setMessage(messageSource.getMessage("successfully", null, "PosOrderLine fetched successfully", LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse CancelPosOrder(CancelPosOrderRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        GlobalReponse response = new GlobalReponse();
        PosOrderline line = entityRepository.findById(request.getPosOrderId()).orElseThrow(() -> new PerstingObjectException("Pos Order Line not found"));
        line.setQty(request.getQty());
        entityRepository.save(line);
//        kitchenOrderlineRepository.findAllByPosOrderLineId(request.getPosOrderId()).ifPresent(kitchenOrderLine -> {
//            kitchenOrderLine.setOrderlineStatus(AppConstant.KitchenStatus.KITCHEN_CANCEL);
//            kitchenOrderLine.setCancelReasonId(request.getCancelReasonId());
//            kitchenOrderlineRepository.save(kitchenOrderLine);
//        });

        List<KitchenOrderLine> kitchenOrderLines = kitchenOrderlineRepository.findAllByPosOrderLineId(request.getPosOrderId());

        for (KitchenOrderLine kitchenOrderLine : kitchenOrderLines) {

            kitchenOrderLine.setOrderlineStatus(AppConstant.KitchenStatus.KITCHEN_CANCEL);
            kitchenOrderLine.setCancelReasonId(request.getCancelReasonId());
            kitchenOrderlineRepository.save(kitchenOrderLine);
        }

        PosOrderlineDto dto = modelMapper.map(line, PosOrderlineDto.class);
        dto.setStatus(request.getStatus());
        GlobalReponse orderService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_URL + "/" + line.getProductId(), HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
        ProductDto productDto = modelMapper.map(orderService.getData(), ProductDto.class);

        dto.setProductDto(productDto);
        response.setData(dto);
        response.setErrors("");
        response.setMessage(messageSource.getMessage("kitchen_order_cancel", null, "Order Line canceled successfully", LocaleContextHolder.getLocale()));

        return response;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse CancelByRequestOrderId(CancelPosOrderRequest request) {
        return null;
    }


}
