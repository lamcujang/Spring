package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Reason;
import com.dbiz.app.systemservice.repository.ReasonRepository;
import com.dbiz.app.systemservice.service.ReasonService;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReasonQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository reasonRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ReasonQueryRequest request) {
        log.info("Finding all Reasons with request: {}", request);
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Reason> specification = Specification.where(null);
        if (request.getKeyword() != null) {
            specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getKeyword().toLowerCase() + "%"));
            specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + request.getKeyword().toLowerCase() + "%"));
        }
        Page<Reason> reasonPage = this.reasonRepository.findAll(specification, pageable);
        List<ReasonDto> reasonDtos = reasonPage.getContent().stream()
                .map(reason -> modelMapper.map(reason, ReasonDto.class))
                .collect(Collectors.toList());
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        globalReponsePagination.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(reasonPage.getTotalPages());
        globalReponsePagination.setPageSize(reasonPage.getSize());
        globalReponsePagination.setCurrentPage(reasonPage.getNumber());
        globalReponsePagination.setTotalItems(reasonPage.getTotalElements());
        globalReponsePagination.setData(reasonDtos);
        return globalReponsePagination;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        log.info("Finding Reason by ID: {}", integer);
        Reason reason = this.reasonRepository.findById(integer).orElse(null);
        if (reason != null) {
            ReasonDto reasonDto = modelMapper.map(reason, ReasonDto.class);
            GlobalReponse response = new GlobalReponse();
            response.setData(reasonDto);
            response.setMessage(messageSource.getMessage("reason.found", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            return response;
        }
        return null;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(ReasonDto entity) {
        log.info("Saving Reason: {}", entity);
        Reason reason = modelMapper.map(entity, Reason.class);
        Reason reasonSave ;
        GlobalReponse response = new GlobalReponse();
        if(entity.getId() != null)
        {
            Reason reasonCheck = this.reasonRepository.findById(entity.getId()).orElse(null);
            modelMapper.map(entity,reasonCheck);
            reasonSave = this.reasonRepository.save(reasonCheck);
            response.setMessage(messageSource.getMessage("reason.update.success", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        }
        else{
            reasonSave = this.reasonRepository.save(reason);
            response.setMessage(messageSource.getMessage("reason.create.success", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }
        response.setData(modelMapper.map(reasonSave, ReasonDto.class));
        return response;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
    log.info("Deleting Reason by ID: {}", integer);
        this.reasonRepository.deleteById(integer);
        return GlobalReponse.builder()
                .message(messageSource.getMessage("reason.delete.success", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
