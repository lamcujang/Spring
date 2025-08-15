package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.Org;
import com.dbiz.app.tenantservice.domain.PosTerminal;
import com.dbiz.app.tenantservice.domain.view.PosTerminalV;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.OrgRepository;
import com.dbiz.app.tenantservice.repository.PosTerminalRepository;
import com.dbiz.app.tenantservice.repository.PosTerminalVRepository;
import com.dbiz.app.tenantservice.service.PosTerminalService;
import com.dbiz.app.tenantservice.specification.PosTerminalVSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.PosTerminalOrgAccessIntDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosPaymentDto;
import org.common.dbiz.dto.productDto.PriceListIntDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.PosTerminalVDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ForbiddenException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.PosTerminalIntDto;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PosTerminalServiceImpl implements PosTerminalService {

    private final PosTerminalRepository posTerminalRepository;

    private final PosTerminalVRepository posTerminalVRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final OrgRepository orgRepository;

    private final RestTemplate restTemplate;

    private final EntityManager entityManager;

    @Override
    public GlobalReponsePagination findAll(PosTerminalQueryRequest request) {

        log.info("*** Pos Terminal, service; fetch all Pos Terminal *");

        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<PosTerminalV> spec = PosTerminalVSpecification.getSpecification(request);
        Page<PosTerminalV> entityList = posTerminalVRepository.findAll(spec, pageable);
        List<PosTerminalVDto> posTerminalDto = new ArrayList<>();
        if(entityList !=  null && entityList.getContent().size() > 0){
            entityList.getContent().forEach(posTerminal -> {
                posTerminalDto.add(modelMapper.map(posTerminal, PosTerminalVDto.class));
            });
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage(messageSource.getMessage("fetchAllPosTerminal", null, LocaleContextHolder.getLocale()));
        response.setData(posTerminalDto);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(PosTerminalDto posTerminalDto) {
        log.info("*** Pos terminal , service; save POS Terminal ***");
        GlobalReponse response = new GlobalReponse();
        try{
            PosTerminal terminal = null;
            if(posTerminalDto.getId() == null){
                terminal =  posTerminalRepository.save(modelMapper.map(posTerminalDto, PosTerminal.class));
                response.setMessage(messageSource.getMessage("updatePosTerminal", null, LocaleContextHolder.getLocale()));
            }else{
                terminal = posTerminalRepository.findById(posTerminalDto.getId()).orElseThrow(()-> new ObjectNotFoundException(messageSource.getMessage("notFoundPosTerminal", null, LocaleContextHolder.getLocale())));
                modelMapper.map(posTerminalDto, terminal);

                terminal = posTerminalRepository.save(terminal);
                response.setMessage(messageSource.getMessage("createPosTerminal", null, LocaleContextHolder.getLocale()));
            }
            response.setData(modelMapper.map(terminal, PosTerminalDto.class));
            response.setStatus(posTerminalDto.getId() == null ?  HttpStatus.OK.value() : HttpStatus.CREATED.value());
            return response;
        }catch(Exception e){
            e.printStackTrace();
            throw new ForbiddenException("Error while persisting POS Terminal object");
        }

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Pos Terminal, service; delete Pos Terminal by id *");
        try{
            GlobalReponse response = new GlobalReponse();

            PosTerminal terminal = posTerminalRepository.findById(id).orElseThrow(()-> new PosException(messageSource.getMessage("notFoundPosTerminal", null, LocaleContextHolder.getLocale())));
            posTerminalRepository.deleteById(id);

            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());

            return response;
        }catch (Exception e) {
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public GlobalReponse findById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        log.info("*** Pos Terminal, service; fetch Pos Terminal by id *");
        PosTerminal terminal = posTerminalRepository.findById(id).orElse(null);
        PosTerminalDto posTerminalDto = null;
        if(terminal != null){
            posTerminalDto = modelMapper.map(terminal, PosTerminalDto.class);
            response.setMessage(messageSource.getMessage("fetchPosTerminal", null, LocaleContextHolder.getLocale()));
        }else{
            response.setMessage(messageSource.getMessage("notFoundPosTerminal",null, LocaleContextHolder.getLocale()));
        }
        response.setData(posTerminalDto);
        response.setStatus(HttpStatus.OK.value());
        return response;

    }

    @Override
    public GlobalReponse saveInt(PosTerminalIntDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        log.info("*** Pos Terminal, service; integration Pos Terminal *");

        dto.getListPosterminalDto().forEach(item -> {
            PosTerminal entity = null;
            Org orgErp = null;
            Integer warehouseId = null;
            Integer pricelistId = null;
            Integer userId = null;
               if(dto.getType().equals(AppConstant.ERP_PLATFORM_IDEMPIERE))
               {
                   orgErp = orgRepository.findByErpOrgId(item.getOrgId()).orElseThrow(()-> new PosException(messageSource.getMessage("not_integration_org",null, LocaleContextHolder.getLocale())));
                   String sqlWarehouse = "select d_warehouse_id from d_warehouse where erp_warehouse_id = :erpWarehouseId ";
                   List<Integer> warehouseIds = entityManager.createNativeQuery(sqlWarehouse)
                           .setParameter("erpWarehouseId", item.getWarehouseId())
                           .getResultList();
                   if(warehouseIds.isEmpty())
                       throw new PosException(messageSource.getMessage("not_integration_warehouse",null, LocaleContextHolder.getLocale()));
                   warehouseId =new BigDecimal(String.valueOf(warehouseIds.get(0))).intValue();

                   String sqlPricelist = "select d_pricelist_id from d_pricelist where erp_pricelist_id = :erpPricelistId ";
                   List<Integer> pricelistIds = entityManager.createNativeQuery(sqlPricelist)
                           .setParameter("erpPricelistId", item.getPriceListId())
                           .getResultList();
                   if(pricelistIds.isEmpty()) {
                       HttpHeaders headersPost = new HttpHeaders();
                       headersPost.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                       headersPost.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                       headersPost.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                       headersPost.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                       headersPost.set("userId", AuditContext.getAuditInfo().getUserId().toString());
                       headersPost.set("Accept-Language", LocaleContextHolder.getLocale().toString());
                       headersPost.setContentType(MediaType.APPLICATION_JSON);
                       HttpEntity<PriceListIntDto> requestEntity = new HttpEntity<>(item.getPriceListIntDto(), headersPost);

                       GlobalReponse exresponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_URL_SAVE_PRICELIST  ,requestEntity, GlobalReponse.class) ;
                       pricelistIds.add((Integer) exresponse.getData());
                   }
                   pricelistId =new BigDecimal(String.valueOf(pricelistIds.get(0))).intValue();

                   String sqlUser = "select d_user_id from d_user where  erp_user_id = :erpUserId  ";
                   List<Integer> userIds = entityManager.createNativeQuery(sqlUser)
                           .setParameter("erpUserId", item.getUserId())
                           .getResultList();
                   if(userIds.isEmpty())
                       throw new PosException(messageSource.getMessage("not_integration_user",null, LocaleContextHolder.getLocale()));
                   userId =new BigDecimal(String.valueOf(userIds.get(0))).intValue();
                   entity = posTerminalRepository.findByErpPosId(item.getErpPosId()).orElse(null);
               }
               else if (dto.getType().equals(AppConstant.ERP_PLATFORM_ERPNEXT))
               {
                   orgErp = orgRepository.findByErpOrgName(item.getOrgName()).orElseThrow(()-> new PosException(messageSource.getMessage("not_integration_org",null, LocaleContextHolder.getLocale())));
                   String sqlWarehouse = "select d_warehouse_id from d_warehouse where  erp_warehouse_name = :erpWarehouseName  ";
                   List<Integer> warehouseIds = entityManager.createNativeQuery(sqlWarehouse)
                           .setParameter("erpWarehouseName",item.getWarehouseName())
                           .getResultList();
                   if(warehouseIds.isEmpty())
                       throw new PosException(messageSource.getMessage("not_integration_warehouse",null, LocaleContextHolder.getLocale()));
                   warehouseId =new BigDecimal(String.valueOf(warehouseIds.get(0))).intValue();

                   String sqlPricelist = "select d_pricelist_id from d_pricelist where  erp_pricelist_name = :erpPricelistName  ";
                   List<Integer> pricelistIds = entityManager.createNativeQuery(sqlPricelist)
                           .setParameter("erpPricelistName", item.getPriceListName())
                           .getResultList();
                   if(pricelistIds.isEmpty()) {
                       HttpHeaders headersPost = new HttpHeaders();
                       headersPost.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                       headersPost.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                       headersPost.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                       headersPost.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                       headersPost.set("userId", AuditContext.getAuditInfo().getUserId().toString());
                       headersPost.set("Accept-Language", LocaleContextHolder.getLocale().toString());
                       headersPost.setContentType(MediaType.APPLICATION_JSON);
                       HttpEntity<PriceListIntDto> requestEntity = new HttpEntity<>(item.getPriceListIntDto(), headersPost);

                       GlobalReponse exresponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_URL_SAVE_PRICELIST  ,requestEntity, GlobalReponse.class) ;
                       pricelistIds.add((Integer) exresponse.getData());
                   }
                   pricelistId =new BigDecimal(String.valueOf(pricelistIds.get(0))).intValue();

                   if(!item.getSalesRepName().isEmpty())
                   {
                       String sqlUser = "select d_user_id from d_user where  erp_user_name = :erpUserName  ";
                       List<Integer> userIds = entityManager.createNativeQuery(sqlUser)
                               .setParameter("erpUserName", item.getSalesRepName())
                               .getResultList();
                       if(userIds.isEmpty())
                           throw new PosException(messageSource.getMessage("not_integration_user",null, LocaleContextHolder.getLocale()));
                       userId =new BigDecimal(String.valueOf(userIds.get(0))).intValue();
                   }
                   entity = posTerminalRepository.findByErpPtmName(item.getErpPtmName()).orElse(null);
               }
                item.setOrgId(orgErp.getId());
                // neu co dong bo may pos la co quan ly pos
                orgErp.setIsPosMng("Y");
                orgRepository.save(orgErp);
            if(entity != null)
            {
                modelMapper.map(item, entity);
                entity.setOrgId(orgErp.getId());
                entity.setWarehouseId(warehouseId);
                entity.setPriceListId(pricelistId);
                entity.setUserId(userId);
                entity.setIsDefault("N");
                posTerminalRepository.save(entity);
            }
            else {
                entity = modelMapper.map(item, PosTerminal.class);
                entity.setOrgId(orgErp.getId());
                entity.setWarehouseId(warehouseId);
                entity.setPriceListId(pricelistId);
                entity.setUserId(userId);
                entity.setIsDefault("N");
                entity.setIsActive("Y");
                posTerminalRepository.save(entity);
            }
        });


        return GlobalReponse.builder()
                .message(messageSource.getMessage("integration_success",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .data(dto)
                .build();
    }

    @Override

    public GlobalReponse findByErpId(Integer erpId) {
        log.info("*** Pos Terminal, service; fetch Pos Terminal by erpId *");
        log.info("tenantId: "+AuditContext.getAuditInfo().getTenantId());
        PosTerminalDto posTerminalDto = posTerminalRepository.findByErpPosId(erpId)
                .map(posTerminal -> modelMapper.map(posTerminal, PosTerminalDto.class))
                .orElse(null);
        if(posTerminalDto == null)
        {
        return GlobalReponse.builder()
                    .data(posTerminalDto)
                    .message( messageSource.getMessage("not_integration_pos",null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errors("")
                    .build();
        }
        return GlobalReponse.builder()
                .data(posTerminalDto)
                .message(messageSource.getMessage("fetchPosTerminal",null,LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @param dto
     * @return
     */
    @Override
//    @Transactional
    public GlobalReponse saveIntUser(PosTerminalDto dto) {
        PosTerminal posTerminal = posTerminalRepository.findByErpPosId(dto.getErpPosId()).orElse(null);
        if(posTerminal == null)
        {
            posTerminal = modelMapper.map(dto, PosTerminal.class);
            posTerminalRepository.save(posTerminal);
        }
        dto = (modelMapper.map(posTerminal, PosTerminalDto.class));
        return GlobalReponse.builder()
                .data(dto)
                .message("success")
                .errors("")
                .status(HttpStatus.OK.value()).build();
    }

    @Override
    public GlobalReponse deleteByPosTerminalId(PosTerminalDto posTerminalId) {
        log.info("*** Pos Terminal, service; delete Pos Terminal by id *");

        PosTerminal terminal = posTerminalRepository.findById(posTerminalId.getId()).orElseThrow(()-> new PosException(messageSource.getMessage("notFoundPosTerminal", null, LocaleContextHolder.getLocale())));
        if (isPosTerminalHasTransaction(posTerminalId.getId())) {
            throw new PosException(messageSource.getMessage("posTerminal.has.transaction", null, LocaleContextHolder.getLocale()));
        }
        try{
            GlobalReponse response = new GlobalReponse();

            
            posTerminalRepository.deleteById(posTerminalId.getId());

            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());

            return response;
        }catch (Exception e) {
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
    }

    public boolean isPosTerminalHasTransaction(Integer id) {
        try {
            String sql = "SELECT 1 " +
                    " FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_terminal_id = :posTerminalId limit 1";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posTerminalId", id)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                return true;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return false;
    }
}
