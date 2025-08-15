package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.domain.view.IntegrationHistoryGetV;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryGetVRepository;
import com.dbiz.app.integrationservice.specification.IntegrationHistoryGetVSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import com.dbiz.app.integrationservice.helper.DateHelper;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.IntegrationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class IntegrationHistoryServiceImpl implements IntegrationHistoryService {
    private final IntegrationHistoryRepository integrationRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final IntegrationHistoryGetVRepository integrationHistoryGetVRepository;

    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(IntegrationHistoryQueryRequest request) {
        GlobalReponsePagination responsePagination = new GlobalReponsePagination();
        Specification<IntegrationHistoryGetV> spec = IntegrationHistoryGetVSpecification.getErpIntegrationSpecification(request);
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<IntegrationHistoryGetV> page = integrationHistoryGetVRepository.findAll(spec, pageable);

        responsePagination.setData(page.getContent().stream().map(entity ->
        {
            return IntegrationHistoryDto.builder()
                    .id(entity.getIntegrationHistoryId())
                    .isActive(entity.getIsActive())
                    .intDate(DateHelper.fromInstant(entity.getIntDate()))
                    .intType(entity.getIntType())
                    .intFlow(entity.getIntFlow())
                    .intStatus(entity.getIntStatus())
                    .user(entity.getUserV()!= null ? UserDto.builder().userId(entity.getUserV().getId()).fullName(entity.getUserV().getFullName()).build() : null)
                    .description(entity.getDescription())
                    .build();
        }).collect(Collectors.toList()));
        responsePagination.setTotalItems(page.getTotalElements());
        responsePagination.setTotalPages(page.getTotalPages());
        responsePagination.setCurrentPage(page.getNumber());
        responsePagination.setStatus(HttpStatus.OK.value());
        responsePagination.setPageSize(page.getSize());
        responsePagination.setMessage("IntegrationHistory fetched successfully");
        return responsePagination;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(IntegrationHistoryDto entity) {
        GlobalReponse response = new GlobalReponse();
        IntegrationHistory history = modelMapper.map(entity, IntegrationHistory.class);
        history.setIntDate(DateHelper.toInstantDateAndTime(entity.getIntDate()));
        history.setTenantId(com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId());
        integrationRepository.save(history);
        entity = modelMapper.map(history, IntegrationHistoryDto.class);
        entity.setIntDate(DateHelper.fromInstant(history.getIntDate()));
        response.setData(entity);
        response.setMessage("IntegrationHistory saved successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse saveAll(List<IntegrationHistoryDto> dtos) {
        List<IntegrationHistoryDto> dtoResponse = new ArrayList<>();
        GlobalReponse response = new GlobalReponse();
        dtos.stream().forEach(dto -> {
            // save dto
            IntegrationHistory history = modelMapper.map(dto, IntegrationHistory.class);
            history.setIntDate(DateHelper.toInstantDateAndTime(dto.getIntDate()));
            integrationRepository.save(history);
            dto = modelMapper.map(history, IntegrationHistoryDto.class);
            dto.setIntDate(DateHelper.fromInstant(history.getIntDate()));
            dtoResponse.add(dto);
        });
        response.setData(dtoResponse);
        response.setMessage("IntegrationHistory saved successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }


    @Override
    public void saveIntegrationHistory(IntegrationHistoryDto dto) {

        IntegrationHistory history = modelMapper.map(dto, IntegrationHistory.class);
        history.setIntDate(Instant.now());
        history.setTenantId(com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId());
        integrationRepository.save(history);
    }

    /**
     *
     * @param testInt
     * @return
     */
    @Override
    public ResponseEntity<GlobalReponse> testInt(String testInt) {
        try {
            if(testInt.equals("ERROR")){
                throw new Exception("Error occurred while testing integration");
            }
            else {
                return ResponseEntity.ok(GlobalReponse.builder().message("Integration tested successfully").status(HttpStatus.OK.value()).build());
            }
        }catch (Exception e){
            log.error("Error occurred while testing integration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GlobalReponse.builder().message("Error occurred while testing integration").status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }
}
