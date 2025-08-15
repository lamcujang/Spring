package com.dbiz.app.userservice.service.impl;


import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.Customer;
import com.dbiz.app.userservice.domain.ICustomer;
import com.dbiz.app.userservice.domain.Image;
import com.dbiz.app.userservice.domain.PartnerGroup;
import com.dbiz.app.userservice.helper.CustomerMapper;
import com.dbiz.app.userservice.helper.ImageHelper;
import com.dbiz.app.userservice.repository.ICustomerRepository;
import com.dbiz.app.userservice.service.IntegrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.exception.wrapper.PerstingObjectException;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.CustomerQueryRequest;
import com.dbiz.app.userservice.repository.CustomerRepository;
import com.dbiz.app.userservice.repository.ImageRepository;
import com.dbiz.app.userservice.repository.PartnerGroupRepository;
import com.dbiz.app.userservice.service.CustomerService;
import com.dbiz.app.userservice.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final RequestParamsUtils requestParamsUtils;
    private final CustomerMapper customerMapper;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final MessageSource messageSource;
    private final PartnerGroupRepository partnerGroupRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final ImageHelper imageHelper;
    private final ICustomerRepository iCustomerRepository;
    private final IntegrationService integrationService;
    private final KafkaAuditUserHelper kafkaAuditUserHelper;
    private final ObjectMapper objectMapper;
    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    @Override
    public GlobalReponsePagination findAll(CustomerQueryRequest request) {
        log.info("Find All Customer Service");
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Customer> spec = CustomerSpecification.getCustomerSpecification(request);

        // lay ra id giao dich  va them dieu kien vao where
        if (request.getTransactionAmountFrom() != null && request.getTransactionAmountTo() != null) {
            List<Integer> customers = new ArrayList<>();
            if (request.getTransactionAmountFrom() != null && request.getTransactionAmountTo() != null)
                customers.addAll(customerRepository.findByTransactionAmountBetween(request.getTransactionAmountFrom(), request.getTransactionAmountTo(),
                        AuditContext.getAuditInfo().getTenantId()));

            if (request.getInvoiceCode() != null)
                customers.addAll(customerRepository.findByDocNo(request.getInvoiceCode(), AuditContext.getAuditInfo().getTenantId()));
            if (!customers.isEmpty()) {
                Specification<Customer> customerIdSpec = (root, query, criteriaBuilder) ->
                        root.get("id").in(customers);
                spec = spec.and(customerIdSpec);
            }
        }

        Page<Customer> customers = customerRepository.findAll(spec, pageable);
        List<CustomerDto> listData = new ArrayList<>();
        for (Customer customer : customers.getContent()) {
            BigDecimal totalTransactionAmount = customerRepository.getTotalTransactionAmountByCustomerId(customer.getId());
            PartnerGroupDto groupDto = null;
            if (customer.getPartnerGroupId() != null) {
                Optional<PartnerGroup> groupOptional = partnerGroupRepository.findById(customer.getPartnerGroupId());

                if (groupOptional.isPresent()) {
                    PartnerGroup group = groupOptional.get();
                    groupDto = modelMapper.map(group, PartnerGroupDto.class);
                } else {
                    PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(customer.getPartnerGroupId());
                    if (group != null) {
                        customer.setPartnerGroupId(group.getId());
                        customerRepository.saveAndFlush(customer);
                        groupDto = modelMapper.map(group, PartnerGroupDto.class);
                    }
                }
            }


            CustomerDto customerDto = customerMapper.toCustomerDto(customer);
            customerDto.setPartnerGroup(groupDto);
            customerDto.setTotalTransactionAmount(totalTransactionAmount);
            listData.add(customerDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage(messageSource.getMessage("customer_fetch_all", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(customers.getTotalPages());
        globalReponsePagination.setPageSize(customers.getSize());
        globalReponsePagination.setCurrentPage(customers.getNumber());
        globalReponsePagination.setTotalItems(customers.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse save(CustomerDto customerDto) {
        log.info("Save Customer Service");
        GlobalReponse reponse = new GlobalReponse();
        Customer customerSave = null;

        String insertOrUpdate = "INSERT";
        if (customerDto.getId() != null) {
            insertOrUpdate = "UPDATE";
        }

        if (customerDto.getId() != null) {//update
            customerSave = this.customerRepository.findById(customerDto.getId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            PartnerGroupDto groupDto = null;
            if (customerRepository.existsByPhone1AndIdNot(customerDto.getPhone1(), customerDto.getId())) {
                throw new PosException(messageSource.getMessage("customer_phone_exist_other", null, LocaleContextHolder.getLocale()));
            }

            if (customerDto.getImage() != null) {
                if (customerDto.getImage().getId() != null)
                    this.imageRepository.delete(customerSave.getImage());
                Image image = this.imageRepository.save(modelMapper.map(customerDto.getImage(), Image.class));
                customerSave.setImage(image);
            }
            customerSave = customerMapper.updateEntity(customerDto, customerSave);
            CustomerDto cusDto = customerMapper.toCustomerDto(this.customerRepository.save(customerSave));

            if (customerSave.getPartnerGroupId() != null) {
                groupDto = modelMapper.map(partnerGroupRepository.findById(customerSave.getPartnerGroupId()).orElseThrow(() -> new ObjectNotFoundException("Partner Group not found")), PartnerGroupDto.class);
                cusDto.setPartnerGroup(groupDto);
            }
            cusDto.setTotalTransactionAmount(customerRepository.getTotalTransactionAmountByCustomerId(cusDto.getId()));
            reponse.setData(cusDto);
            reponse.setMessage(messageSource.getMessage("customer_update", null, LocaleContextHolder.getLocale()));

        } else {//insert
            if (customerDto.getCode() == null) {
                String docNo = "CUS" + (customerRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                customerDto.setCode(docNo);
            } else {
                if (this.customerRepository.existsByCode(customerDto.getCode()))
                    throw new PosException(messageSource.getMessage("customer_code_exist", null, LocaleContextHolder.getLocale()));
            }
            if (customerRepository.existsByPhone1(customerDto.getPhone1())) {
                throw new PosException(messageSource.getMessage("customer_phone_exist", null, LocaleContextHolder.getLocale()));
            }

            customerSave = customerMapper.toCustomer(customerDto);
            if (customerDto.getImage() != null) {
                Image image = imageHelper.saveImage(customerDto.getImage());
                customerSave.setImage(image);
            }
            customerSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            customerSave = customerRepository.save(customerSave);
            customerDto = customerMapper.toCustomerDto(customerSave);
            reponse.setData(customerDto);
            reponse.setStatus(HttpStatus.CREATED.value());
            reponse.setMessage(messageSource.getMessage("customer_create", null, LocaleContextHolder.getLocale()));
        }
        try {
            // Send Kafka save Audit for user

            Map<String, String> productInfo = Map.of(
                    "name", customerSave.getName(),
                    "document_no", customerSave.getCode()
            );
            String productInfoJson = objectMapper.writeValueAsString(productInfo);

            // Send Kafka save AuditUser for INSERT||UPDATE Customer
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(customerSave),
                    customerSave.getId(),
                    insertOrUpdate,
                    AuditContext.getAuditInfo().getUserId(),
                    productInfoJson);

        } catch (Exception e) {
            log.info("Error: CustomerServiceImpl: save(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: CustomerServiceImpl: save(): send Kafka save AuditLogUser"); // thêm messageSource
        }
        return reponse;
    }

    @Override
    public GlobalReponse deleteById(Integer customerId) {
        log.info("Delete Customer Service");
        GlobalReponse reponse = new GlobalReponse();

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer delete not found"));
        try {
            customerRepository.delete(customer);
            customerRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("customer_delete_error", null, LocaleContextHolder.getLocale()));
        }
        try {
            // Send Kafka save Audit for user

            Map<String, String> productInfo = Map.of(
                    "name", customer.getName(),
                    "document_no", customer.getCode()
            );
            String productInfoJson = objectMapper.writeValueAsString(productInfo);

            // Send Kafka save AuditUser for INSERT||UPDATE Customer
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(customer),
                    customer.getId(),
                    "DELETE",
                    AuditContext.getAuditInfo().getUserId(),
                    productInfoJson);

        } catch (Exception e) {
            log.info("Error: CustomerServiceImpl: deleteById(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: CustomerServiceImpl: deleteById(): send Kafka save AuditLogUser"); // thêm messageSource
        }
        reponse.setMessage(messageSource.getMessage("customer_delete", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    public GlobalReponse findById(Integer customerId) {
        GlobalReponse reponse = new GlobalReponse();
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException(messageSource.getMessage("customer_notFound", null, LocaleContextHolder.getLocale())));
        CustomerDto dto = customerMapper.toCustomerDto(customer);
        dto.setTotalTransactionAmount(customerRepository.getTotalTransactionAmountByCustomerId(customerId));
        reponse.setData(dto);
        reponse.setMessage(messageSource.getMessage("customer_fetch_success", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    public GlobalReponse deleteAllCustomerByIds(CustomerDto ids) {
        GlobalReponse reponse = new GlobalReponse();


        for (Integer id : ids.getIds()) {
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer delete not found"));
            customerRepository.delete(customer);
            reponse.setMessage(messageSource.getMessage("customer_delete_all", null, LocaleContextHolder.getLocale()));
            reponse.setData(new ArrayList<>());
            reponse.setStatus(HttpStatus.OK.value());
        }


        return reponse;
    }


    /**
     * @param phone1
     * @return
     */
    @Override
    public GlobalReponse getByCustomerPhone(String phone1, String fullName) {
        log.info("Get Customer By Phone Service");
        Customer customerSave = customerRepository.findFirstByPhone1(phone1)
                .orElse(
                        null
                );
        if (customerSave == null) {
            customerSave = customerRepository.save(Customer.builder()
                    .phone1(phone1)
                    .name(fullName)
                    .code(("CUS" + (customerRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth()))
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .build());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(customerMapper.toCustomerDto(customerSave))
                .message(messageSource.getMessage("customer_fetch_success", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse updateCusDebitAmount(CustomerDto customerDto) {

        log.info("Update Customer Debit Amount Service");
        Customer customer = customerRepository.findById(customerDto.getId())
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("customer_notFound", null, LocaleContextHolder.getLocale())));
        try {
            BigDecimal debitAmount = customer.getDebitAmount();
            if (debitAmount == null) debitAmount = BigDecimal.ZERO;
            if (customerDto.getDebitAmount() != null) {
                debitAmount = debitAmount.add(customerDto.getDebitAmount());
                customer.setDebitAmount(debitAmount);
                customerRepository.save(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PerstingObjectException("Error while persisting Customer object");
        }


        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * thanhnc: api private internal service save customer
     * @param listInt
     * @return
     */
    @Override
    public GlobalReponse intSaveERPNext(List<CustomerDto> listInt) {
        log.info("*** function intSaveERPNext ***");
        GlobalReponse response = new GlobalReponse();
        try {
            this.saveListCustomerFromERPNext(listInt);
            response.setMessage("save success");
            response.setStatus(HttpStatus.OK.value());
            response.setData(listInt);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("save error");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setErrors(e.getMessage());
        }
        return response;
    }

    @Override
    public GlobalReponse intSave(List<CustomerDto> listInt) { // dat try catch  Transaction silently rolled back because it has been marked as rollback-only
        log.info("Save All Customer Service");
        listInt.forEach(item->{
            String error = this.integrationService.saveSingleCustomer(item);
            if(error != null && !error.isEmpty())
            {
                ICustomer i = ICustomer.builder()
                        .name(item.getName())
                        .discount(item.getDiscount() != null ? item.getDiscount() : null)
                        .partnerGroupId(item.getPartnerGroupId())
                        .isActive(item.getIsActive())
                        .isPosVip(item.getIsPosVip())
                        .taxCode(item.getTaxCode())
                        .email(item.getEmail())
                        .build();
                i.setTenantId(AuditContext.getAuditInfo().getTenantId());
                i.setErrorMessage(error);
                iCustomerRepository.save(i);
            }
        });
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .errors("")
                .data(listInt)
                .message("All customers saved successfully")
                .build();

    }


    @Transactional
    public void saveListCustomerFromERPNext(List<CustomerDto> param) {
        log.info("function saveListCustomerFromERPNext");
        for (CustomerDto item : param) {
            Customer cusCheck = this.checkCustomerByERPNext(item);
            PartnerGroup group = null;
            if (item.getPartnerGroup() != null) {
                group = partnerGroupRepository.findByErpBpGroupName(item.getPartnerGroup().getErpBpGroupName()).get();
                cusCheck.setPartnerGroupId(group.getId());
            }
            if (cusCheck == null) {
                cusCheck = customerMapper.toCustomer(item);
                cusCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                customerRepository.save(cusCheck);
            } else {
                customerMapper.updateEntity(item, cusCheck);
                customerRepository.save(cusCheck);
            }
        }
    }

    private Customer checkCustomerByERPNext(CustomerDto param) {
        if (param.getId() != null)
            return this.customerRepository.findById(param.getId())
                    .orElse(this.customerRepository.findByErpCustomerName(param.getErpCustomerName())
                            .orElse(null));
        else
            return this.customerRepository.findByErpCustomerName(param.getErpCustomerName())
                    .orElse(null);
    }
//    @Override
//    public GlobalReponse intSave(List<CustomerDto> listInt) {
//
//        log.info("Save All Customer Service");
//
//        StringBuilder errors = new StringBuilder();
//        List<CustomerDto> savedCustomers = new ArrayList<>();
//
//        listInt.forEach(item -> {
//            CustomerDto savedCustomer = saveCustomer(item, errors);
//            if (savedCustomer != null) {
//                savedCustomers.add(savedCustomer);
//            }
//        });
//
//        return GlobalReponse.builder()
//                .status(HttpStatus.OK.value())
//                .errors(errors.toString())
//                .data(savedCustomers)
//                .message(errors.length() == 0 ? "All customers saved successfully" : "Customer saved with errors")
//                .build();
//    }
//
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW )
//    public CustomerDto saveCustomer(CustomerDto item, StringBuilder errors) {
//        try {
//            Customer cusCheck = customerRepository.findByErpCustomerId(item.getErpCustomerId()).orElse(null);
//            PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
//
//            if (cusCheck == null) {
//                if (customerRepository.findFirstByPhone1(item.getPhone1()).isPresent()) {
//                    errors.append("Phone1: ").append(item.getPhone1()).append(" already exists");
//                    return null; // Không lưu nếu số điện thoại đã tồn tại
//                }
//                cusCheck = customerMapper.toCustomer(item);
//                cusCheck.setPartnerGroupId(group.getId());
//                cusCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                cusCheck = customerRepository.saveAndFlush(cusCheck);
//            } else {
//                customerMapper.updateEntity(item, cusCheck);
//                cusCheck.setPartnerGroupId(group.getId());
//                customerRepository.saveAndFlush(cusCheck);
//            }
//
//            modelMapper.map(cusCheck, item);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            errors.append(e.getMessage());
//            return null;
//        }
//        return item;
//    }


//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public CustomerDto saveCustomer(CustomerDto item, StringBuilder errors) {
//        try {
//            Customer cusCheck = customerRepository.findByErpCustomerId(item.getErpCustomerId()).orElse(null);
//            PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
//
//            if (cusCheck == null) {
//                if (customerRepository.findFirstByPhone1(item.getPhone1()).isPresent()) {
//                    errors.append("Phone1: ").append(item.getPhone1()).append(" already exists");
//                    return null; // Không lưu nếu số điện thoại đã tồn tại
//                }
//                cusCheck = customerMapper.toCustomer(item);
//                cusCheck.setPartnerGroupId(group.getId());
//                cusCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//
//                // Sử dụng EntityManager để lưu entity
//                entityManager.persist(cusCheck); // Thêm mới entity
//                entityManager.flush(); // Đẩy các thay đổi xuống database
//            } else {
//                customerMapper.updateEntity(item, cusCheck);
//                cusCheck.setPartnerGroupId(group.getId());
//
//                // Sử dụng EntityManager để cập nhật entity
//                entityManager.merge(cusCheck); // Cập nhật entity
//                entityManager.flush();
//            }
//
//            // Ánh xạ lại từ entity đã lưu vào DTO
//            modelMapper.map(cusCheck, item);
//            TransactionAspectSupport.currentTransactionStatus().flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//            errors.append(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return null;
//        }
//        TransactionAspectSupport.currentTransactionStatus().flush();
//        return item;
//    }


}
