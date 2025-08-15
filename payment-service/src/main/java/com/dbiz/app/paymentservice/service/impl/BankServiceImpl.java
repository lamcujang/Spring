package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.domain.Bank;
import com.dbiz.app.paymentservice.repository.BankRepository;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.paymentDto.BankDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import com.dbiz.app.paymentservice.service.BankService;
import com.dbiz.app.paymentservice.specification.BankSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class BankServiceImpl implements BankService {
    private final MessageSource messageSource;

    private final BankRepository bankRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;
    private StringBuilder sql ;

    private StringBuilder sqlCount;
    @Override
    public GlobalReponsePagination findAll(BankQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        sql =new StringBuilder("select d_bank_id, is_active, name, bin_code, swift_code, code, short_name, image_url, description, d_image_id, d_tenant_id, d_org_id from d_bank_v  where d_tenant_id = :tenantId" );
        sqlCount =new StringBuilder("select count(*) from d_bank_v where d_tenant_id = :tenantId" );

        if(request.getKeyword()!=null)
        {
            sql.append(" and name like  "+"%" + request.getKeyword() + "%");
            sqlCount.append(" and name like  "+"%" + request.getKeyword() + "%");
        }
        if(request.getId()!=null)
        {
            sql.append(" and d_bank_id = "+request.getId());
            sqlCount.append(" and d_bank_id = "+request.getId());
        }

        BigInteger totalItems = (BigInteger) entityManager.createNativeQuery(sqlCount.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems.longValue() / request.getPageSize());


        List<Map<String, Object>> resultDto = entityManager.createNativeQuery(sql.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .setFirstResult(request.getPage() *request.getPageSize()) // Tính vị trí bắt đầu
                .setMaxResults(request.getPageSize())
                .getResultList();

        Page<Map<String, Object>> page =  new PageImpl<>(resultDto, PageRequest.of(request.getPage(), request.getPageSize()), totalItems.longValue());


        List<BankDto>resultData = new ArrayList<>();
        for (Map<String, Object> row : resultDto) {
            BankDto item = BankDto.builder()
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
            resultData.add(item);
        }

//        Specification<Bank> spec = BankSpecification.getSpecification(request);
//        Page<Bank>banks = bankRepository.findAll(spec, pageable);
//        List<BankDto>bankDtoList = banks.getContent().stream().map(item->modelMapper.map(item, BankDto.class)).collect(Collectors.toList());

        response.setData(resultData);
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPage(page.getNumber());
        response.setTotalItems(page.getTotalElements());
        response.setMessage("fetch bank success");
        return response;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(BankDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
