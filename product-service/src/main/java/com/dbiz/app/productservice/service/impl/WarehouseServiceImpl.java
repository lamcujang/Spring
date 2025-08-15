package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.Locator;
import com.dbiz.app.productservice.domain.Warehouse;
import com.dbiz.app.productservice.helper.Helper;
import com.dbiz.app.productservice.helper.Mapper.WarehouseMapper;
import com.dbiz.app.productservice.repository.LocatorRepository;
import com.dbiz.app.productservice.repository.ProductLocationRepository;
import com.dbiz.app.productservice.repository.WarehouseRepository;
import com.dbiz.app.productservice.service.WarehouseService;
import com.dbiz.app.productservice.specification.ProductSpecification;
import com.dbiz.app.productservice.specification.WarehouseSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ListWarehouseDto;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.WarehouseIntDto;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class WarehouseServiceImpl implements WarehouseService {
    private final RequestParamsUtils requestParamsUtils;

    private final WarehouseRepository warehouseRepository;

    private final WarehouseMapper warehouseMapper;

    private final ProductLocationRepository productLocationRepository;

    private final MessageSource messageSource;

    private final Helper helper;

    private final LocatorRepository locatorRepository;

    private final ModelMapper modelMapper;


    private final RestTemplate restTemplate;

    private final EntityManager entityManager;


    private StringBuilder errors;

    @Override
    public GlobalReponsePagination findAll(WarehouseQueryRequest paramRequest) {
        log.info("*** WarehouseDto List, service; fetch all products *");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<Warehouse> spec = WarehouseSpecification.getWarehouseSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Warehouse> warehouses = warehouseRepository.findAll(spec, pageable);
        List<WarehouseDto> listData = new ArrayList<>();
        for (Warehouse item : warehouses.getContent()) {
            WarehouseDto warehouseDto = warehouseMapper.toWarehouseDto(item);
            warehouseDto.setOrgName(getOrgName(item.getOrgId()));
            List<Locator> locator = locatorRepository.findAllByWarehouseId(item.getId());
            List<LocatorDto> listLocatorDto = new ArrayList<>();
            listLocatorDto = locator.stream().map(locator1 -> modelMapper.map(locator1, LocatorDto.class)).collect(Collectors.toList());
            warehouseDto.setLocatorDtos(listLocatorDto);
            listData.add(warehouseDto);
        }
        response.setMessage(messageSource.getMessage("warehouse.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(warehouses.getNumber());
        response.setPageSize(warehouses.getSize());
        response.setTotalPages(warehouses.getTotalPages());
        response.setTotalItems(warehouses.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(WarehouseDto Dto) {

        log.info("*** warehouse, service; save warehouse ***");
        GlobalReponse response = new GlobalReponse();
        Warehouse warehouseSave = warehouseMapper.toWarehouse(Dto);
        WarehouseDto whDtoReponse = null;

        if (warehouseSave.getId() != null) // update
        {
            warehouseSave = this.warehouseRepository.findById(Dto.getId()).orElseThrow(() -> new ObjectNotFoundException("Warehouse not found"));

            warehouseSave = warehouseMapper.updateEntity(Dto, warehouseSave);
            this.warehouseRepository.save(warehouseSave);

            Integer warehouseId = warehouseSave.getId();

            if (Dto.getLocatorDtos() != null && Dto.getLocatorDtos().size() > 0) {
                AtomicBoolean checkDefault = new AtomicBoolean(true);
                Dto.getLocatorDtos().forEach(locatorDto -> {
                    if (locatorDto.getIsDefault().equals("Y") && !checkDefault.get()) {
                        throw new PosException(messageSource.getMessage("error.unique.locator.default", null, LocaleContextHolder.getLocale()));
                    }
                    if (locatorDto.getIsDefault().equals("Y")) {
                        checkDefault.set(false);
                    }
                    if (locatorDto.getId() == null) {// insert locator
                        locatorDto.setId(null);
                        if (locatorDto.getCode() == null) {
                            locatorDto.setCode("LOC" + (this.locatorRepository.getMaxLocatorId() + 1) + LocalDate.now().getDayOfMonth());
                        }
                        locatorDto.setWarehouseId(warehouseId);
                        Locator locatorSave = modelMapper.map(locatorDto, Locator.class);
                        locatorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        locatorSave = locatorRepository.save(locatorSave);
                        locatorDto.setId(locatorSave.getId());
                    } else {// update locator

                        Locator locator = locatorRepository.findById(locatorDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Locator not found"));
                        modelMapper.map(locatorDto, locator);
                        locatorRepository.save(locator);
                    }

                });
            }
            response.setMessage(messageSource.getMessage("warehouse.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        } else {

            if (warehouseSave.getCode() == null || warehouseSave.getCode().isEmpty()) {
                warehouseSave.setCode("WH" + (this.warehouseRepository.getMaxWarehouseId() + 1) + LocalDate.now().getDayOfMonth());
            }

            if (warehouseSave.getIsNegative() == null) {
                warehouseSave.setIsNegative("N");
            }

            if (warehouseSave.getOrgId() == null) {
                warehouseSave.setOrgId(0);
            }

            if (warehouseSave.getIsMergeItem() == null) {
                warehouseSave.setIsMergeItem("N");
            }

            warehouseSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            warehouseSave = this.warehouseRepository.save(warehouseSave);
            Integer warehouseId = warehouseSave.getId();

            if (Dto.getLocatorDtos() != null && Dto.getLocatorDtos().size() > 0) {
                AtomicBoolean checkDefault = new AtomicBoolean(true);
                Dto.getLocatorDtos().forEach(locatorDto -> {
                    if (locatorDto.getIsDefault().equals("Y") && !checkDefault.get()) {
                        throw new PosException(messageSource.getMessage("error.unique.locator.default", null, LocaleContextHolder.getLocale()));
                    }
                    if (locatorDto.getIsDefault().equals("Y")) {
                        checkDefault.set(false);
                    }
                    if (locatorDto.getId() == null && locatorDto.getCode() == null) {

                        locatorDto.setCode("LOC" + (this.locatorRepository.getMaxLocatorId() + 1) + LocalDate.now().getDayOfMonth());
                    }
                    locatorDto.setWarehouseId(warehouseId);
                    Locator locatorSave = modelMapper.map(locatorDto, Locator.class);
                    locatorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    locatorSave = locatorRepository.save(locatorSave);
                    locatorDto.setId(locatorSave.getId());
                    locatorDto.setIsActive(locatorSave.getIsActive());
                });
            } else {//Nếu tạo mới kho tự động tạo thêm locator mặc định cho kho đó
                Locator locatorSave = new Locator();
                locatorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                locatorSave.setWarehouseId(warehouseId);
                locatorSave.setCode("LOC" + (this.locatorRepository.getMaxLocatorId() + 1) + LocalDate.now().getDayOfMonth());
                locatorSave.setIsActive("Y");
                locatorSave.setName(messageSource.getMessage("default.locator.name", null, LocaleContextHolder.getLocale()));
                locatorSave.setIsDefault("Y");
                locatorSave.setOrgId(warehouseSave.getOrgId());
                locatorSave = locatorRepository.save(locatorSave);
                LocatorDto locatorDto = modelMapper.map(locatorSave, LocatorDto.class);
                Dto.setLocatorDtos(new ArrayList<>());
                Dto.getLocatorDtos().add(locatorDto);
            }


            response.setMessage(messageSource.getMessage("warehouse.saved", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }
        whDtoReponse = warehouseMapper.toWarehouseDto(warehouseSave);
        whDtoReponse.setLocatorDtos(Dto.getLocatorDtos());
        response.setData(whDtoReponse);

        log.info("Warehouse saved successfully with ID: {}", warehouseSave.getId());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete warehouse by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<Warehouse> warehouseDelete = this.warehouseRepository.findById(id);
        if (warehouseDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("warehouse.not.found", null, LocaleContextHolder.getLocale()), id));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.warehouseRepository.delete(warehouseDelete.get());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("warehouse.deleted", null, LocaleContextHolder.getLocale()));
        return response;
    }

    /**
     * @param erpId
     * @return
     */
    @Override
    public GlobalReponse findByErpId(Integer erpId) {
        log.info("*** WarehouseDto, service; fetch warehouse by erpId *");
        GlobalReponse response = new GlobalReponse();
        Warehouse warehouse = this.warehouseRepository.findByErpWarehouseId(erpId).orElse(null);
        if (warehouse == null) {
            response.setMessage(String.format(messageSource.getMessage("warehouse.not.found", null, LocaleContextHolder.getLocale()), erpId));
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
        response.setStatus(HttpStatus.OK.value());
        response.setData(modelMapper.map(warehouse, WarehouseDto.class));
        return response;
    }

    @Override
    public GlobalReponse updateAll(ListWarehouseDto param) {

        log.info("*** WarehouseDto, service; save all warehouse *");
        GlobalReponse response = new GlobalReponse();

        try {
            for (WarehouseDto warehouseDto : param.getData()) {
                Warehouse warehouse = warehouseRepository.findById(warehouseDto.getId()).get();
                warehouse = warehouseMapper.updateEntity(warehouseDto, warehouse);
                warehouseRepository.save(warehouse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        response.setMessage(messageSource.getMessage("warehouse.updated", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** WarehouseDto, service; fetch warehouse by id *");

        GlobalReponse response = new GlobalReponse();
        WarehouseDto warehouseDto = warehouseMapper.toWarehouseDto(this.warehouseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("warehouse.not.found", null, LocaleContextHolder.getLocale()), id))));

        warehouseDto.setOrgName(getOrgName(warehouseDto.getOrgId()));
        List<Locator> locator = locatorRepository.findAllByWarehouseId(warehouseDto.getId());
        List<LocatorDto> listLocatorDto = new ArrayList<>();
        listLocatorDto = locator.stream().map(locator1 -> modelMapper.map(locator1, LocatorDto.class)).collect(Collectors.toList());
        warehouseDto.setLocatorDtos(listLocatorDto);


        response.setData(warehouseDto);

        response.setMessage(messageSource.getMessage("warehouse.fetch.success", null, LocaleContextHolder.getLocale()));
        return response;
    }

    /**
     * @param param
     * @return
     */
    @Override
    public GlobalReponse intSave(WarehouseIntDto param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        log.info("*** WarehouseDto, service; integration warehouse *");
        errors = new StringBuilder();

        param.getListWarehouseDto().forEach(item ->
        {
            Integer orgId = null;
            Warehouse warehouse = null;
            if (param.getType().equals(AppConstant.ERP_PLATFORM_IDEMPIERE)) {
                List<Tuple> rs = entityManager.createNativeQuery("select d_org_id from d_org where erp_org_id  = :orgId ", Tuple.class)
                        .setParameter("orgId", item.getOrgId())
                        .getResultList();
                if (rs.isEmpty())
                    throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
                orgId = Integer.valueOf(rs.get(0).get("d_org_id").toString());
                warehouse = warehouseRepository.findByErpWarehouseId(item.getErpWarehouseId()).orElse(null);

            } else if (param.getType().equals(AppConstant.ERP_PLATFORM_ERPNEXT)) {
                List<Tuple> rs = entityManager.createNativeQuery("select d_org_id from d_org where erp_org_name  = :orgName ", Tuple.class)
                        .setParameter("orgName", item.getOrgName())
                        .getResultList();
                if (rs.isEmpty())
                    throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
                orgId = Integer.valueOf(rs.get(0).get("d_org_id").toString());
                warehouse = warehouseRepository.findByErpWarehouseName(item.getErpWarehouseName()).orElse(null);
            }
            if (warehouse == null) {
                warehouse = modelMapper.map(item, Warehouse.class);
                warehouse.setTenantId(AuditContext.getAuditInfo().getTenantId());
                warehouse.setOrgId(orgId);
                warehouse = warehouseRepository.save(warehouse);

                //tao locator mac dinh cho warehouse
                Locator locatorSave = new Locator();
                locatorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                locatorSave.setWarehouseId(warehouse.getId());
                locatorSave.setCode("LOC" + (this.locatorRepository.getMaxLocatorId() + 1) + LocalDate.now().getDayOfMonth());
                locatorSave.setIsActive("Y");
                locatorSave.setName(messageSource.getMessage("default.locator.name", null, LocaleContextHolder.getLocale()));
                locatorSave.setIsDefault("Y");
                locatorSave.setOrgId(warehouse.getOrgId());
                locatorSave = locatorRepository.save(locatorSave);

                productLocationRepository.updateAllByWarehouseId(warehouse.getId(), locatorSave.getId());

            } else {
                modelMapper.map(item, warehouse);
                warehouse.setOrgId(orgId);
                this.warehouseRepository.save(warehouse);
                Locator locator = locatorRepository.findByWarehouseIdAndIsDefault(warehouse.getId(), "Y").orElse(null);
                if(locator == null)
                {
                    Locator locatorSave = new Locator();
                    locatorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    locatorSave.setWarehouseId(warehouse.getId());
                    locatorSave.setCode("LOC" + (this.locatorRepository.getMaxLocatorId() + 1) + LocalDate.now().getDayOfMonth());
                    locatorSave.setIsActive("Y");
                    locatorSave.setName(messageSource.getMessage("default.locator.name", null, LocaleContextHolder.getLocale()));
                    locatorSave.setIsDefault("Y");
                    locatorSave.setOrgId(warehouse.getOrgId());
                    locatorSave = locatorRepository.save(locatorSave);

                    productLocationRepository.updateAllByWarehouseId(warehouse.getId(), locatorSave.getId());
                }
            }
        });


        return GlobalReponse.builder()
                .message(messageSource.getMessage("integration_success", null, LocaleContextHolder.getLocale()))
                .data(param)
                .status(HttpStatus.OK.value())
                .errors(errors.toString())
                .build();
    }

    public String getOrgName(Integer orgId) {
        String sql1 = "select name from pos.d_org where d_org_id = :orgId and d_tenant_id = :tenantId";
        String orgName = ((String) entityManager.createNativeQuery(sql1)
                .setParameter("orgId", orgId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult()).toString();
        return orgName;
    }
}
