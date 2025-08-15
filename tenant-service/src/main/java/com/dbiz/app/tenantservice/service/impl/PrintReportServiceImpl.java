package com.dbiz.app.tenantservice.service.impl;


import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.PrintReport;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.PrintReportRepository;
import com.dbiz.app.tenantservice.service.PrintReportService;
import com.dbiz.app.tenantservice.specification.PrintReportSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.PrintReportDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PrintReportServiceImpl implements PrintReportService {

    private final RequestParamsUtils requestParamsUtils;
    private final PrintReportRepository printReportRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(PrintReportQueryRequest paramRequest) {
        log.info("*** PrintReportDTO List, service; fetch all PrintReport *");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<PrintReport> spec = PrintReportSpecification.getPrintReportSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PrintReport> PrintReports = printReportRepository.findAll( spec,pageable);
        List<PrintReportDto> listData = new ArrayList<>();
        for(PrintReport item : PrintReports.getContent()){
            PrintReportDto dto= modelMapper.map(item,PrintReportDto.class);
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(PrintReports.getNumber());
        response.setPageSize(PrintReports.getSize());
        response.setTotalPages(PrintReports.getTotalPages());
        response.setTotalItems(PrintReports.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponsePagination findAllByTenant(PrintReportQueryRequest paramRequest) {
        log.info("*** PrintReportDTO List, service; fetch all PrintReport *");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<PrintReport> spec = PrintReportSpecification.getPrintReportSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PrintReport> printReports = printReportRepository.findAll( spec,pageable);
        List<Map<String, Object>> listData = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (PrintReport item : printReports.getContent()) {

            try {

                // Chuyển reportSource (chuỗi JSON) thành Map
                Map<String, Object> dataMap = mapper.readValue(item.getReportSource(), new TypeReference<Map<String, Object>>() {});

                // Thêm field "option" từ reportType
                dataMap.put("option", item.getReportType());

                listData.add(dataMap);
            } catch (JsonProcessingException e) {

                log.error("Lỗi parse JSON từ reportSource: {}", item.getId(), e);
            }
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(printReports.getNumber());
        response.setPageSize(printReports.getSize());
        response.setTotalPages(printReports.getTotalPages());
        response.setTotalItems(printReports.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(PrintReportDto Dto) {

        GlobalReponse response = new GlobalReponse();
        PrintReport PrintReportSave = modelMapper.map(Dto,PrintReport.class);

        if(PrintReportSave.getId() !=null && PrintReportSave.getId() > 0) // update
        {
            PrintReportSave = this.printReportRepository.findById(Dto.getId()).orElseThrow(()->
                    new ObjectNotFoundException(messageSource.getMessage("print_report_notFound",null, LocaleContextHolder.getLocale())));

            modelMapper.map(Dto,PrintReportSave);
            this.printReportRepository.save(PrintReportSave);
            response.setMessage(messageSource.getMessage("print_report_update",null, LocaleContextHolder.getLocale()));
        }else
        {
            int count = this.printReportRepository.countByTenantId(AuditContext.getAuditInfo().getTenantId());
            if(count <= 0){
                PrintReportSave.setIsDefault("Y");
            }
            PrintReportSave = this.printReportRepository.save(PrintReportSave);
            response.setMessage(messageSource.getMessage("print_report_create",null, LocaleContextHolder.getLocale()));

        }

        response.setData(modelMapper.map(PrintReportSave, PrintReportDto.class));
        response.setStatus(HttpStatus.OK.value());
        return response;

    }

    @Override
    public GlobalReponse saveAll(List<Map<String, Object>> rawList) {

        GlobalReponse response = new GlobalReponse();
        ObjectMapper mapper = new ObjectMapper();
        List<PrintReportDto> dtoList = new ArrayList<>();

        for (Map<String, Object> item : rawList) {

            PrintReportDto dto = new PrintReportDto();

            try {

                // Chuyển object JSON thành chuỗi JSON
                String jsonString = mapper.writeValueAsString(item);
                dto.setReportSource(jsonString);
            } catch (Exception e) {

                // log hoặc xử lý lỗi JSON
                e.printStackTrace();
            }

            // Gán giá trị option
            Object option = item.get("option");
            if (option != null) {

                dto.setReportType(option.toString());
            }

            PrintReport printReportSave = this.printReportRepository.findByType(dto.getReportType()).orElse(new PrintReport());;

            printReportSave.setReportSource(dto.getReportSource());
            printReportSave.setReportType(dto.getReportType());
            printReportSave.setIsActive("Y");
            printReportSave.setIsDefault("Y");
            printReportSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            this.printReportRepository.save(printReportSave);

            response.setMessage(messageSource.getMessage("print_report_update",null, LocaleContextHolder.getLocale()));

            dtoList.add(dto);
        }

        response.setData(dtoList);
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        Optional<PrintReport> entityDelete = this.printReportRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("print_report_notFound",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.printReportRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("print_report_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** WarehouseDto, service; fetch warehouse by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.printReportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("print_report_notFound",null, LocaleContextHolder.getLocale()))), PrintReportDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }
}
