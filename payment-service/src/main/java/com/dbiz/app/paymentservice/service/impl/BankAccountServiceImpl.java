package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.BankAccount;
import com.dbiz.app.paymentservice.helper.BankAccountMapper;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.paymentDto.BankDto;
import org.common.dbiz.dto.paymentDto.request.BankAccountReqDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import com.dbiz.app.paymentservice.repository.BankAccountRepository;
import com.dbiz.app.paymentservice.repository.BankRepository;
import com.dbiz.app.paymentservice.service.BankAccountService;
import com.dbiz.app.paymentservice.specification.BankAccountSpecification;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class BankAccountServiceImpl implements BankAccountService {

    private final MessageSource messageSource;

    private final BankAccountRepository bankAccountRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final RestTemplate restTemplate;

    private final BankRepository bankRepository;

    private final BankAccountMapper bankAccountMapper;

    private final EntityManager entityManager;

    private final QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination findAll(BankAccountQueryRequest request) {
        log.info("*** BankAccount List, service; fetch all BankAccount ***");
        GlobalReponsePagination response = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        if (request.getSwiftCode() != null) {
            String sqlGetBank = "select d_bank_id from d_bank where swift_code = :swiftCode ";
            List<Map<String, Object>> result = entityManager.createNativeQuery(sqlGetBank)
                    .setParameter("swiftCode", request.getSwiftCode())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .setMaxResults(request.getPageSize())
                    .getResultList();
            for (Map<String, Object> row : result) {
                request.setBankId(ParseHelper.INT.parse(row.get("d_bank_id")));
            }
        }
        Specification<BankAccount> specification = BankAccountSpecification.getSpecification(request);

        if (request.getPosTerminalId() != null && request.getOrgId() != null) {
            // xu ly man cashier neu org co quan ly ptm thi lay tai khoan ngan hang cua ptm
            StringBuilder sqlCheckOrg = new StringBuilder("select is_pos_mng,d_pricelist_id,d_org_id from d_org where d_org_id = :orgId ");
            List<Tuple> resultCheckOrg = entityManager.createNativeQuery(sqlCheckOrg.toString(), Tuple.class)
                    .setParameter("orgId", request.getOrgId())
                    .setMaxResults(1)
                    .getResultList();
            if (resultCheckOrg.get(0).get("is_pos_mng").equals("Y")) {
                Integer[] ids = new Integer[2];
                StringBuilder sqlGetBank = new StringBuilder("select d_bank_account_visa_id,d_bank_account_cash_id,d_bank_account_id from d_pos_terminal where d_pos_terminal_id = :posterminalId");
                List<Tuple> result = entityManager.createNativeQuery(sqlGetBank.toString(), Tuple.class)
                        .setParameter("posterminalId", request.getPosTerminalId())
                        .setMaxResults(1)
                        .getResultList();
                for (Tuple row : result) {
                    ids[0] = ParseHelper.INT.parse(row.get("d_bank_account_id"));
                    ids[1] = ParseHelper.INT.parse(row.get("d_bank_account_visa_id"));
                }
                specification = specification.and(BankAccountSpecification.equalIds(ids));
            }
        }

        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(specification, pageable);
        List<BankAccountDto> bankAccountDtos = bankAccounts.getContent().stream().map(item -> {
            BankAccountDto dto = modelMapper.map(item, BankAccountDto.class);
            dto.setBankName(bankRepository.findById(item.getBankId()).get().getName());
            BankDto bank = new BankDto();
            StringBuilder sql = new StringBuilder("select d_bank_id, is_active, name, bin_code, swift_code, code, short_name, image_url, description, d_image_id, d_tenant_id, d_org_id from d_bank_v  where d_tenant_id = :tenantId and d_bank_id = :bankId ");
            List<Map<String, Object>> resultDto = entityManager.createNativeQuery(sql.toString())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("bankId", item.getBankId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .setMaxResults(request.getPageSize())
                    .getResultList();
            for (Map<String, Object> row : resultDto) {

                bank = BankDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_bank_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .binCode(ParseHelper.STRING.parse(row.get("bin_code")))
                        .swiftCode(ParseHelper.STRING.parse(row.get("swift_code")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .shortName(ParseHelper.STRING.parse(row.get("short_name")))
                        .imageId(ParseHelper.BIGDECIMAL.parse(row.get("d_image_id")))
                        .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                        .description(ParseHelper.STRING.parse(row.get("description")))
                        .imageUrl(ParseHelper.STRING.parse(row.get("image_url")))
                        .build();
                dto.setImageUrlBank(ParseHelper.STRING.parse(row.get("image_url")));
                dto.setNameBank(ParseHelper.STRING.parse(row.get("name")));
                dto.setCodeBank(ParseHelper.STRING.parse(row.get("code")));
            }
            dto.setBank(bank);
            return dto;
        }).collect(Collectors.toList());
        response.setData(bankAccountDtos);
        response.setPageSize(bankAccounts.getSize());
        response.setTotalPages(bankAccounts.getTotalPages());
        response.setCurrentPage(bankAccounts.getNumber());
        response.setTotalItems(bankAccounts.getTotalElements());
        response.setMessage("Fetch All Success !!!");
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponsePagination findByBank(BankAccountReqDto reqDto) {
        Parameter parameter = new Parameter();
        parameter.add("d_bank_id", reqDto.getBankId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("pos.d_bankaccount", parameter, reqDto);
        try {
            List<BankAccountDto> data = new ArrayList<>();
            while (rs.next()) {
                BankAccountDto acc = BankAccountDto.builder()
                        .id(rs.getInt("d_bank_id"))
                        .accountNo(rs.getString("account_no"))
                        .name(rs.getString("name"))
                        .isDefault(rs.getString("is_default"))
                        .bankAccountType(rs.getString("bankaccount_type"))
                        .isActive(rs.getString("is_active"))
                        .bank(BankDto.builder()
                                .id(rs.getInt("d_bank_id"))
                                .build())
                        .build();
                data.add(acc);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_bankaccount", parameter, reqDto);
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
    public GlobalReponse findById(Integer integer) {
        log.info("*** BankAccount, service; fetch BankAccount by id ***");
        GlobalReponse reponse = new GlobalReponse();
        BankAccount bankAccount = bankAccountRepository.findById(integer).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("bankAccount.notFound", null, LocaleContextHolder.getLocale())));
        BankAccountDto bankAccountDto = modelMapper.map(bankAccount, BankAccountDto.class);
        bankAccountDto.setBank(modelMapper.map(bankRepository.findById(bankAccount.getBankId()).get(), BankDto.class));
        reponse.setData(bankAccountDto);
        reponse.setMessage("Success");
        reponse.setStatus(HttpStatus.OK.value());
        reponse.setErrors("");
        return reponse;
    }

    @Override
    public GlobalReponse save(BankAccountDto entity) {
        log.info("*** BankAccount, service; save BankAccount ***");
        GlobalReponse reponse = new GlobalReponse();
        BankAccount bankAccount = null;
        if (entity.getId() != null) {
            if (entity.getIsDefault().equals("Y")) {
                BankAccount bankCheckDefault = bankAccountRepository.findByIsDefault("Y");
                if (bankCheckDefault != null)
                    throw new PosException(messageSource.getMessage("bankAccount.default.exists", null, LocaleContextHolder.getLocale()));
            }

            bankAccount = bankAccountRepository.findById(entity.getId()).get();
            bankAccount = bankAccountMapper.updateEntity(entity, bankAccount);
            bankAccountRepository.save(bankAccount);

            BankAccountDto bankAccountDto = bankAccountMapper.toDto(bankAccount);
            bankAccountDto.setBankName(entity.getBankName());
            bankAccountDto.setNameBank(entity.getBankName());

            reponse.setData(bankAccountDto);
            reponse.setMessage("Update success!!!");
            reponse.setStatus(HttpStatus.OK.value());
        } else {
            if (entity.getIsDefault().equals("Y")) {
                BankAccount bankCheckDefault = bankAccountRepository.findByIsDefault("Y");
                if (bankCheckDefault != null)
                    throw new PosException(messageSource.getMessage("bankAccount.default.exists", null, LocaleContextHolder.getLocale()));
            }
            BankAccount entitySave = bankAccountMapper.toEntity(entity);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            bankAccount = bankAccountRepository.save(entitySave);

            BankAccountDto bankAccountDto = bankAccountMapper.toDto(bankAccount);
            bankAccountDto.setBankName(entity.getBankName());
            bankAccountDto.setNameBank(entity.getBankName());

            reponse.setData(bankAccountDto);
            reponse.setMessage("Save Success!!!");
            reponse.setStatus(HttpStatus.CREATED.value());
        }


        if(entity.getPosTerminalId() != null)
        {
            PosTerminalDto posTerminalDto = new PosTerminalDto();
            posTerminalDto.setId(entity.getPosTerminalId());
            if (entity.getBankAccountType().equals("CHE")) {
                posTerminalDto.setBankAccountId(bankAccount.getId());
            } else if (entity.getBankAccountType().equals("CAS")) {
                posTerminalDto.setBankAccountCashId(bankAccount.getId());
            } else if (entity.getBankAccountType().equals("VIS")) {
                posTerminalDto.setBankAccountVisaId(bankAccount.getId());
            }
            if (posTerminalDto.getId() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());


                HttpEntity<PosTerminalDto> requestEntity = new HttpEntity<>(posTerminalDto, headers);
                //Response from Invoice Service
                GlobalReponse responsePosTerminal = this.restTemplate
                        .postForEntity(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_TERMINAL_SAVE_URL,
                                requestEntity,
                                GlobalReponse.class)
                        .getBody();
                if (responsePosTerminal.getStatus().intValue() != HttpStatus.OK.value()
                        && responsePosTerminal.getStatus().intValue() != HttpStatus.CREATED.value()) {
                    throw new RuntimeException(responsePosTerminal.getMessage());
                }
            }
        }
        reponse.setErrors("");
        return reponse;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        log.info("*** BankAccount, service; delete BankAccount by id ***");
        GlobalReponse reponse = new GlobalReponse();
        BankAccount bankAccount = bankAccountRepository.findById(integer).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("bankAccount.notFound", null, LocaleContextHolder.getLocale())));
        bankAccountRepository.delete(bankAccount);
        reponse.setData(null);
//        reponse.setMessage(messageSource.getMessage("bankAccount.deleted", null, LocaleContextHolder.getLocale()));
        reponse.setMessage("Success");
        reponse.setStatus(HttpStatus.OK.value());
        reponse.setErrors("");
        return reponse;
    }
}
