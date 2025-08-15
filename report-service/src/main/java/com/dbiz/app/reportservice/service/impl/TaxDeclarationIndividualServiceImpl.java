

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.*;
import com.dbiz.app.reportservice.repository.*;
import com.dbiz.app.reportservice.service.TaxDeclarationIndividualService;
import com.dbiz.app.reportservice.specification.TaxDeclarationIndividualSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.ConfigForTenantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.dto.reportDto.*;
import org.common.dbiz.dto.reportDto.response.ReportSaleServiceTypeDto;
import org.common.dbiz.dto.reportDto.response.TaxDeclarationIndividualHeaderResDto;
import org.common.dbiz.dto.reportDto.response.TaxDeclarationVatPitLineInitDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TaxDeclarationIndividualServiceImpl implements TaxDeclarationIndividualService {

    private final TaxDeclarationIndividualRepository taxDeclarationIndividualRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;
    private final EntityManager entityManager;
    private final DataSource dataSource;

    private final IndividualIndustryRepository individualIndustryRepository;
    private final TaxDeclarationVatPitLineRepository taxDeclarationVatPitLineRepository;
    private final TaxDeclarationExciseRepository taxDeclarationExciseRepository;
    private final TaxDeclarationResourceEnvironmentRepository taxDeclarationResourceEnvironmentRepository;
    private final TaxAgencyInfoRepository taxAgencyInfoRepository;
    private final TaxHouseholdProfileRepository taxHouseholdProfileRepository;
    private final TaxDeclarationExpenseDetailRepository taxDeclarationExpenseDetailRepository;
    private final TaxDeclarationInventoryDetailRepository taxDeclarationInventoryDetailRepository;
    private final HouseholdIndustryRepository householdIndustryRepository;
    private final TaxPaymentMethodRepository taxPaymentMethodRepository;

    private final ConfigForTenantRepository configForTenantRepository;


    @Override
    public GlobalReponsePagination findAll(TaxDeclarationIndividualQueryRequest paramRequest) {

        log.info("*** TaxDeclarationIndividualDto List, service; fetch all taxDeclarationIndividuals *");

        paramRequest.setSortBy("id");
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);

        Specification<TaxDeclarationIndividual> spec = TaxDeclarationIndividualSpecification.getSpecification(paramRequest);
        Page<TaxDeclarationIndividual> entityList = taxDeclarationIndividualRepository.findAll(spec, pageable);

        List<TaxDeclarationIndividualDto> listData = new ArrayList<>();
        for (TaxDeclarationIndividual item : entityList.getContent()) {

            TaxDeclarationIndividualDto taxDeclarationIndividualDto = modelMapper.map(item, TaxDeclarationIndividualDto.class);

//            setAllNecessaryInfo(taxDeclarationIndividualDto);
            listData.add(taxDeclarationIndividualDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxDeclarationIndividual fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponsePagination findAllHeader(TaxDeclarationIndividualQueryRequest paramRequest) {

        log.info("*** TaxDeclarationIndividualResDto List, service; fetch all TaxDeclarationIndividualResDto *");

        paramRequest.setSortBy("id");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);

        Specification<TaxDeclarationIndividual> spec = TaxDeclarationIndividualSpecification.getSpecification(paramRequest);
        Page<TaxDeclarationIndividual> entityList = taxDeclarationIndividualRepository.findAll(spec, pageable);

        List<TaxDeclarationIndividualHeaderResDto> listData = new ArrayList<>();
        for (TaxDeclarationIndividual item : entityList.getContent()) {

            TaxDeclarationIndividualHeaderResDto taxDeclarationIndividualDto = modelMapper.map(item, TaxDeclarationIndividualHeaderResDto.class);
            listData.add(taxDeclarationIndividualDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxDeclarationIndividualResDto fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse init(TaxDeclarationIndividualQueryRequest paramRequest) {

        log.info("*** TaxDeclarationIndividualDto, service; fetch init taxDeclarationIndividual *");

        paramRequest.setSortBy("id");

        GlobalReponse response = new GlobalReponse();

        TaxDeclarationInforDto taxDeclarationInforDto = new TaxDeclarationInforDto();
        taxDeclarationInforDto.setTaxDeclarationIndividualDto(setAllTaxDeclarationIndividualInitInfo(paramRequest));

        setAllNecessaryInfoForInit(taxDeclarationInforDto);

        response.setData(taxDeclarationInforDto);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxDeclarationIndividualId) {
        log.info("*** TaxDeclarationIndividualDto, service; fetch taxDeclarationIndividual by id *");

        GlobalReponse response = new GlobalReponse();

        TaxDeclarationIndividualDto taxDeclarationIndividualDto = modelMapper.map(this.taxDeclarationIndividualRepository.findById(taxDeclarationIndividualId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxDeclarationIndividual with id: %d not found", taxDeclarationIndividualId))), TaxDeclarationIndividualDto.class);

        TaxDeclarationInforDto taxDeclarationInforDto = new TaxDeclarationInforDto();
        taxDeclarationInforDto.setTaxDeclarationIndividualDto(taxDeclarationIndividualDto);
        setAllNecessaryInfo(taxDeclarationIndividualDto.getId(), taxDeclarationInforDto);

        response.setData(taxDeclarationInforDto);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(TaxDeclarationIndividualDto entity) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse saveAllInfo(TaxDeclarationInforDto taxDeclarationInforDto) {

        GlobalReponse response = new GlobalReponse();
        TaxDeclarationIndividualDto resultDto = null;
        TaxDeclarationIndividual entitySave = null;

        TaxDeclarationIndividualDto taxDeclarationIndividualDto = taxDeclarationInforDto.getTaxDeclarationIndividualDto();

        if (taxDeclarationIndividualDto.getId() != null) {

            GlobalReponse exRes = null;
            entitySave = this.taxDeclarationIndividualRepository.findById(taxDeclarationIndividualDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(taxDeclarationIndividualDto, entitySave);
        } else {

            entitySave = modelMapper.map(taxDeclarationIndividualDto, TaxDeclarationIndividual.class);
        }

        entitySave = this.taxDeclarationIndividualRepository.saveAndFlush(entitySave);

        // chuyen ve dtoTaxDeclarationIndividualRepository
        resultDto = modelMapper.map(entitySave, TaxDeclarationIndividualDto.class);

        if (taxDeclarationIndividualDto.getIndividualIndustryDto() != null && !taxDeclarationIndividualDto.getIndividualIndustryDto().isEmpty()) {

            // NGÀNH NGHỀ KINH DOANH CỦA HỘ KINH DOANH
            List<IndividualIndustryDto> individualIndustryDto = processIndividualIndustry(taxDeclarationIndividualDto, entitySave);
            resultDto.setIndividualIndustryDto(individualIndustryDto);
        }

        if (taxDeclarationInforDto.getTaxDeclarationExciseDto() != null && !taxDeclarationInforDto.getTaxDeclarationExciseDto().isEmpty()) {

            // A. KÊ KHAI THUẾ GIÁ TRỊ GIA TĂNG (GTGT), THUẾ THU NHẬP CÁ NHÂN (TNCN)
            List<TaxDeclarationExciseDto> taxDeclarationExciseDto = processTaxDeclarationExcise(taxDeclarationInforDto, entitySave);
            taxDeclarationInforDto.setTaxDeclarationExciseDto(taxDeclarationExciseDto);
        }

        if (taxDeclarationInforDto.getTaxDeclarationVatPitLineDto() != null && !taxDeclarationInforDto.getTaxDeclarationVatPitLineDto().isEmpty()) {

            // B. KÊ KHAI THUẾ TIÊU THỤ ĐẶC BIỆT (TTĐB)
            List<TaxDeclarationVatPitLineDto> taxDeclarationVatPitLineDto = processTaxDeclarationVatPitLine(taxDeclarationInforDto, entitySave);
            taxDeclarationInforDto.setTaxDeclarationVatPitLineDto(taxDeclarationVatPitLineDto);
        }

        if (taxDeclarationInforDto.getTaxDeclarationResourceEnvironmentDto() != null && !taxDeclarationInforDto.getTaxDeclarationResourceEnvironmentDto().isEmpty()) {

            // C. KÊ KHAI THUẾ/PHÍ BẢO VỆ MÔI TRƯỜNG HOẶC THUẾ TÀI NGUYÊN
            List<TaxDeclarationResourceEnvironmentDto> taxDeclarationResourceEnvironmentDto = processTaxDeclarationResourceEnvironment(taxDeclarationInforDto.getTaxDeclarationResourceEnvironmentDto(), entitySave);
            taxDeclarationInforDto.setTaxDeclarationResourceEnvironmentDto(taxDeclarationResourceEnvironmentDto);
        }

        if (taxDeclarationInforDto.getTaxDeclarationExpenseDetailDto() != null && !taxDeclarationInforDto.getTaxDeclarationExpenseDetailDto().isEmpty()) {

            //
            List<TaxDeclarationExpenseDetailDto> taxDeclarationExpenseDetailDto = processTaxDeclarationExpenseDetail(taxDeclarationInforDto.getTaxDeclarationExpenseDetailDto(), entitySave);
            taxDeclarationInforDto.setTaxDeclarationExpenseDetailDto(taxDeclarationExpenseDetailDto);
        }

        if (taxDeclarationInforDto.getTaxDeclarationInventoryDetailDto() != null && !taxDeclarationInforDto.getTaxDeclarationInventoryDetailDto().isEmpty()) {

            //
            List<TaxDeclarationInventoryDetailDto> taxDeclarationInventoryDetailDto = processTaxDeclarationInventoryDetailDto(taxDeclarationInforDto.getTaxDeclarationInventoryDetailDto(), entitySave);
            taxDeclarationInforDto.setTaxDeclarationInventoryDetailDto(taxDeclarationInventoryDetailDto);
        }

        taxDeclarationInforDto.setTaxDeclarationIndividualDto(resultDto);

        response.setData(taxDeclarationInforDto);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    public void setAllNecessaryInfoForInit(TaxDeclarationInforDto taxDeclarationInforDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        Integer taxDeclarationIndividualId = 1000000;

        TaxAgencyInfo taxAgencyInfo = taxAgencyInfoRepository.findLatestActiveNative().orElse(new TaxAgencyInfo());
        TaxAgencyInfoDto taxAgencyInfoDto = modelMapper.map(taxAgencyInfo, TaxAgencyInfoDto.class);

        // NGÀNH NGHỀ KINH DOANH CỦA HỘ KINH DOANH
        List<HouseholdIndustry> householdIndustry = householdIndustryRepository.findAllByIsActive("Y");
        List<IndividualIndustryDto> individualIndustryDto = new ArrayList<>();

        if (householdIndustry != null && !householdIndustry.isEmpty()) {

            for (HouseholdIndustry item : householdIndustry) {

                IndividualIndustryDto dto = IndividualIndustryDto.builder()
                        .taxBusinessIndustryId(item.getTaxBusinessIndustryId())
                        .isActive(item.getIsActive())
                        .build();
                setAllNecessaryBusinessIndustryInfo(dto);
                individualIndustryDto.add(dto);
            }
        }

        String taxMonthFrom = taxDeclarationInforDto.getTaxDeclarationIndividualDto().getTaxMonthFrom().toString();
        String taxMonthTo = taxDeclarationInforDto.getTaxDeclarationIndividualDto().getTaxMonthTo().toString();

        // A. KÊ KHAI THUẾ GIÁ TRỊ GIA TĂNG (GTGT), THUẾ THU NHẬP CÁ NHÂN (TNCN)
        List<TaxDeclarationVatPitLineDto> taxDeclarationVatPitLineDto = getInitReportSaleServiceType(taxMonthFrom, taxMonthTo);

        // B. KÊ KHAI THUẾ TIÊU THỤ ĐẶC BIỆT (TTĐB)
        List<TaxDeclarationExciseDto> taxDeclarationExciseDto = getInitTaxDeclarationExcise(taxMonthFrom, taxMonthTo);

        taxDeclarationIndividualId = null;

        // C. KÊ KHAI THUẾ/PHÍ BẢO VỆ MÔI TRƯỜNG HOẶC THUẾ TÀI NGUYÊN
        List<TaxDeclarationResourceEnvironmentDto> taxDeclarationResourceEnvironmentDto = new ArrayList<>();

        // Tờ 2

        //
        List<TaxDeclarationInventoryDetail> entityListD = taxDeclarationInventoryDetailRepository.findByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationInventoryDetailDto> taxDeclarationInventoryDetailDto = new ArrayList<>();
        if (!entityListD.isEmpty()) {

            for (TaxDeclarationInventoryDetail item : entityListD) {

                TaxDeclarationInventoryDetailDto dto = modelMapper.map(item, TaxDeclarationInventoryDetailDto.class);
                taxDeclarationInventoryDetailDto.add(dto);
            }
        }

        //
        List<TaxDeclarationExpenseDetail> entityListE = taxDeclarationExpenseDetailRepository.findByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationExpenseDetailDto> taxDeclarationExpenseDetailDto = new ArrayList<>();
        if (!entityListE.isEmpty()) {

            for (TaxDeclarationExpenseDetail item : entityListE) {

                TaxDeclarationExpenseDetailDto dto = modelMapper.map(item, TaxDeclarationExpenseDetailDto.class);
                setAllNecessaryExpenseTypeInfo(dto);
                taxDeclarationExpenseDetailDto.add(dto);
            }
        }

        taxDeclarationInforDto.getTaxDeclarationIndividualDto().setTaxAgencyInfoDto(taxAgencyInfoDto);

        // Gán danh sách ngành nghề đã lấy được vào TaxDeclarationInforDto
        taxDeclarationInforDto.getTaxDeclarationIndividualDto().setIndividualIndustryDto(individualIndustryDto);

        // Gán các DTO đã lấy được vào TaxDeclarationInforDto
        taxDeclarationInforDto.setTaxDeclarationVatPitLineDto(taxDeclarationVatPitLineDto);
        taxDeclarationInforDto.setTaxDeclarationExciseDto(taxDeclarationExciseDto);
        taxDeclarationInforDto.setTaxDeclarationResourceEnvironmentDto(taxDeclarationResourceEnvironmentDto);
        taxDeclarationInforDto.setTaxDeclarationExpenseDetailDto(taxDeclarationExpenseDetailDto);
        taxDeclarationInforDto.setTaxDeclarationInventoryDetailDto(taxDeclarationInventoryDetailDto);
    }

    public void setAllNecessaryInfo(Integer taxDeclarationIndividualId, TaxDeclarationInforDto taxDeclarationInforDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        TaxAgencyInfo taxAgencyInfo = taxAgencyInfoRepository.findById(taxDeclarationInforDto.getTaxDeclarationIndividualDto().getTaxAgencyInfoId()).orElse(new TaxAgencyInfo());
        TaxAgencyInfoDto taxAgencyInfoDto = modelMapper.map(taxAgencyInfo, TaxAgencyInfoDto.class);

        // NGÀNH NGHỀ KINH DOANH CỦA HỘ KINH DOANH
        List<IndividualIndustry> individualIndustry = individualIndustryRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<IndividualIndustryDto> individualIndustryDto = new ArrayList<>();

        if (individualIndustry != null && !individualIndustry.isEmpty()) {

            for (IndividualIndustry item : individualIndustry) {

                IndividualIndustryDto dto = modelMapper.map(item, IndividualIndustryDto.class);
                setAllNecessaryBusinessIndustryInfo(dto);
                individualIndustryDto.add(dto);
            }
        }

        // A. KÊ KHAI THUẾ GIÁ TRỊ GIA TĂNG (GTGT), THUẾ THU NHẬP CÁ NHÂN (TNCN)
        List<TaxDeclarationVatPitLine> entityListA = taxDeclarationVatPitLineRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationVatPitLineDto> taxDeclarationVatPitLineDto = new ArrayList<>();
        if (!entityListA.isEmpty()) {

            for (TaxDeclarationVatPitLine item : entityListA) {

                TaxDeclarationVatPitLineDto dto = modelMapper.map(item, TaxDeclarationVatPitLineDto.class);
                setAllNecessaryBusinessSectorGroupInfo(dto);
                taxDeclarationVatPitLineDto.add(dto);
            }
        }

        // B. KÊ KHAI THUẾ TIÊU THỤ ĐẶC BIỆT (TTĐB)
        List<TaxDeclarationExcise> entityListB = taxDeclarationExciseRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationExciseDto> taxDeclarationExciseDto = new ArrayList<>();
        if (!entityListB.isEmpty()) {

            for (TaxDeclarationExcise item : entityListB) {
                TaxDeclarationExciseDto dto = modelMapper.map(item, TaxDeclarationExciseDto.class);
                setAllNecessaryInventoryCategorySpecialInfo(dto);
                taxDeclarationExciseDto.add(dto);
            }
        }

        // C. KÊ KHAI THUẾ/PHÍ BẢO VỆ MÔI TRƯỜNG HOẶC THUẾ TÀI NGUYÊN
        List<TaxDeclarationResourceEnvironment> entityListC = taxDeclarationResourceEnvironmentRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationResourceEnvironmentDto> taxDeclarationResourceEnvironmentDto = new ArrayList<>();
        if (!entityListC.isEmpty()) {

            for (TaxDeclarationResourceEnvironment item : entityListC) {

                TaxDeclarationResourceEnvironmentDto dto = modelMapper.map(item, TaxDeclarationResourceEnvironmentDto.class);
                setAllNecessaryEnvironmentFeeInfo(dto);
                taxDeclarationResourceEnvironmentDto.add(dto);
            }
        }

        // Tờ 2

        //
        List<TaxDeclarationInventoryDetail> entityListD = taxDeclarationInventoryDetailRepository.findByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationInventoryDetailDto> taxDeclarationInventoryDetailDto = new ArrayList<>();
        if (!entityListD.isEmpty()) {

            for (TaxDeclarationInventoryDetail item : entityListD) {

                TaxDeclarationInventoryDetailDto dto = modelMapper.map(item, TaxDeclarationInventoryDetailDto.class);
                taxDeclarationInventoryDetailDto.add(dto);
            }
        }

        //
        List<TaxDeclarationExpenseDetail> entityListE = taxDeclarationExpenseDetailRepository.findByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationExpenseDetailDto> taxDeclarationExpenseDetailDto = new ArrayList<>();
        if (!entityListE.isEmpty()) {

            for (TaxDeclarationExpenseDetail item : entityListE) {

                TaxDeclarationExpenseDetailDto dto = modelMapper.map(item, TaxDeclarationExpenseDetailDto.class);
                setAllNecessaryExpenseTypeInfo(dto);
                taxDeclarationExpenseDetailDto.add(dto);
            }
        }

        taxDeclarationInforDto.getTaxDeclarationIndividualDto().setTaxAgencyInfoDto(taxAgencyInfoDto);

        // Gán danh sách ngành nghề đã lấy được vào TaxDeclarationInforDto
        taxDeclarationInforDto.getTaxDeclarationIndividualDto().setIndividualIndustryDto(individualIndustryDto);

        // Gán các DTO đã lấy được vào TaxDeclarationInforDto
        taxDeclarationInforDto.setTaxDeclarationVatPitLineDto(taxDeclarationVatPitLineDto);
        taxDeclarationInforDto.setTaxDeclarationExciseDto(taxDeclarationExciseDto);
        taxDeclarationInforDto.setTaxDeclarationResourceEnvironmentDto(taxDeclarationResourceEnvironmentDto);
        taxDeclarationInforDto.setTaxDeclarationExpenseDetailDto(taxDeclarationExpenseDetailDto);
        taxDeclarationInforDto.setTaxDeclarationInventoryDetailDto(taxDeclarationInventoryDetailDto);
    }

    private IndividualIndustry saveIndividualIndustry(IndividualIndustryDto element, Integer id) {

        IndividualIndustry entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, IndividualIndustry.class);
        }else{

            entity = individualIndustryRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return individualIndustryRepository.saveAndFlush(entity);
    }

    public List<IndividualIndustryDto> processIndividualIndustry(TaxDeclarationIndividualDto paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.getIndividualIndustryDto().stream()
                .map(element -> {
                    IndividualIndustry individualIndustry = saveIndividualIndustry(element, finalEntitySave.getId());
                    element.setId(individualIndustry.getId());
                    return modelMapper.map(individualIndustry, IndividualIndustryDto.class);
                })
                .collect(Collectors.toList());
    }

    private TaxDeclarationVatPitLine saveTaxDeclarationVatPitLine(TaxDeclarationVatPitLineDto element, Integer id) {

        TaxDeclarationVatPitLine entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, TaxDeclarationVatPitLine.class);
        }else{

            entity = taxDeclarationVatPitLineRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return taxDeclarationVatPitLineRepository.saveAndFlush(entity);
    }

    public List<TaxDeclarationVatPitLineDto> processTaxDeclarationVatPitLine(TaxDeclarationInforDto paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.getTaxDeclarationVatPitLineDto().stream()
                .map(element -> {
                    TaxDeclarationVatPitLine taxDeclarationVatPitLine = saveTaxDeclarationVatPitLine(element, finalEntitySave.getId());
                    element.setId(taxDeclarationVatPitLine.getId());
                    return modelMapper.map(taxDeclarationVatPitLine, TaxDeclarationVatPitLineDto.class);
                })
                .collect(Collectors.toList());
    }

    private TaxDeclarationExcise saveTaxDeclarationExcise(TaxDeclarationExciseDto element, Integer id) {

        TaxDeclarationExcise entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, TaxDeclarationExcise.class);
        }else{

            entity = taxDeclarationExciseRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return taxDeclarationExciseRepository.saveAndFlush(entity);
    }

    public List<TaxDeclarationExciseDto> processTaxDeclarationExcise(TaxDeclarationInforDto paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.getTaxDeclarationExciseDto().stream()
                .map(element -> {
                    TaxDeclarationExcise taxDeclarationExcise = saveTaxDeclarationExcise(element, finalEntitySave.getId());
                    element.setId(taxDeclarationExcise.getId());
                    return modelMapper.map(taxDeclarationExcise, TaxDeclarationExciseDto.class);
                })
                .collect(Collectors.toList());
    }

    private TaxDeclarationResourceEnvironment saveTaxDeclarationResourceEnvironment(TaxDeclarationResourceEnvironmentDto element, Integer id) {

        TaxDeclarationResourceEnvironment entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, TaxDeclarationResourceEnvironment.class);
        }else{

            entity = taxDeclarationResourceEnvironmentRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return taxDeclarationResourceEnvironmentRepository.saveAndFlush(entity);
    }

    public List<TaxDeclarationResourceEnvironmentDto> processTaxDeclarationResourceEnvironment(List<TaxDeclarationResourceEnvironmentDto> paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.stream()
                .map(element -> {
                    TaxDeclarationResourceEnvironment taxDeclarationResourceEnvironment = saveTaxDeclarationResourceEnvironment(element, finalEntitySave.getId());
                    element.setId(taxDeclarationResourceEnvironment.getId());
                    return modelMapper.map(taxDeclarationResourceEnvironment, TaxDeclarationResourceEnvironmentDto.class);
                })
                .collect(Collectors.toList());
    }

    private TaxDeclarationExpenseDetail saveTaxDeclarationExpenseDetail(TaxDeclarationExpenseDetailDto element, Integer id) {

        TaxDeclarationExpenseDetail entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, TaxDeclarationExpenseDetail.class);
        }else{

            entity = taxDeclarationExpenseDetailRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return taxDeclarationExpenseDetailRepository.saveAndFlush(entity);
    }

    public List<TaxDeclarationExpenseDetailDto> processTaxDeclarationExpenseDetail(List<TaxDeclarationExpenseDetailDto> paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.stream()
                .map(element -> {
                    TaxDeclarationExpenseDetail taxDeclarationExpenseDetail = saveTaxDeclarationExpenseDetail(element, finalEntitySave.getId());
                    element.setId(taxDeclarationExpenseDetail.getId());
                    return modelMapper.map(taxDeclarationExpenseDetail, TaxDeclarationExpenseDetailDto.class);
                })
                .collect(Collectors.toList());
    }

    private TaxDeclarationInventoryDetail saveTaxDeclarationInventoryDetail(TaxDeclarationInventoryDetailDto element, Integer id) {

        TaxDeclarationInventoryDetail entity = null;

        if(element.getId() == null){

            entity = modelMapper.map(element, TaxDeclarationInventoryDetail.class);
        }else{

            entity = taxDeclarationInventoryDetailRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, entity);
        }
        entity.setTaxDeclarationIndividualId(id);
        return taxDeclarationInventoryDetailRepository.saveAndFlush(entity);
    }

    public List<TaxDeclarationInventoryDetailDto> processTaxDeclarationInventoryDetailDto(List<TaxDeclarationInventoryDetailDto> paramDto, TaxDeclarationIndividual finalEntitySave) {
        return paramDto.stream()
                .map(element -> {
                    TaxDeclarationInventoryDetail taxDeclarationInventoryDetail = saveTaxDeclarationInventoryDetail(element, finalEntitySave.getId());
                    element.setId(taxDeclarationInventoryDetail.getId());
                    return modelMapper.map(taxDeclarationInventoryDetail, TaxDeclarationInventoryDetailDto.class);
                })
                .collect(Collectors.toList());
    }

    public void setAllNecessaryEnvironmentFeeInfo(TaxDeclarationResourceEnvironmentDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Environment_Fee_BY_ID + "/" + dto.getEnvironmentFeeId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        EnvironmentFeeDto result = objectMapper.convertValue(res.getData(), EnvironmentFeeDto.class);

        dto.setEnvironmentFeeDto(result);
    }

    public void setAllNecessaryBusinessSectorGroupInfo(TaxDeclarationVatPitLineDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_GET_Business_Sector_Group_BY_ID + "/" + dto.getBusinessSectorGroupId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        BusinessSectorGroupDto result = objectMapper.convertValue(res.getData(), BusinessSectorGroupDto.class);

        dto.setBusinessSectorGroupDto(result);
    }

    public void setAllNecessaryInventoryCategorySpecialInfo(TaxDeclarationExciseDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Inventory_Category_Special_Tax_BY_ID + "/" + dto.getInventoryCategorySpecialTaxId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        InventoryCategorySpecialTaxDto result = objectMapper.convertValue(res.getData(), InventoryCategorySpecialTaxDto.class);

        dto.setInventoryCategorySpecialTaxDto(result);
    }

    public void setAllNecessaryExpenseTypeInfo(TaxDeclarationExpenseDetailDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Expense_Type_BY_ID + "/" + dto.getExpenseTypeId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        ExpenseTypeDto result = objectMapper.convertValue(res.getData(), ExpenseTypeDto.class);

        dto.setExpenseTypeDto(result);
    }

    public void setAllNecessaryBusinessIndustryInfo(IndividualIndustryDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Tax_Business_Industry_BY_ID + "/" + dto.getTaxBusinessIndustryId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        TaxBusinessIndustryDto result = objectMapper.convertValue(res.getData(), TaxBusinessIndustryDto.class);

        dto.setTaxBusinessIndustryDto(result);
    }

    private List<TaxDeclarationVatPitLineDto> getInitReportSaleServiceType(String taxMonthFrom, String taxMonthTo) {

        List<TaxDeclarationVatPitLineDto> taxDeclarationVatPitLineDto = new ArrayList<>();


        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            CallableStatement callableStatement = connection.prepareCall("{? = call get_revenue_by_business_sector_group(?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL refcursor
            callableStatement.setString(2, taxMonthFrom);
            callableStatement.setString(3, taxMonthTo);

            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                while (rs.next()) {
                    // Map dữ liệu từ ResultSet sang DTO
                    TaxDeclarationVatPitLineInitDto initDto = new TaxDeclarationVatPitLineInitDto();
                    initDto.setBusinessSectorGroupId(rs.getInt("d_business_sector_group_id"));
                    initDto.setPitRevenue(rs.getBigDecimal("pit_revenue"));
                    initDto.setVatRevenue(rs.getBigDecimal("vat_revenue"));

                    // Dùng modelMapper để map sang DTO chính
                    TaxDeclarationVatPitLineDto dto = modelMapper.map(initDto, TaxDeclarationVatPitLineDto.class);
                    dto.setId(null);
                    setAllNecessaryBusinessSectorGroupInfo(dto);

                    taxDeclarationVatPitLineDto.add(dto);
                }
            }

        } catch (Exception e) {
            log.error("Error while calling stored procedure: {}", e.getMessage(), e);
            e.printStackTrace();
        }

        return taxDeclarationVatPitLineDto;
    }

    private List<TaxDeclarationExciseDto> getInitTaxDeclarationExcise(String taxMonthFrom, String taxMonthTo) {

        List<TaxDeclarationExciseDto> taxDeclarationExciseDto = new ArrayList<>();


        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            CallableStatement callableStatement = connection.prepareCall("{? = call get_revenue_by_inventory_category_special_tax(?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL refcursor
            callableStatement.setString(2, taxMonthFrom);
            callableStatement.setString(3, taxMonthTo);

            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                while (rs.next()) {
                    // Map dữ liệu từ ResultSet sang DTO
                    TaxDeclarationExciseDto initDto = new TaxDeclarationExciseDto();
                    initDto.setInventoryCategorySpecialTaxId(rs.getInt("d_inventory_category_special_tax_id"));
                    initDto.setExciseRevenue(rs.getBigDecimal("excise_revenue"));

                    // Dùng modelMapper để map sang DTO chính
                    TaxDeclarationExciseDto dto = modelMapper.map(initDto, TaxDeclarationExciseDto.class);
                    dto.setId(null);
                    setAllNecessaryInventoryCategorySpecialInfo(dto);

                    taxDeclarationExciseDto.add(dto);
                }
            }

        } catch (Exception e) {
            log.error("Error while calling stored procedure: {}", e.getMessage(), e);
            e.printStackTrace();
        }

        return taxDeclarationExciseDto;
    }

    public TaxDeclarationIndividualDto setAllTaxDeclarationIndividualInitInfo(TaxDeclarationIndividualQueryRequest paramRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        TaxDeclarationIndividualDto taxDeclarationIndividualDto = new TaxDeclarationIndividualDto();

        Optional<TaxHouseholdProfile> taxHouseholdProfile = taxHouseholdProfileRepository.findById(0);
        taxDeclarationIndividualDto = taxHouseholdProfile
                .map(profile -> {
                    TaxDeclarationIndividualDto dto = modelMapper.map(profile, TaxDeclarationIndividualDto.class);
                    return dto;
                })
                .orElseThrow(() -> new PosException(
                        messageSource.getMessage("failed", null, LocaleContextHolder.getLocale())
                ));

        GlobalReponse serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ID + "/" + AuditContext.getAuditInfo().getOrgId(), HttpMethod.GET, entityHeader
                , GlobalReponse.class).getBody();
        OrgDto orgDto = modelMapper.map(serviceDto.getData(), OrgDto.class);
        taxDeclarationIndividualDto.setTaxpayerName(orgDto.getName());
        taxDeclarationIndividualDto.setStoreName(orgDto.getName());
        taxDeclarationIndividualDto.setTaxpayerCode(orgDto.getTaxCode());
        taxDeclarationIndividualDto.setTaxpayerAddress(orgDto.getAddress());
        taxDeclarationIndividualDto.setTaxpayerMobile(orgDto.getPhone());
        taxDeclarationIndividualDto.setTaxpayerDistrictName(orgDto.getWards());
        taxDeclarationIndividualDto.setTaxpayerProvinceName(orgDto.getArea());

        TaxAgencyInfo taxAgencyInfo = taxAgencyInfoRepository.findLatestActiveNative().orElse(new TaxAgencyInfo());
        if ("Y".equals(taxAgencyInfo.getTaxAgentShowOnReport())) {

            taxDeclarationIndividualDto.setTaxAgencyInfoId(taxAgencyInfo.getId());
            taxDeclarationIndividualDto.setTaxAgentCode(taxAgencyInfo.getTaxAgentCode());
            taxDeclarationIndividualDto.setTaxAgentName(taxAgencyInfo.getTaxAgentName());
            taxDeclarationIndividualDto.setTaxAgentStaffFullname(taxAgencyInfo.getTaxAgentStaffName());
            taxDeclarationIndividualDto.setTaxAgentPracticeCertificateNo(taxAgencyInfo.getTaxAgentStaffCertNo());
        }

        String sql = "SELECT tax_agent_signer_name FROM d_tax_declaration_individual WHERE is_active = 'Y' ORDER BY d_tax_declaration_individual_id DESC LIMIT 1";
        String lastTaxAgentSignerName =  ((String) entityManager.createNativeQuery(sql)
                .getSingleResult()).toString();
        taxDeclarationIndividualDto.setTaxAgentSignerName(lastTaxAgentSignerName);
        taxDeclarationIndividualDto.setTaxAgentSignDate(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = null;
        Date toDate = null;
        try {

            fromDate = sdf.parse(paramRequest.getTaxMonthFrom());
            toDate = sdf.parse(paramRequest.getTaxMonthTo());
        } catch (Exception e) {

            e.printStackTrace();
        }

        taxDeclarationIndividualDto.setTaxMonthFrom(fromDate);
        taxDeclarationIndividualDto.setTaxMonthTo(toDate);
        taxDeclarationIndividualDto.setTaxPeriodType(paramRequest.getTaxPeriodType());
        taxDeclarationIndividualDto.setTaxQuarter(paramRequest.getTaxQuarter());
        taxDeclarationIndividualDto.setTaxYear(paramRequest.getTaxYear());

        if (paramRequest.getAdditionalSubmission() == null || paramRequest.getAdditionalSubmission() == 0) taxDeclarationIndividualDto.setIsFirstSubmission("Y");
        else taxDeclarationIndividualDto.setIsFirstSubmission("N");

        taxDeclarationIndividualDto.setAdditionalSubmission(paramRequest.getAdditionalSubmission());
        taxDeclarationIndividualDto.setIsBusinessChange("N");
        taxDeclarationIndividualDto.setIsAddressChange("N");
        taxDeclarationIndividualDto.setIsUnregisteredIndividual("N");
        taxDeclarationIndividualDto.setIsNoIdIndividual("N");

        String applicableCircularDefault = configForTenantRepository.findByNameAndTenantId(AppConstant.DefaultTaxDeclarationConfig.TAX_PAY_MT_DEF_TYPE,0).get().getValue();
        String taxPaymentMethodDefaultType = configForTenantRepository.findByNameAndTenantId(AppConstant.DefaultTaxDeclarationConfig.APPLICABLE_CIRCULAR_DEFAULT,0).get().getValue();

        taxDeclarationIndividualDto.setApplicableCircular(applicableCircularDefault);

        Integer taxPaymentMethodId = taxPaymentMethodRepository
                .findByCode(taxPaymentMethodDefaultType)
                .map(TaxPaymentMethod::getId)
                .orElse(0);

        taxDeclarationIndividualDto.setTaxPaymentMethodId(taxPaymentMethodId);

        return taxDeclarationIndividualDto;
    }

}









