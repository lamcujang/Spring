package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.repository.KitchenOrderlineRepository;
import com.dbiz.app.orderservice.domain.OrderLine;
import com.dbiz.app.orderservice.repository.OrderLineRepository;
import com.dbiz.app.orderservice.service.OrderLineService;
import com.dbiz.app.orderservice.specification.OrderLineSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderLineDto;
import org.common.dbiz.dto.orderDto.OrderLineListDto;
import org.common.dbiz.exception.wrapper.PerstingObjectException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderLineQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final RequestParamsUtils requestParamsUtils;
    private final ModelMapper modelMapper;
    private final KitchenOrderlineRepository kitchenOrderlineRepository;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(OrderLineQueryRequest query) {
        log.info("*** OrderLine List, service; fetch all OrderLine *");

        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<OrderLine> spec = OrderLineSpecification.getSpecification(query);

        Page<OrderLine> entityList = orderLineRepository.findAll(spec, pageable);
        List<OrderLineDto > listData = new ArrayList<>();
        for(OrderLine item : entityList.getContent()) {
            listData.add(modelMapper.map(item, OrderLineDto.class));
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("Order Lines fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(OrderLineListDto orderlineListDto) {

        log.info("*** Order Line, service; save Order Line ***");
        try{
            OrderLine orderLine = null;
            for (OrderLineDto orderlineDto : orderlineListDto.getLines()){
                orderLine = orderLineRepository.save(modelMapper.map(orderlineDto, OrderLine.class));
            }

        }catch (Exception e) {
            throw new PerstingObjectException("Error while saving Order Line");
        }
        GlobalReponse response = new GlobalReponse();
        response.setMessage("Order Line saved successfully");
        response.setStatus(HttpStatus.CREATED.value());
//        response.setData(modelMapper.map(orderLine, OrderLineDto.class));
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }


}
