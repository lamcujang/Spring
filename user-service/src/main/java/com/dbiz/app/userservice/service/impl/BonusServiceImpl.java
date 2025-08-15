package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.Bonus;
import com.dbiz.app.userservice.domain.Role;
import com.dbiz.app.userservice.repository.BonusRepository;
import com.dbiz.app.userservice.service.BonusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.BonusDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.BonusQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BonusServiceImpl implements BonusService {

    private final BonusRepository bonusRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;


    private final RequestParamsUtils requestParamsUtils;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(BonusQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<Bonus> specification = Specification.where(null);
         Page<Bonus> listReponse = bonusRepository.findAll(specification, page);
        List<BonusDto> data = listReponse.getContent().stream()
                .map(bonus -> modelMapper.map(bonus, BonusDto.class))
                .collect(Collectors.toList());
        response.setData(data);
        response.setMessage(messageSource.getMessage("customer_fetch_all", null, LocaleContextHolder.getLocale()));
        response.setTotalPages(listReponse.getTotalPages());
        response.setPageSize(listReponse.getSize());
        response.setCurrentPage(listReponse.getNumber());
        response.setTotalItems(listReponse.getTotalElements());
        return response;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        Bonus bonus = bonusRepository.findById(integer).orElse(null);
        if(bonus == null)
            throw new RuntimeException(messageSource.getMessage("bonus_not_found", null, LocaleContextHolder.getLocale()));
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, null))
                .data(modelMapper.map(bonus, BonusDto.class))
                .build();
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(BonusDto entity) {
        Bonus bonus = modelMapper.map(entity, Bonus.class);
        Bonus bonusSaved ;
        GlobalReponse response = new GlobalReponse();
        if(bonus.getId() != null)
        {
            Bonus existingBonus = bonusRepository.findById(bonus.getId()).orElse(null);
            modelMapper.map(bonus, existingBonus);
            bonusSaved = bonusRepository.save(existingBonus);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        }else{
            bonus.setTenantId(0);
            bonus.setOrgId(0);
            bonusSaved = bonusRepository.save(bonus);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        }
        response.setData(modelMapper.map(bonusSaved, BonusDto.class));
        return response;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        GlobalReponse response = new GlobalReponse();
        this.bonusRepository.deleteById(integer);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setData(null);
        return response;
    }
}
