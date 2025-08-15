package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.ConfigSalaryBonusAllowance;
import com.dbiz.app.userservice.repository.ConfigSalaryBonusAllowanceRepository;
import com.dbiz.app.userservice.service.ConfigSalaryBonusAllowanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigSalaryBonusAllowanceDto;
import org.common.dbiz.dto.userDto.request.ConfigSalaryBonusAllowanceRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigSalaryBonusAllowanceServiceImpl implements ConfigSalaryBonusAllowanceService {
    ConfigSalaryBonusAllowanceRepository configSalaryBonusAllowanceRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;


    @Override
    public GlobalReponsePagination findAll(ConfigSalaryBonusAllowanceRequest request) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(ConfigSalaryBonusAllowanceDto dto) {
        log.info("ConfigSalaryBonusAllowanceServiceImpl save");
        log.info("ConfigSalaryBonusAllowanceServiceImpl dto: {}", dto);

        ConfigSalaryBonusAllowance entity = null;

        if(dto.getId() == null){
            entity = modelMapper.map(dto, ConfigSalaryBonusAllowance.class);

            entity.setOrgId(0);
            entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
        }else{
            entity = configSalaryBonusAllowanceRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("ConfigSalaryBonusAllowance not found"));

            modelMapper.map(dto, entity);
        }

        entity = configSalaryBonusAllowanceRepository.save(entity);

        return GlobalReponse.builder()
                .data(modelMapper.map(entity, ConfigSalaryBonusAllowanceDto.class))
                .status(HttpStatus.CREATED.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        log.info("ConfigSalaryBonusAllowanceServiceImpl delete");

        ConfigSalaryBonusAllowance entity = configSalaryBonusAllowanceRepository.findById(integer)
                .orElseThrow(() -> new RuntimeException("ConfigSalaryBonusAllowance not found"));

        entity.setIsActive("N");
        configSalaryBonusAllowanceRepository.save(entity);

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
}
