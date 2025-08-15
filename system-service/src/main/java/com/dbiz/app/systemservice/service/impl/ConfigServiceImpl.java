package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.constant.AppConstant;
import com.dbiz.app.systemservice.helper.EncoderUrl;
import com.dbiz.app.systemservice.helper.NotifyHelper;
import com.dbiz.app.systemservice.repository.ReferenceListRepository;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.systemservice.domain.Config;
import com.dbiz.app.systemservice.domain.GetEmenuUrlV;
import com.dbiz.app.systemservice.repository.GetEmenuUrlVRepository;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import org.common.dbiz.dto.orderDto.response.PosOrderLineResDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.model.EMenuDecoded;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import com.dbiz.app.systemservice.repository.ConfigRepository;
import com.dbiz.app.systemservice.service.ConfigService;
import com.dbiz.app.systemservice.specification.ConfigSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ConfigDto;
import org.common.dbiz.request.systemRequest.ConfigQueryRequest;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository entityRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private final GetEmenuUrlVRepository getEmenuUrlVRepository;

    private final NotifyHelper notify;

    private final ReferenceListRepository referenceListRepository;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final EntityManager entityManager;

    @Override
    public GlobalReponsePagination findAll(ConfigQueryRequest request) {
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Config> spec = ConfigSpecification.getEntitySpecification(request);
        // lay ra id giao dich  va them dieu kien vao where

        Page<Config> configs = entityRepository.findAll(spec, pageable);
        List<ConfigDto> listData = new ArrayList<>();
        for (Config item : configs.getContent()) {

            ConfigDto itemDto = modelMapper.map(item, ConfigDto.class);
            listData.add(itemDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage("Config fetched successfully");
        globalReponsePagination.setTotalPages(configs.getTotalPages());
        globalReponsePagination.setPageSize(configs.getSize());
        globalReponsePagination.setCurrentPage(configs.getNumber());
        globalReponsePagination.setTotalItems(configs.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse save(ConfigDto Dto) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findValueByName(String name) {
        log.info("*** System config, service; fetch System config by name *");
        dataSourceContextHolder.setCurrentTenantId(null);
        String value = entityRepository.findValueByNameAndTenantId(name,0);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        GlobalReponse response = new GlobalReponse();
        response.setData(value);
        response.setMessage("entity fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    /**
     * @param
     * @return
     */
    @Override
    public GlobalReponsePagination getEMenuConfig(EMenuGetUrlQueryRequest request)
    {
        log.info("*** System config, service; fetch EMenu config *");
        dataSourceContextHolder.setCurrentTenantId(null);
        String linkeEmenu = entityRepository.findValueByNameAndTenantId("D_EMENU_URL",0);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        Specification<GetEmenuUrlV> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId()));
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), request.getOrgId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getPosTerminalId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("posTerminalId"), request.getPosTerminalId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getTableId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("tableId"), request.getTableId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getFloorId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("floorId"), request.getFloorId()));
        if(request.getKeyWords()!= null)
        {
            spec = spec.and((root, query, criteriaBuilder) -> {
                if (request.getKeyWords() == null)
                {
                    return criteriaBuilder.conjunction();
                }
                String keyWordsLower = "%" + request.getKeyWords().toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tableName")), keyWordsLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tableNo")), keyWordsLower)
                );
            });
        }
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<GetEmenuUrlV> resultQuery = getEmenuUrlVRepository.findAll(spec, pageable);



        List<GetEmenuUrlV> eMenuUrl = resultQuery.getContent().stream().map(item -> {
            item.setEmenuUrl(linkeEmenu+AuditContext.getAuditInfo().getMainTenantId()+"/"+item.getEmenuUrl());
            return item;
        }).collect(Collectors.toList());



        return GlobalReponsePagination.builder()
                .data(eMenuUrl)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .currentPage(resultQuery.getNumber())
                .totalItems(resultQuery.getTotalElements())
                .totalPages(resultQuery.getTotalPages())
                .pageSize(resultQuery.getSize())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponsePagination getEMenuConfigV2(EMenuGetUrlQueryRequest request) {

        log.info("*** System config, service; fetch EMenu config *");
        dataSourceContextHolder.setCurrentTenantId(null);
        String linkeEmenu = entityRepository.findValueByNameAndTenantId("D_EMENU_URL",0);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        Specification<GetEmenuUrlV> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId()));
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), request.getOrgId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getPosTerminalId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("posTerminalId"), request.getPosTerminalId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getTableId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("tableId"), request.getTableId()));
        spec = spec.and((root, query, criteriaBuilder) -> request.getFloorId() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("floorId"), request.getFloorId()));
        if(request.getKeyWords()!= null)
        {
            spec = spec.and((root, query, criteriaBuilder) -> {
                if (request.getKeyWords() == null)
                {
                    return criteriaBuilder.conjunction();
                }
                String keyWordsLower = "%" + request.getKeyWords().toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tableName")), keyWordsLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tableNo")), keyWordsLower)
                );
            });
        }
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<GetEmenuUrlV> resultQuery = getEmenuUrlVRepository.findAll(spec, pageable);



        List<GetEmenuUrlV> eMenuUrl = resultQuery.getContent().stream().map(item -> {
            String urlEncode = EncoderUrl.encodeEmenu(AuditContext.getAuditInfo().getMainTenantId(),
                    item.getOrgId(),
                    item.getTableId(),
                    item.getFloorId(),
                    item.getPosTerminalId(),
                    item.getTableNo(),item.getFloorNo(),
                    item.getOrgName(),
                    item.getAddress(),
                    item.getPriceListId());

            item.setEmenuUrl(linkeEmenu+urlEncode);
            return item;
        }).collect(Collectors.toList());



        return GlobalReponsePagination.builder()
                .data(eMenuUrl)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .currentPage(resultQuery.getNumber())
                .totalItems(resultQuery.getTotalElements())
                .totalPages(resultQuery.getTotalPages())
                .pageSize(resultQuery.getSize())
                .errors("")
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse sendNotify(SendNotification request) {
        log.info("*** System config, service; send notify *");

        String result = notify.notifyFirebase(request.getDeviceToken(), request.getStatus(), request.getTitle(), request.getBody(), null,1, "router",request.getType(),request.getSpeak());

        return GlobalReponse.builder()
                .data(result).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @param name
     * @return
     */
    @Override
    public GlobalReponse getImageAsset(String name) {
        return null;
    }

    @Override
    public GlobalReponse getParamEmenu(String param) {
        EMenuDecoded decode = EncoderUrl.decodeEmenu(param);
        return GlobalReponse.builder()
                .data(decode)
                .message(messageSource.getMessage("successfully",null,LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getNapasConfig(NapasConfigReqDto req) {

//        dataSourceContextHolder.setCurrentTenantId(null);
        try {

            List<Map<String, Object>> results = null;
            String sql = null;

            if(req.getType().equals("INFO")){
                sql = "SELECT " +
                        " name , value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name in (:param1,:param2) " ;

                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                results = entityManager.createNativeQuery(sql)
                        .setParameter("param1", AppConstant.Napas.D_NAPAS_CODE)
                        .setParameter("param2", AppConstant.Napas.D_NAPAS_MASTER_MERCHANT)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
            }else{
                sql = "SELECT " +
                        " name , value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name like '%NAPAS%' " ;


                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                results = entityManager.createNativeQuery(sql)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
            }


            NapasConfigResDto dto = new NapasConfigResDto();

            String url = "";
            String urlPre = "";
            String uri = "";
            String uriInvestigation = "";
            String uriToken = "";

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);


                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_URL_PRE))
                    urlPre = ParseHelper.STRING.parse(row.get("value"));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_URL_INVESTIGATION))
                    uriInvestigation = ParseHelper.STRING.parse(row.get("value"));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_URL_GETTOKEN))
                    uriToken  = ParseHelper.STRING.parse(row.get("value"));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_CRD_CLIENT_ID))
                    dto.setClientId(ParseHelper.STRING.parse(row.get("value")));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_CRD_CLIENT_SECRET))
                    dto.setClientSecret(ParseHelper.STRING.parse(row.get("value")));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_GRANT_TYPE))
                    dto.setGrantType(ParseHelper.STRING.parse(row.get("value")));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_MASTER_MERCHANT))
                    dto.setMasterMerchantCode(ParseHelper.STRING.parse(row.get("value")));

                if (ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_CODE))
                    dto.setNapasCode(ParseHelper.STRING.parse(row.get("value")));
            }

            if(!req.getType().equals("INFO")) {
                uri = req.getType().equals("INVESTIGATION") ? uriInvestigation : uriToken;
                url = urlPre + uri;
                dto.setUrl(url);
            }

            return GlobalReponse.builder()
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.OK.value())
                    .data(dto)
                    .build();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }




}
