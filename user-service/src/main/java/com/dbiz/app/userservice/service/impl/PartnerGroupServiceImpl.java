package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.dbiz.app.userservice.domain.Customer;
import com.dbiz.app.userservice.domain.PartnerGroup;
import com.dbiz.app.userservice.domain.Vendor;
import com.dbiz.app.userservice.helper.CustomerMapper;
import com.dbiz.app.userservice.helper.VendorMapper;
import com.dbiz.app.userservice.repository.CustomerRepository;
import com.dbiz.app.userservice.repository.VendorRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.tomcat.jni.Global;
import org.common.dbiz.dto.integrationDto.UserIntKafkaDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.IntPartnerDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DeletePartnerGroupByIds;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import com.dbiz.app.userservice.repository.PartnerGroupRepository;
import com.dbiz.app.userservice.service.PartnerGroupService;
import com.dbiz.app.userservice.specification.PartnerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartnerGroupServiceImpl implements PartnerGroupService {
    private final PartnerGroupRepository partnerGroupRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final EntityManager entityManager;

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final static String TOPIC_SEND_CAV = "sync-integration-to-customer-vendor";

    private final static String TOPIC_RECEIVE_CAV = "sync-customer-vendor-to-integration";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    @Override
    public GlobalReponsePagination findAll(PartnerGroupQuery request) {
        log.info("Find All PartnerGroup Service");
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<PartnerGroup> spec = PartnerSpecification.getEntitySpecification(request);

        Page<PartnerGroup> pages = partnerGroupRepository.findAll(spec, pageable);
        List<PartnerGroupDto> listData = new ArrayList<>();
        for (PartnerGroup item : pages.getContent()) {
            PartnerGroupDto itemDto = modelMapper.map(item, PartnerGroupDto.class);
            listData.add(itemDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage(messageSource.getMessage("partnerGroup_fetch_all", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(pages.getTotalPages());
        globalReponsePagination.setPageSize(pages.getSize());
        globalReponsePagination.setCurrentPage(pages.getNumber());
        globalReponsePagination.setTotalItems(pages.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    @Transactional
    public GlobalReponse save(PartnerGroupDto dto) {
        log.info("Save PartnerGroup Service");
        GlobalReponse reponse = new GlobalReponse();
        if (dto.getId() != null) {
            PartnerGroup partnerGroup = partnerGroupRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException(messageSource.getMessage("partnerGroup_notFound", null, LocaleContextHolder.getLocale())));
            modelMapper.map(dto, partnerGroup);
            this.partnerGroupRepository.save(partnerGroup);
            reponse.setData(modelMapper.map(partnerGroup, PartnerGroupDto.class));
            reponse.setMessage(messageSource.getMessage("partnerGroup_update", null, LocaleContextHolder.getLocale()));
            reponse.setStatus(HttpStatus.OK.value());
        } else {
            if (dto.getGroupCode() == null) {
                String docNo = "BP" + (partnerGroupRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                dto.setGroupCode(docNo);
            } else {
                if (partnerGroupRepository.findByGroupCode(dto.getGroupCode()) != null)
                    throw new PosException(messageSource.getMessage("partnerGroup_code_exist", null, LocaleContextHolder.getLocale()));
            }

            PartnerGroup entity = modelMapper.map(dto, PartnerGroup.class);
            entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entity.setOrgId(0);
            dto = modelMapper.map(partnerGroupRepository.save(entity), PartnerGroupDto.class);
            reponse.setData(dto);
            reponse.setMessage(messageSource.getMessage("partnerGroup_create", null, LocaleContextHolder.getLocale()));

        }
        reponse.setStatus(HttpStatus.CREATED.value());
        return reponse;
    }

    @Override
    @Transactional
    public GlobalReponse deleteById(Integer id) {
        log.info("Delete PartnerGroup Service");
        GlobalReponse reponse = new GlobalReponse();

        PartnerGroup entity = partnerGroupRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer delete not found"));
        try {
            partnerGroupRepository.delete(entity);
            partnerGroupRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("partnerGroup.not.delete", null, LocaleContextHolder.getLocale()));
        }
        reponse.setMessage(messageSource.getMessage("customer_delete", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("Find PartnerGroup Service");
        GlobalReponse reponse = new GlobalReponse();
        PartnerGroup entity = partnerGroupRepository.findById(id).orElseThrow(() -> new RuntimeException(messageSource.getMessage("customer_notFound", null, LocaleContextHolder.getLocale())));
        reponse.setData(modelMapper.map(entity, PartnerGroupDto.class));
        reponse.setMessage(messageSource.getMessage("partnerGroup_fetch_success", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override

    @Transactional
    public GlobalReponse removeChildPartner(PartnerGroupDto param) {
        log.info("Remove Child Partner Service");
        GlobalReponse reponse = new GlobalReponse();
        PartnerGroup entity = partnerGroupRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException(messageSource.getMessage("partnerGroup_notFound", null, LocaleContextHolder.getLocale())));
        partnerGroupRepository.save(entity);
        reponse.setStatus(HttpStatus.OK.value());
        reponse.setMessage(messageSource.getMessage("partnerGroup_remove_child", null, LocaleContextHolder.getLocale()));
        reponse.setData(modelMapper.map(entity, PartnerGroupDto.class));
        return reponse;
    }

    @Override

    @Transactional
    public GlobalReponse deleteAllByIds(DeletePartnerGroupByIds request) {

        log.info("Delete All PartnerGroup Service");

        GlobalReponse reponse = new GlobalReponse();

        for (Integer i : request.getIds()) {
            partnerGroupRepository.deleteById(i);
        }
        reponse.setData(null);
        reponse.setMessage(messageSource.getMessage("partnerGroup_delete_all", null, LocaleContextHolder.getLocale()));
        reponse.setStatus(HttpStatus.OK.value());
        return reponse;

    }

    @Override
    public GlobalReponse intSave(List<PartnerGroupDto> listInt) {
        log.info("Int Save PartnerGroup Service");
        this.saveListPartnerIdempiere(listInt);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("PartnerGroup saved successfully")
                .errors("")
                .data(listInt)
                .build();
    }

    /**
     *
     * @param listInt
     * @return
     */
    @Override
    public GlobalReponse intSaveERPNext(List<PartnerGroupDto> listInt)
    {
        log.info("Save Int PartnerGroup ERPNext Service");
        GlobalReponse reponse = new GlobalReponse();
        try {
            this.saveListPartnerFromERPNext(listInt);
            reponse.setStatus(HttpStatus.OK.value());
            reponse.setMessage("PartnerGroup saved successfully");
            reponse.setData(listInt);

        }catch (Exception e)
        {
            e.printStackTrace();
            reponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            reponse.setMessage( e.getMessage());
            reponse.setData(null);
        }
        return reponse;
    }

    @Transactional
    public void saveListPartnerIdempiere(List<PartnerGroupDto> listInt)
    {
        List<PartnerGroup> list = this.partnerGroupRepository.findAll();
        listInt.forEach(item -> {
            PartnerGroup entityCheck = partnerGroupRepository.findByErpBpGroupId(item.getErpBpGroupId());
            if (entityCheck == null) {
                PartnerGroup entity = modelMapper.map(item, PartnerGroup.class);
                entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entity.setOrgId(0);
                partnerGroupRepository.save(entity);
                item = modelMapper.map(entity, PartnerGroupDto.class);
            } else {
                modelMapper.map(item, entityCheck);
                partnerGroupRepository.save(entityCheck);
                item = modelMapper.map(entityCheck, PartnerGroupDto.class);
            }

        });
    }

//    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_SEND_CAV, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, IntPartnerDto> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("listtener save partner");
////        log.info("Received message: " + consumerRecord.value());
//        try {
//            String key = consumerRecord.key(); // could be null
//            List<CustomerDto> customers = consumerRecord.value().getCustomers();
//            List<VendorDto> vendors = consumerRecord.value().getVendors();
//            List<PartnerGroupDto> partnerGroups = consumerRecord.value().getPartnerGroups();
//
//            try {
//                int tenantNumbers = getTenantNumbers();
//                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//                            .configureDataSources();
//
//                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//                }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (consumerRecord.value().getTenantId() != 0) {
//                dataSourceContextHolder.setCurrentTenantId(new Long(consumerRecord.value().getTenantId()));
//            } else {
//                dataSourceContextHolder.setCurrentTenantId(null);
//
//            }
//            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                    "0", 0, "vi", consumerRecord.value().getTenantId()));
//
//
//            log.info("Received message:");
//            log.info("Key: " + key);
////                log.info("Value: " + value);
//            IntPartnerDto resultPartner = new IntPartnerDto();
//            IntPartnerDto resultCustomer = new IntPartnerDto();
//            IntPartnerDto resultVendor = new IntPartnerDto();
//            if (consumerRecord.value().getPartnerGroups() != null && !consumerRecord.value().getPartnerGroups().isEmpty()) {
//                log.info("int save partner group");
//                resultPartner = this.intSavePartnerKafka(partnerGroups);
//            }
//
//            if (consumerRecord.value().getCustomers() != null && !consumerRecord.value().getCustomers().isEmpty()) {
//                log.info("int save customer ");
//                resultCustomer = this.intSaveCustomerKafkar(customers);
//            }
//            if (consumerRecord.value().getVendors() != null && !consumerRecord.value().getVendors().isEmpty()) {
//                log.info("int save vendor");
//                resultVendor = this.intSaveVendorKafka(vendors);
//                resultVendor.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//                resultVendor.setSyncIntegrationCredential(consumerRecord.value().getSyncIntegrationCredential());
//                kafkaTemplate.send(TOPIC_RECEIVE_CAV, resultVendor);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        acknowledgment.acknowledge();
//    }

    @Transactional
    public IntPartnerDto intSavePartnerKafka(List<PartnerGroupDto> listInt) {
        log.info("Int Save PartnerGroup Service");
        IntPartnerDto result = new IntPartnerDto();
        result.setError("");
        result.setStatus("COM");
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        try {
            listInt.forEach(item -> {
                PartnerGroup entityCheck = partnerGroupRepository.findByErpBpGroupId(item.getErpBpGroupId());
                if (entityCheck == null) {
                    PartnerGroup entity = modelMapper.map(item, PartnerGroup.class);
                    entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    entity.setOrgId(0);
                    partnerGroupRepository.save(entity);
                    item = modelMapper.map(entity, PartnerGroupDto.class);
                } else {
                    modelMapper.map(item, entityCheck);
                    partnerGroupRepository.save(entityCheck);
                    item = modelMapper.map(entityCheck, PartnerGroupDto.class);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(e.getMessage());
            result.setStatus("FAI");
        }
        return result;
    }

    @Transactional
    public IntPartnerDto intSaveCustomerKafkar(List<CustomerDto> listInt) {

        log.info("Save All Customer int");
        IntPartnerDto result = new IntPartnerDto();
        result.setError("");
        result.setStatus("COM");
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        StringBuilder builder = new StringBuilder();
        List<CustomerDto> savedCustomers = new ArrayList<>();

        listInt.forEach(item -> {
            try {

                Customer cusCheck = customerRepository.findByErpCustomerId(item.getErpCustomerId()).orElse(null);
                PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
                if (cusCheck == null) {
//                    if (customerRepository.findFirstByPhone1(item.getPhone1()).isPresent()) {
//                        builder.append(item.getPhone1() + "Already Exist,");
//                        return;
//                    }
                    cusCheck = customerMapper.toCustomer(item);
                    if (group == null)
                        log.info("partner null: " + item.getPartnerGroupId());
                    else
                        cusCheck.setPartnerGroupId(group.getId());
                    cusCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    cusCheck = customerRepository.save(cusCheck);
                    modelMapper.map(cusCheck, item);
                } else {
//                    if (customerRepository.findFirstByPhone1(item.getPhone1()).isPresent())
//                    {
//                        if(!item.getPhone1().equals(cusCheck.getPhone1()))
//                        {
//                            builder.append(item.getPhone1() + "Already Exist,");
//                            return;
//                        }
//                    }
                    customerMapper.updateEntity(item, cusCheck);
                    if (group == null)
                        log.info("partner null: " + item.getPartnerGroupId());
                    else
                        cusCheck.setPartnerGroupId(group.getId());
                    customerRepository.save(cusCheck);
                }
//                TransactionAspectSupport.currentTransactionStatus().flush();
                savedCustomers.add(item);
            } catch (Exception e) {
                e.printStackTrace();
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setError(e.getMessage());
                result.setStatus("FAI");
            }


        });

        return result;

    }


    @Transactional
    public IntPartnerDto intSaveVendorKafka(List<VendorDto> vendorDto) {
        log.info("Int Save Vendor Service");
        IntPartnerDto result = new IntPartnerDto();
        result.setError("");
        result.setStatus("COM");
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        try {

            StringBuilder message = new StringBuilder();
            vendorDto.forEach(item -> {
                PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
                Vendor vendorCheck = vendorRepository.findByErpVendorId(item.getErpVendorId());
                if (vendorCheck == null) {
//                if(vendorRepository.findByPhone1(item.getPhone1()).isPresent())
//                {
//                    message.append("Phone1: ").append(item.getPhone1()).append(" already exists").append("\n");
//                    return;
//                }
                    Vendor vendor = vendorMapper.toVendor(item);
                    if (group == null)
                        log.info("partner null: " + item.getPartnerGroupId());
                    else
                        vendor.setPartnerGroupId(group.getId());
                    vendor.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    vendorRepository.save(vendor);
                    item = vendorMapper.toVendorDto(vendor);
                } else {
//                if(vendorRepository.findByPhone1(item.getPhone1()).isPresent())
//                {
//                    message.append("Phone1: ").append(item.getPhone1()).append(" already exists").append("\n");
//                    return;
//                }
                    vendorMapper.updateEntity(item, vendorCheck);
                    if (group == null)
                        log.info("partner null: " + item.getPartnerGroupId());
                    else
                        vendorCheck.setPartnerGroupId(group.getId());
                    vendorRepository.save(vendorCheck);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(e.getMessage());
            result.setStatus("FAI");
        }
        return result;
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @Transactional
    public void saveListPartnerFromERPNext(List<PartnerGroupDto> param)
    {
        log.info("function saveListPartnerFromERPNext");
        List<PartnerGroup> list = this.partnerGroupRepository.findAll();
        for(PartnerGroupDto item : param)
        {
            // check group exist
            PartnerGroup entityCheck =this.checkPartnerGroupByERPNext(item);

            if(entityCheck == null)// insert
            {
                PartnerGroup entity = modelMapper.map(item, PartnerGroup.class);
                entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entity.setOrgId(0);
                if(item.getParent() != null) // handler parent
                {
                    PartnerGroup parentCheck = this.checkPartnerGroupByERPNext(item.getParent());
                    if(parentCheck == null)
                    {
                        PartnerGroup entityParent = modelMapper.map(item.getParent(), PartnerGroup.class);
                        entityParent.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        entityParent.setOrgId(0);
                        this.partnerGroupRepository.save(entityParent);
                        entity.setDPartnerGroupParentId(entityParent.getId());
                    }
                    else
                        entity.setDPartnerGroupParentId(parentCheck.getId());
                }
                partnerGroupRepository.save(entity);
                item = modelMapper.map(entity, PartnerGroupDto.class);
            }
            else// update
            {
                modelMapper.map(item, entityCheck);
                PartnerGroup parentCheck = this.checkPartnerGroupByERPNext(item.getParent());
                if(parentCheck == null)
                {
                    PartnerGroup entityParent = modelMapper.map(item.getParent(), PartnerGroup.class);
                    entityParent.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    entityParent.setOrgId(0);
                    this.partnerGroupRepository.save(entityParent);
                    entityCheck.setDPartnerGroupParentId(entityParent.getId());
                }
                else
                    entityCheck.setDPartnerGroupParentId(parentCheck.getId());
                partnerGroupRepository.save(entityCheck);
                item = modelMapper.map(entityCheck, PartnerGroupDto.class);
            }
        }

    }

    private PartnerGroup checkPartnerGroupByERPNext(PartnerGroupDto param)
    {
        if(param.getId()!=null)
            return partnerGroupRepository.findById(param.getId())
                .orElse(this.partnerGroupRepository.findByErpBpGroupName(param.getErpBpGroupName()).orElse(null));
        else
            return this.partnerGroupRepository.findByErpBpGroupName(param.getErpBpGroupName()).orElse(null);
    }

}
