package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.*;
import com.dbiz.app.userservice.helper.CustomerMapper;
import com.dbiz.app.userservice.helper.UserMapper;
import com.dbiz.app.userservice.repository.*;
import com.dbiz.app.userservice.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.OrgDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final MessageSource messageSource;

    private final UserOrgAccessRepository userOrgAccessRepository;

    private final OrgWarehouseAccessRepository orgWarehouseAccessRepository;

    private final ModelMapper modelMapper;

    private final PosTerminalOrgAccessRepository posTerminalOrgAccessRepository;

    private final RoleRepository roleRepository;

    private final UserRoleAccessRepository userRoleAccessRepository;

    private final CustomerRepository customerRepository;

    private final PartnerGroupRepository partnerGroupRepository;

    private final CustomerMapper customerMapper;
    /**
     *
     * @param userIntDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String saveSingleUser(UserIntDto itemUser) {
        try {
            HttpHeaders headersCall = new HttpHeaders();
            headersCall.setContentType(MediaType.APPLICATION_JSON);
            headersCall.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

            HttpEntity<String> entityHeader = new HttpEntity<>(headersCall);

            itemUser.setPassword(passwordEncoder.encode(itemUser.getPassword()));
            User userCheck = userRepository.findByErpUserId(itemUser.getErpUserId());
            if (userCheck == null) {
                userCheck = userMapper.toUserInt(itemUser);
                userCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                log.info("Save user: " + userCheck.getTenantId());
                userCheck = userRepository.save(userCheck);
            } else {
                userCheck = userMapper.updateEntityInt(itemUser, userCheck);
                userRepository.save(userCheck);
            }
            UserDto userSave = userMapper.toUserDto(userCheck);
            Integer userId = userSave.getUserId();
            // luu org access cua user
            itemUser.getUserOrgAccessDtos().forEach(itemOrgAccess -> {
                GlobalReponse exResponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ERP_ID + "/" + itemOrgAccess.getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                if (exResponse.getData() == null)
                    throw new PosException(messageSource.getMessage("org.not.synced", null, LocaleContextHolder.getLocale()));

                OrgDto orgDto = modelMapper.map(exResponse.getData(), OrgDto.class);
                UserOrgAccess userOrgAccess = userOrgAccessRepository.findByUserIdAndOrgId(userId, orgDto.getId());
                if (userOrgAccess == null) {
                    UserOrgAccess userOrgAccessSave = UserOrgAccess.builder()
                            .userId(userId)
                            .orgId(orgDto.getId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    userOrgAccessRepository.save(userOrgAccessSave);
                } else {
                    userOrgAccess.setUserId(userId);
                    userOrgAccess.setOrgId(orgDto.getId());
                    userOrgAccess.setIsActive(itemOrgAccess.getIsActive());
                    userOrgAccessRepository.save(userOrgAccess);
                }

                // luu warehouse
                itemOrgAccess.getOrgWarehouseAccessIntDtos().forEach(itemWarehouse -> {
                    GlobalReponse resWarehouse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_GET_WAREHOUSE_BY_ERP_ID + "/" + itemWarehouse.getWarehouseId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                    if (resWarehouse.getStatus() != HttpStatus.OK.value())
                        throw new PosException(messageSource.getMessage("warehouse.not.synced", null, LocaleContextHolder.getLocale()));
                    WarehouseDto wh = modelMapper.map(resWarehouse.getData(), WarehouseDto.class);
                    OrgWarehouseAccess orgWarehouseAccess = orgWarehouseAccessRepository.findByUserIdAndWarehouseIdAndOrgIdAndTenantId(userId, wh.getId(), orgDto.getId(), AuditContext.getAuditInfo().getTenantId());
                    if (orgWarehouseAccess == null) {
                        OrgWarehouseAccess orgWarehouseAccessSave = OrgWarehouseAccess.builder()
                                .userId(userId)
                                .orgId(orgDto.getId())
                                .warehouseId(wh.getId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        orgWarehouseAccessSave.setIsActive("Y");
                        orgWarehouseAccessRepository.save(orgWarehouseAccessSave);
                    } else {
                        orgWarehouseAccess.setIsActive(itemWarehouse.getIsActive());
                        orgWarehouseAccessRepository.save(orgWarehouseAccess);
                    }
                });

                // luu posterminal theo org + user
                itemOrgAccess.getPosTerminalOrgAccessDtos().forEach(itemPosTerminal -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                    headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                    headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                    headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                    headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
                    headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<PosTerminalDto> requestEntity = new HttpEntity<>(itemPosTerminal.getInfor(), headers);

                    GlobalReponse exResponsePos = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.CHECK_POSTERMINALBYERP_ID, requestEntity, GlobalReponse.class);
                    if (exResponsePos.getData() == null)
                        throw new PosException(messageSource.getMessage("org.not.synced", null, LocaleContextHolder.getLocale()));

                    PosTerminalDto posTerminalDto = modelMapper.map(exResponsePos.getData(), PosTerminalDto.class);
                    PosTerminalOrgAccess posterminalOrgAccess = posTerminalOrgAccessRepository.findByUserIdAndOrgIdAndPosTerminalId(userId, orgDto.getId(), posTerminalDto.getId());
                    if (posterminalOrgAccess == null) {
                        PosTerminalOrgAccess posterminalOrgAccessSave = PosTerminalOrgAccess.builder()
                                .userId(userId)
                                .orgId(orgDto.getId())
                                .posTerminalId(posTerminalDto.getId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        posTerminalOrgAccessRepository.save(posterminalOrgAccessSave);
                    } else {
                        posterminalOrgAccess.setIsActive(itemPosTerminal.getIsActive());
                        posTerminalOrgAccessRepository.save(posterminalOrgAccess);
                    }
                });


            });

            // xu ly role access cua user
            itemUser.getUserRoleAccessDtos().forEach(itemRoleAccess -> {
                Role roleCheck = roleRepository.findByErpRoleId(itemRoleAccess.getRoleId());
                if (roleCheck != null) {
                    UserRoleAccess userRoleAccess = userRoleAccessRepository.findByUserIdAndRoleId(userId, roleCheck.getId());
                    if (userRoleAccess == null) {
                        UserRoleAccess userRoleAccessSave = UserRoleAccess.builder()
                                .userId(userId)
                                .roleId(roleCheck.getId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        userRoleAccessRepository.save(userRoleAccessSave);
                    } else {
                        userRoleAccess.setIsActive(itemRoleAccess.getIsActive());
                        userRoleAccessRepository.save(userRoleAccess);

                    }
                }
            });

        } catch (Exception e) {
            log.info("error save User : " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Throwable root = e;
            while (root.getCause() != null) {
                return ExceptionUtils.getRootCauseMessage(e);
            }

            return e.getMessage();
        }
        return null;
    }

    /**
     *
     * @param paramCustomerDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String saveSingleCustomer(CustomerDto item) {
        try {
            Customer cusCheck = customerRepository.findByErpCustomerId(item.getErpCustomerId()).orElse(null);
            PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
            if (cusCheck == null) {
                cusCheck = customerMapper.toCustomer(item);
                cusCheck.setPartnerGroupId(group.getId());
                cusCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                cusCheck = customerRepository.saveAndFlush(cusCheck);
                modelMapper.map(cusCheck, item);
            } else {
                customerMapper.updateEntity(item, cusCheck);
                cusCheck.setPartnerGroupId(group.getId());
                customerRepository.saveAndFlush(cusCheck);
            }
        } catch (Exception e) {
            log.info("error save Customer : " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Throwable root = e;
            while (root.getCause() != null) {
                return ExceptionUtils.getRootCauseMessage(e);
            }

            return e.getMessage();
        }


        return null;
    }
}
