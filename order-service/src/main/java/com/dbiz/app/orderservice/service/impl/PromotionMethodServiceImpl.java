package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.service.PromotionMethodService;
import com.dbiz.app.orderservice.service.PromotionService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PromotionMethodServiceImpl implements PromotionMethodService {
    private final MessageSource messageSource;

    private final PromotionMethodRepository promotionMethodRepository;
    /**
     *
     * @param promotionMethodId
     */
    @Override
    @Transactional
    public GlobalReponse delete(Integer promotionMethodId) {
        if(!this.promotionMethodRepository.existsById(promotionMethodId))
            throw new PosException(messageSource.getMessage("promotion_method_not_found", null, LocaleContextHolder.getLocale()));
        this.promotionMethodRepository.deleteById(promotionMethodId);
        return GlobalReponse.builder()
                .data(null)
                .message(messageSource.getMessage("promotion_method_deleted", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }
}
