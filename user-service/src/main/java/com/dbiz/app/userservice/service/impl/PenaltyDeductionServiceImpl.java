package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.PenaltyDeduction;
import com.dbiz.app.userservice.repository.PenaltyDeductionRepository;
import com.dbiz.app.userservice.service.PenaltyDeductionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CustomerDto;
import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PenaltyDeductionServiceImpl implements PenaltyDeductionService {
    PenaltyDeductionRepository penaltyDeductionRepository;

    RestTemplate restTemplate;
    ModelMapper modelMapper;
    MessageSource messageSource;
    @Override
    public GlobalReponse save(PenaltyDeductionDto dto) {
        log.info("Save PenaltyDeductionDto:{}", dto);

        PenaltyDeduction penaltyDeduction = null;
        if(dto.getId() == null){
            penaltyDeduction =  modelMapper.map(dto, PenaltyDeduction.class);
            penaltyDeduction.setOrgId(0);
            penaltyDeduction.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(dto.getCode() != null){
                switch (dto.getCode()) {
                    case "OTH": {
                        if(penaltyDeduction.getPenaltyAmount() == null) {
                            throw new PosException(messageSource.getMessage("penalty.deduction.amount.is.null", null, LocaleContextHolder.getLocale()));
                        }

                        if(penaltyDeduction.getWarningCount() == null) {
                            throw new PosException(messageSource.getMessage("penalty.deduction.warning.is.null", null, LocaleContextHolder.getLocale()));
                        }
                        break;
                    }
                    case "ULV": {
                        checkValueReferenceList(dto.getValue(), "Unauthorized Leave");
                        if("FIX".equals(penaltyDeduction.getValue())){
                            if(penaltyDeduction.getPenaltyAmount() != null) {
                                dto.setPenaltyAmount(dto.getPenaltyAmount());
                            }else{
                                throw new PosException(messageSource.getMessage("penalty.deduction.amount.is.null", null, LocaleContextHolder.getLocale()));
                            }
                        }

                        break;
                    }
                    case "BIR":{
                        checkValueReferenceList(dto.getValue(), "Break Internal Rule");
                        if("MIN".equals(penaltyDeduction.getValue())) {
                            penaltyDeduction.setPenaltyAmount(BigDecimal.valueOf(200000));
                        }else if("MAJ".equals(penaltyDeduction.getValue())) {
                            penaltyDeduction.setPenaltyAmount(BigDecimal.valueOf(300000));
                        }else if("SER".equals(penaltyDeduction.getValue())) {
                            penaltyDeduction.setPenaltyAmount(BigDecimal.valueOf(1000000));
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("Invalid PenaltyDeductionDto");
                }

                penaltyDeduction = penaltyDeductionRepository.save(penaltyDeduction);
            }
        }else{
            penaltyDeduction = penaltyDeductionRepository.findById(dto.getId()).orElse(null);

            if(penaltyDeduction == null){
                throw new PosException(messageSource.getMessage("penalty.deduction.not.exist", null, LocaleContextHolder.getLocale()));
            }

            String reference = null;
            if(penaltyDeduction.getCode().equals("ULV")){
                checkValueReferenceList(dto.getValue(), "Unauthorized Leave");
            }else if(penaltyDeduction.getCode().equals("BIR")){
                checkValueReferenceList(dto.getValue(), "Break Internal Rule");
            }else if(penaltyDeduction.getCode().equals("OTH")){
                if(penaltyDeduction.getPenaltyAmount() == null) {
                    throw new PosException(messageSource.getMessage("penalty.deduction.amount.is.null", null, LocaleContextHolder.getLocale()));
                }

                if(penaltyDeduction.getWarningCount() != null) {
                    throw new PosException(messageSource.getMessage("penalty.deduction.warning.is.null", null, LocaleContextHolder.getLocale()));
                }
            }

            modelMapper.map(dto, penaltyDeduction);

            penaltyDeduction = penaltyDeductionRepository.save(penaltyDeduction);
        }

        return GlobalReponse.builder()
                .data(modelMapper.map(penaltyDeduction, PenaltyDeductionDto.class))
                .status(org.apache.http.HttpStatus.SC_CREATED)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    private void checkValueReferenceList(String value, String nameReference) {
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);

        GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.REFERENCELIST_SERVICE_API_URL_VALUE + "?nameReference=" + nameReference + "&value=" + value,
                HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();

       if(exRes == null || exRes.getData() == null){
           throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
       }
    }


    @Override
    public GlobalReponse delete(Integer id) {
        PenaltyDeduction penaltyDeduction = penaltyDeductionRepository.findById(id).orElseThrow(()
                -> new PosException(messageSource.getMessage("penalty.deduction.not.exist", null, LocaleContextHolder.getLocale())));

        penaltyDeduction.setIsActive("N");
        penaltyDeductionRepository.save(penaltyDeduction);

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(org.apache.http.HttpStatus.SC_OK)
                .build();
    }
}
