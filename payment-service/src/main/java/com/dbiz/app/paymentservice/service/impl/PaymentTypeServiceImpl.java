package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.domain.PaymentType;
import com.dbiz.app.paymentservice.repository.PaymentTypeRepository;
import com.dbiz.app.paymentservice.service.PaymentTypeService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentTypeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final PaymentTypeRepository paymentTypeRepository;
    private final MessageSource messageSource;

    /**
     * @return
     */
    @Override
    public GlobalReponse getAllPaymentTypes() {
        StringBuilder sql = new StringBuilder("    select d_payment_type_id, d_tenant_id, d_org_id, code, payment_name, payment_type_name, payment_type_code,\n" +
                "           image_url, is_enable from d_payment_type_v where d_tenant_id = ? ");
        log.info("*** Payment Type List, service; fetch all Payment Type *");
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString()).
                setParameter(1, AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
        List<PaymentTypeDto> resultReponse = new ArrayList<>();
        for (Map<String, Object> result : results) {
            PaymentTypeDto item = PaymentTypeDto.builder()
                    .id(ParseHelper.INT.parse(result.get("d_payment_type_id")))
                    .orgId(ParseHelper.INT.parse(result.get("d_org_id")))
                    .code(ParseHelper.STRING.parse(result.get("code")))
                    .paymentName(ParseHelper.STRING.parse(result.get("payment_name")))
                    .paymentType(ParseHelper.STRING.parse(result.get("payment_type_code")))
                    .paymentTypeName(ParseHelper.STRING.parse(result.get("payment_type_name")))
                    .paymentTypeCode(ParseHelper.STRING.parse(result.get("payment_type_code")))
                    .imageUrl(ParseHelper.STRING.parse(result.get("image_url")))
                    .isEnable(ParseHelper.STRING.parse(result.get("is_enable")))
                    .build();
            resultReponse.add(item);
        }
        return GlobalReponse.builder()
                .data(resultReponse)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value()).build();
    }

    /**
     * @param paymentTypeDto
     * @return
     */
    @Override
    public GlobalReponse save(List<PaymentTypeDto> paymentTypeDto) {
        log.info("*** Payment Type, service; save Payment Type *");
        paymentTypeDto.stream().forEach(item -> {
            PaymentType paymentType = paymentTypeRepository.findById(item.getId()).orElse(null);
            if (paymentType != null) {
                modelMapper.map(item, paymentType);
                paymentType.setTenantId(AuditContext.getAuditInfo().getTenantId());
                paymentType.setOrgId(0);
                paymentTypeRepository.save(paymentType);

            }
        });
        return GlobalReponse.builder()
                .data(paymentTypeDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value()).build();
    }
}
