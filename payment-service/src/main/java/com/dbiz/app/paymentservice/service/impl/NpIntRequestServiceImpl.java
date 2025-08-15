package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.NpIntRequest;
import com.dbiz.app.paymentservice.repository.NpIntRequestRepository;
import com.dbiz.app.paymentservice.service.NpIntRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.BankDto;
import org.common.dbiz.dto.paymentDto.napas.NpIntRequestDto;
import org.common.dbiz.dto.paymentDto.request.BankNpReqDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.paymentDto.request.NpIntRequestReqDto;
import org.common.dbiz.dto.paymentDto.response.NpIntRequestResDto;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NpIntRequestServiceImpl implements NpIntRequestService {

    private final NpIntRequestRepository npIntRequestRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final EntityManager entityManager;
    private final QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination findBank(BankNpReqDto reqDto) {
        Parameter parameter = getParameter(reqDto);
        ResultSet rs = queryEngine.getRecords("pos.d_bank_np_status_v", parameter, reqDto);
        try {
            List<BankDto> data = new ArrayList<>();
            while (rs.next()) {
                BankDto bank = BankDto.builder()
                        .id(rs.getInt("d_bank_id"))
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .name(rs.getString("name"))
                        .shortName(rs.getString("short_name"))
                        .code(rs.getString("code"))
                        .description(rs.getString("description"))
                        .binCode(rs.getString("bin_code"))
                        .swiftCode(rs.getString("swift_code"))
                        .imageId(rs.getBigDecimal("d_image_id"))
                        .imageUrl(rs.getString("image_url"))
                        .isActive(rs.getString("is_active"))
                        .requestStatus(rs.getString("request_status"))
                        .requestStatusName(rs.getString("request_status_name"))
                        .build();
                data.add(bank);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_bank_np_status_v", parameter, reqDto);
            log.info("Load pagination...");
            return GlobalReponsePagination.builder()
                    .data(data)
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.OK.value())
                    .pageSize(pagination.getPageSize())
                    .currentPage(pagination.getPage())
                    .totalPages(pagination.getTotalPage())
                    .totalItems(pagination.getTotalCount())
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return null;
    }

    private static @NotNull Parameter getParameter(BankNpReqDto reqDto) {
        Parameter parameter = new Parameter();

        parameter.add("code", reqDto.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("name", reqDto.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("short_name", reqDto.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("bin_code", reqDto.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("swift_code", reqDto.getKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        parameter.add("code", reqDto.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("name", reqDto.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("short_name", reqDto.getShortName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("bin_code", reqDto.getBinCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("swift_code", reqDto.getSwiftCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        return parameter;
    }

    @Override
    public GlobalReponsePagination findRequest(NpIntRequestReqDto reqDto) {
        Parameter parameter = new Parameter();
        parameter.add("d_bank_id", reqDto.getBankId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("pos.d_np_int_request_v", parameter, reqDto);
        try {
            List<NpIntRequestResDto> data = new ArrayList<>();
            while (rs.next()) {
                NpIntRequestResDto request = NpIntRequestResDto.builder()
                        .requestStatus(rs.getString("request_status"))
                        .requestStatusName(rs.getString("request_status_name"))
                        .phone(rs.getString("phone"))
                        .bankDto(BankDto.builder()
                                .id(rs.getInt("d_bank_id"))
                                .name(rs.getString("bank_name"))
                                .build())
                        .bankAccountDto(BankAccountDto.builder()
                                .id(rs.getInt("d_bankaccount_id"))
                                .name(rs.getString("account_owner"))
                                .accountNo(rs.getString("account_no"))
                                .build())
                        .posTerminalDto(PosTerminalDto.builder()
                                .id(rs.getInt("d_pos_terminal_id"))
                                .name(rs.getString("pos_terminal_name"))
                                .build())
                        .orgDto(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .build())
                        .build();
                data.add(request);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_np_int_request_v", parameter, reqDto);
            log.info("Load pagination...");
            return GlobalReponsePagination.builder()
                    .data(data)
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.OK.value())
                    .pageSize(pagination.getPageSize())
                    .currentPage(pagination.getPage())
                    .totalPages(pagination.getTotalPage())
                    .totalItems(pagination.getTotalCount())
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return null;
    }

    @Override
    @Transactional
    public GlobalReponse save(NpIntRequestDto dto) {

        log.info("*** NpIntRequest, service; save NpIntRequest ***");
        GlobalReponse response = new GlobalReponse();

        if (dto.getId() != null) {
            Optional<NpIntRequest> entity = npIntRequestRepository.findById(dto.getId());
            if (entity.isEmpty()) {
                throw new PosException("NpIntRequest does not exist!");
            }
            npIntRequestRepository.save(entity.get());
            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());

        } else {
            NpIntRequest entity = modelMapper.map(dto, NpIntRequest.class);
            entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entity.setRequestStatus("PAP");
            npIntRequestRepository.save(entity);
            dto.setId(entity.getId());
            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }

        response.setData(dto);
        return response;
    }
}
