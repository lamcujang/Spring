package com.dbiz.app.userservice.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import com.dbiz.app.tenantservice.domain.*;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.BrandPartnerRepository;
import com.dbiz.app.tenantservice.repository.OrgRepository;
import com.dbiz.app.tenantservice.repository.TenantRepository;
import com.dbiz.app.tenantservice.service.TenantService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.*;
import com.dbiz.app.userservice.domain.view.GetUserOrgAccessV;
import com.dbiz.app.userservice.domain.view.GetUserRoleAccessV;
import com.dbiz.app.userservice.domain.view.PosterminalOrgAccessV;
import com.dbiz.app.userservice.exception.UserServiceException;
import com.dbiz.app.userservice.helper.DateHelper;
import com.dbiz.app.userservice.helper.UserMapper;
import com.dbiz.app.userservice.repository.*;
import com.dbiz.app.userservice.service.ImageService;
import com.dbiz.app.userservice.service.IntegrationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.IdentityDto.Tenant.IDTenantLoginInfoDto;
import org.common.dbiz.dto.IdentityDto.User.IDLoginSocialDto;
import org.common.dbiz.dto.IdentityDto.User.IDPhoneListDto;
import org.common.dbiz.dto.IdentityDto.User.IDUserDto;
import org.common.dbiz.dto.integrationDto.FloorKafkaDto;
import org.common.dbiz.dto.integrationDto.KitchenOrder.KitchenOrderLineDto;
import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.integrationDto.UserIntKafkaDto;
import org.common.dbiz.dto.integrationDto.posOrder.PaymentInfoDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.dto.paymentDto.response.PaymentRespDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.CreateOrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.dto.userDto.*;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.exception.wrapper.UserObjectNotFoundException;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.UserQueryRequest;
import com.dbiz.app.userservice.specification.UserSpecification;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.dbiz.app.userservice.helper.UserMappingHelper;
import com.dbiz.app.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@DependsOn("dataSourceRouting")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TenantRepository tenantRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final RequestParamsUtils requestParamsUtils;

    private final UserRoleAccessRepository userRoleAccessRepository;

    private final UserOrgAccessRepository userOrgAccessRepository;

    private final PosTerminalOrgAccessRepository posTerminalOrgAccessRepository;

    private final GetUserRoleAccessVRepository getUserRoleAccessVRepository;

    private final GetUserOrgAccessVRepository getUserOrgAccessVRepository;

    private final PosterminalOrgAccessVRepository posterminalOrgAccessVRepository;

    private final RoleRepository roleRepository;

    private final EntityManager entityManager;

    private final OrgWarehouseAccessRepository orgWarehouseAccessRepository;

    private final IntegrationService integrationService;

    private final IUserRepository iUserRepository ;
    private StringBuilder sql;

    private StringBuilder error = new StringBuilder();

    private final ObjectMapper objectMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;


    private final static String TOPIC_SEND_CUS = "sync-integration-to-user";

    private final static String TOPIC_RECEIVE_CUS = "sync-user-to-integration";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";

    private final ImageService imageService;
    private final OrgRepository orgRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final UserOtherRepository userOtherRepository;
    private final BrandPartnerRepository brandPartnerRepository;

    @Override
    public List<UserDto> findAll() {
        log.info("*** UserDto List, service; fetch all users *");
        return this.userRepository.findAll()
                .stream()
                .map(UserMappingHelper::map)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserDto findById(final Integer userId) {
        log.info("*** UserDto, service; fetch user by id *");
        return this.userRepository.findById(userId)
                .map(UserMappingHelper::map)
                .orElseThrow(() -> new UserObjectNotFoundException(String.format("User with id: %d not found", userId)));
    }

    @Override
    @Transactional
    public UserDto save(final UserDto userDto) {

//		TenantDto tenantDto =	this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
//				.TENANT_SERVICE_API_FIND_ID + "/" + AuditContext.getAuditInfo().getTenantId(), TenantDto.class);

        log.info("*** UserDto, service; save user *");
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User uSave = userMapper.toUser(userDto);
        uSave.setTenantId(userDto.getTenantId());

        User saveUser = this.userRepository.save(uSave);

        Role role = roleRepository.findByCodeAndTenantId("ADM", AuditContext.getAuditInfo().getTenantId()).get();


        UserRoleAccess userRoleAccess = UserRoleAccess.builder()
                .userId(saveUser.getUserId())
                .roleId(role.getId())
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .build();
        userRoleAccessRepository.save(userRoleAccess);

//        UserOrgAccess userOrgAccess = UserOrgAccess.builder()
//                .userId(saveUser.getUserId())
//                .orgId(0)
//                .tenantId(AuditContext.getAuditInfo().getTenantId())
//                .build();
//        userOrgAccessRepository.save(userOrgAccess);
//
//        PosTerminalOrgAccess posTerminalOrgAccess = PosTerminalOrgAccess.builder()
//                .userId(saveUser.getUserId())
//                .orgId(0)
//                .posTerminalId(0)
//                .tenantId(AuditContext.getAuditInfo().getTenantId())
//                .build();
//        posTerminalOrgAccessRepository.save(posTerminalOrgAccess);

        if (userDto.getIsTenantAdmin() != null && userDto.getIsTenantAdmin().equals("Y")) {

            try {
                String listOrgJson = "[]";
                if (userDto.getOrgs() != null && userDto.getOrgs().size() > 0) {
                    listOrgJson = objectMapper.writeValueAsString(userDto.getOrgs());
                }
                log.info("listOrgJson: {}", listOrgJson);
                String numberTableCode = userDto.getNumberOfTables();
                if (numberTableCode == null || numberTableCode.isEmpty()) {
                    numberTableCode = "";
                }
                Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_pos_masterdata_gen(:industryCode, :numberTableCode, cast(:listOrg as jsonb))");
                queryProcedure.setParameter("industryCode", userDto.getIndustryCode());
                queryProcedure.setParameter("numberTableCode", numberTableCode);
                queryProcedure.setParameter("listOrg", listOrgJson);
                queryProcedure.executeUpdate();

                entityManager.flush();

                initLogoOrg(userDto);
                initOrgAccess(saveUser);

                initPosTerminalAccess(saveUser);

                initWarehouseAccess(saveUser);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return userMapper.toUserDto(saveUser);
    }

    public void initLogoOrg(UserDto saveUser) {

        if (saveUser.getOrgs() != null && saveUser.getOrgs().size() > 0) {
            Map<String, Org> orgMap = new HashMap<>();
            List<Org> orgEntity = orgRepository.findAll();
            for (Org org : orgEntity) {
                orgMap.put(org.getName(), org);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);

            for (CreateOrgDto org : saveUser.getOrgs()) {
                if (org.getImage() != null) {
                    HttpEntity<ImageDto> requestEntity = new HttpEntity<>(org.getImage(), headers);
                    GlobalReponse exResponse = restTemplate.postForObject(com.dbiz.app.tenantservice.constant.AppConstant.DiscoveredDomainsApi.SAVE_IMAGE, requestEntity, GlobalReponse.class);
                    ImageDto image = modelMapper.map(exResponse.getData(), ImageDto.class);
                    Org o = orgMap.get(org.getName());
                    o.setImageId(image.getId());
                    orgRepository.save(o);

                }
            }
        }

    }

    public void initOrgAccess(User saveUser) {

        String sql = "SELECT d_org_id " +
                " FROM pos.d_org WHERE d_tenant_id = :tenantId ";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<KitchenOrderLineDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            UserOrgAccess userOrgAccess = UserOrgAccess.builder()
                    .userId(saveUser.getUserId())
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .build();
            userOrgAccessRepository.save(userOrgAccess);
        }
    }

    public void initPosTerminalAccess(User saveUser) {

        String sql = "SELECT d_org_id,d_pos_terminal_id " +
                " FROM pos.d_pos_terminal WHERE d_tenant_id = :tenantId ";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<KitchenOrderLineDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            PosTerminalOrgAccess posTerminalOrgAccess = PosTerminalOrgAccess.builder()
                    .userId(saveUser.getUserId())
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .posTerminalId(ParseHelper.INT.parse(row.get("d_pos_terminal_id")))
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .build();
            posTerminalOrgAccessRepository.save(posTerminalOrgAccess);
        }
    }


    public void initWarehouseAccess(User saveUser) {

        String sql = "SELECT d_org_id,d_warehouse_id " +
                " FROM pos.d_warehouse WHERE d_tenant_id = :tenantId ";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<KitchenOrderLineDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            OrgWarehouseAccess orgWarehouseAccess = OrgWarehouseAccess.builder()
                    .userId(saveUser.getUserId())
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .isActive("Y")
                    .build();
            orgWarehouseAccessRepository.save(orgWarehouseAccess);
        }
    }

    @Override

    @Transactional
    public UserDto update(final UserDto userDto) {
        log.info("*** UserDto, service; update user *");
        if (userDto.getUserId() != null) {
            User userUpdate = this.userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new UserObjectNotFoundException(String.format("User with id: %d not found", userDto.getUserId())));
            modelMapper.map(userDto, userUpdate);
            this.userRepository.save(userUpdate);
            return UserMappingHelper.map(userUpdate);
        }
        return UserMappingHelper.map(this.userRepository.save(UserMappingHelper.map(userDto)));
    }

    @Override

    @Transactional
    public UserDto update(final Integer userId, final UserDto userDto) {
        log.info("*** UserDto, service; update user with userId *");
        return UserMappingHelper.map(this.userRepository.save(
                UserMappingHelper.map(this.findById(userId))));
    }

    @Override

    @Transactional
    public void deleteById(final Integer userId) {
        log.info("*** Void, service; delete user by id *");
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDto findByUsername(final String username) {
        log.info("*** UserDto, service; fetch user with username *");
        Optional<User> user = this.userRepository.findByUserName(username);
        if (user.isEmpty()) {
            throw new UserObjectNotFoundException(String.format("User with username: %s not found", username));
        }

        return UserMappingHelper.map(this.userRepository.findByUserName(username)
                .orElseThrow(() -> new UserObjectNotFoundException(String.format("User with username: %s not found", username))));
    }

    @Override
    public UserDto findByUsernameAndDTenantId(String username, Integer tenantId) {
        log.info("*** UserDto, service; fetch user with username and tenant Id  *");


        Optional<User> user = this.userRepository.findByUserNameAndDTenantId(username, tenantId);
        if (user.isEmpty()) {
            throw new UserObjectNotFoundException(String.format("User with username: %s - %s not found", username, tenantId));
        }

        return UserMappingHelper.map(this.userRepository.findByUserNameAndDTenantId(username, tenantId)
                .orElseThrow(() -> new UserObjectNotFoundException(String.format("User with username: %s not found", username))));

    }

    @Override
    public UserLoginDto findByUserNdTenantPass(String username, String password, Integer tenantId) {
        // chon role
        // chon org
        // chon posterminal
        log.info("*** UserDto, service; fetch user with username and password and tenant Id  *");
        Optional<User> user = this.userRepository.findByUserNameAndDTenantId(username, tenantId);
        if (user.isEmpty()) {
//            throw new UserServiceException(String.format(messageSource.getMessage("user_notFound", null, LocaleContextHolder.getLocale()), username));
            throw new UserServiceException(String.format(messageSource.getMessage("incorrect_username_or_password", null, LocaleContextHolder.getLocale()), username));
        } else {
            log.info("Password 1: " + passwordEncoder.encode(password));
            log.info("Password 1: " + user.get().getPassword());
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                UserLoginDto userLogin = modelMapper.map(user.get(), UserLoginDto.class);
                // lay thong tin truy cap
                List<GetUserRoleAccessV> userRoleAccess = getUserRoleAccessVRepository.findAllByUserId(userLogin.getUserId());
                userLogin.setRoleAccess(userRoleAccess.stream().map((element) -> modelMapper.map(element, GetUserRoleAccessVDto.class)).collect(Collectors.toList()));

                List<GetUserOrgAccessV> userOrgAccesses = getUserOrgAccessVRepository.findAllByUserId(userLogin.getUserId());
                userLogin.setOrgAccess(userOrgAccesses.stream().map((element) -> modelMapper.map(element, GetUserOrgAccessVDto.class)).collect(Collectors.toList()));

                Tenant tenant = this.tenantRepository.findById(0).orElseThrow(()->
                        new ObjectNotFoundException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale())));

                userLogin.getTenant().setAgentCode(tenant.getAgentCode());
                userLogin.getTenant().setExpiredDate(tenant.getExpiredDate());

                if (tenant.getAgentCode() != null) {

                    dataSourceContextHolder.setCurrentTenantId(null);
                    BrandPartner brandPartner = brandPartnerRepository.findByCodeAndIsActive(tenant.getAgentCode(), "Y").orElse(null);
                    dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
                    if (brandPartner != null) {

                        userLogin.getTenant().setLogoUrl(brandPartner.getLogoUrl());
                    }
                }

                if (userOrgAccesses.size() == 1) {
                    List<PosterminalOrgAccessV> posTerminalOrgAccesses = posterminalOrgAccessVRepository.findAllByUserIdAndOrgId(userLogin.getUserId(), userOrgAccesses.get(0).getOrgId());
                    if (!posTerminalOrgAccesses.isEmpty())
                        userLogin.setPosTerminalAccess(posTerminalOrgAccesses.stream().map((element) -> modelMapper.map(element, PosterminalOrgAccessVDto.class)).collect(Collectors.toList()));
                    else
                        userLogin.setPosTerminalAccess(new ArrayList<>());

                    if (userRoleAccess.size() == 1 && userOrgAccesses.size() == 1) {
                        if (userRoleAccess.get(0).getCode().equals("SSF") || userRoleAccess.get(0).getCode().equals("KST")) {
                            sql = new StringBuilder("select d_tenant_id, d_org_id, d_warehouse_id, d_user_id, is_active, created, created_by, updated, updated_by, name, description from d_org_warehouse_access_v " +
                                    " where d_user_id = :userId and d_org_id = :orgId and d_user_id = :userId and is_active = 'Y' and d_tenant_id = :tenantId");
                            List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                                    .setParameter("userId", userRoleAccess.get(0).getUserId())
                                    .setParameter("orgId", userOrgAccesses.get(0).getOrgId())
                                    .setParameter("tenantId", tenantId)
                                    .unwrap(NativeQuery.class)
                                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                    .getResultList();
                            List<OrgWarehouseAccessDto> resultDto = new ArrayList<>();
                            for (Map<String, Object> row : results) {
                                OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                                        .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                                        .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                                        .nameWarehouse(ParseHelper.STRING.parse(row.get("name")))
                                        .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                                        .build();
                                resultDto.add(accessDto);
                            }
                            userLogin.setOrgWarehouseAccessDto(resultDto);
                        }
                    }

                }

                GetTokenRespDto tokens = getToken(username, password, userLogin.getUserId());
                userLogin.setAccessToken(tokens.getAccessToken());
                userLogin.setRefreshToken(tokens.getRefreshToken());
//                userLogin.setAccessTokenExp(tokens.getAccessTokenExp());

                if (userLogin != null) {
                    if (isFirstLogin()) {
                        userLogin.setIsFirstLogin("Y");
                    } else {
                        userLogin.setIsFirstLogin("N");
                    }
                }
                return userLogin;
            } else {
                throw new UserServiceException(messageSource.getMessage("incorrect_username_or_password", null, LocaleContextHolder.getLocale()));
            }
        }
    }


    @Override
    public UserLoginDto findBySocial(UserDto userLoginDto, Integer tenantId) {
        // chon role
        // chon org
        // chon posterminal
        log.info("*** UserDto, service; fetch user with username and password and tenant Id  *");
        Optional<User> user = null;

        if (userLoginDto.getGoogleId() != null
                && !userLoginDto.getGoogleId().isEmpty() && !userLoginDto.getGoogleId().isBlank()) {

            user = this.userRepository.findByGoogleIdAndTenantId(userLoginDto.getGoogleId(), tenantId);

        } else if (userLoginDto.getZaloId() != null
                && !userLoginDto.getZaloId().isEmpty() && !userLoginDto.getZaloId().isBlank()) {

            user = this.userRepository.findByZaloIdAndTenantId(userLoginDto.getZaloId(), tenantId);

        } else if (userLoginDto.getFacebookId() != null
                && !userLoginDto.getFacebookId().isEmpty() && !userLoginDto.getFacebookId().isBlank()) {

            user = this.userRepository.findByFacebookIdAndTenantId(userLoginDto.getFacebookId(), tenantId);

        } else if (userLoginDto.getAppleId() != null
                && !userLoginDto.getAppleId().isEmpty() && !userLoginDto.getAppleId().isBlank()) {

            user = this.userRepository.findByAppleIdAndTenantId(userLoginDto.getAppleId(), tenantId);

        }


        if (user == null || user.isEmpty()) {
            throw new UserServiceException(messageSource.getMessage("not.registered.social", null, LocaleContextHolder.getLocale()));
        } else {

            UserLoginDto userLogin = modelMapper.map(user.get(), UserLoginDto.class);
            // lay thong tin truy cap
            List<GetUserRoleAccessV> userRoleAccess = getUserRoleAccessVRepository.findAllByUserId(userLogin.getUserId());
            userLogin.setRoleAccess(userRoleAccess.stream().map((element) -> modelMapper.map(element, GetUserRoleAccessVDto.class)).collect(Collectors.toList()));

            List<GetUserOrgAccessV> userOrgAccesses = getUserOrgAccessVRepository.findAllByUserId(userLogin.getUserId());
            userLogin.setOrgAccess(userOrgAccesses.stream().map((element) -> modelMapper.map(element, GetUserOrgAccessVDto.class)).collect(Collectors.toList()));

            if (userOrgAccesses.size() == 1) {
                List<PosterminalOrgAccessV> posTerminalOrgAccesses = posterminalOrgAccessVRepository.findAllByUserIdAndOrgId(userLogin.getUserId(), userOrgAccesses.get(0).getOrgId());
                if (!posTerminalOrgAccesses.isEmpty())
                    userLogin.setPosTerminalAccess(posTerminalOrgAccesses.stream().map((element) -> modelMapper.map(element, PosterminalOrgAccessVDto.class)).collect(Collectors.toList()));
                else
                    userLogin.setPosTerminalAccess(new ArrayList<>());

                if (userRoleAccess.size() == 1 && userOrgAccesses.size() == 1) {
                    if (userRoleAccess.get(0).getCode().equals("SSF") || userRoleAccess.get(0).getCode().equals("KST")) {
                        sql = new StringBuilder("select d_tenant_id, d_org_id, d_warehouse_id, d_user_id, is_active, created, created_by, updated, updated_by, name, description from d_org_warehouse_access_v " +
                                " where d_user_id = :userId and d_org_id = :orgId and d_user_id = :userId and is_active = 'Y' and d_tenant_id = :tenantId");
                        List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                                .setParameter("userId", userRoleAccess.get(0).getUserId())
                                .setParameter("orgId", userOrgAccesses.get(0).getOrgId())
                                .setParameter("tenantId", tenantId)
                                .unwrap(NativeQuery.class)
                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                .getResultList();
                        List<OrgWarehouseAccessDto> resultDto = new ArrayList<>();
                        for (Map<String, Object> row : results) {
                            OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                                    .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                                    .nameWarehouse(ParseHelper.STRING.parse(row.get("name")))
                                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                                    .build();
                            resultDto.add(accessDto);
                        }
                        userLogin.setOrgWarehouseAccessDto(resultDto);
                    }
                }

            }

            GetTokenRespDto tokens = getToken(userLogin.getUserName(), null, userLogin.getUserId());
            userLogin.setAccessToken(tokens.getAccessToken());
            userLogin.setRefreshToken(tokens.getRefreshToken());
//            userLogin.setAccessTokenExp(tokens.getAccessTokenExp());

            return userLogin;

        }
    }

    /**
     * luu org access cho user admin
     * @param userDto
     * @return
     */
    @Override

    @Transactional
    public GlobalReponse saveOrgAccess(UserDto userDto) {
        log.info("** Save Org Access Service * ");
        UserOrgAccess userOrgAccess = UserOrgAccess.builder()
                .userId(userDto.getUserId())
                .orgId(userDto.getOrgId())
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .build();
        userOrgAccessRepository.save(userOrgAccess);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .errors("").build();
    }

    @Override
    public GlobalReponse createVariousUser(VariousUserDto userDto) {

        log.info("** Save Various User * ");

        if (userDto.getUserGroup() == null || userDto.getUserGroup().isEmpty()) {
            throw new PosException(messageSource.getMessage("user.group.not.allow.empty", null, LocaleContextHolder.getLocale()));
        }

        try {
            if (userDto.getUserGroup().equals("CUS")) {
                createCustomer(userDto);
            } else if (userDto.getUserGroup().equals("VEN")) {
                createVendor(userDto);
            } else if (userDto.getUserGroup().equals("EMP")) {
                createUser(userDto);
            } else if (userDto.getUserGroup().equals("OTH")) {
                createUserOther(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(userDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("").build();
    }

    @Override
    public GlobalReponsePagination getVariousUser(VariousUserParamDto dto) {
        log.info("*** User, service; fetch all various user ***");

        StringBuilder sql = new StringBuilder(
                "SELECT id, name, " +
                        "code, " +
                        "phone," +
                        "user_group" +
                        " FROM pos.d_various_user_v  WHERE is_active = 'Y' "

        );

        if (dto.getId() != null) {
            sql.append(" and id = :id ");
        }

        if (dto.getCode() != null) {
            sql.append(" and lower(code) like lower( '%' || :code  || '%' ) ");
        }

        if (dto.getName() != null) {
            sql.append(" and lower(name) like lower( '%' || :name  || '%' ) ");
        }

        if (dto.getPhone() != null) {
            sql.append(" and phone like  '%' || :phone  || '%' ");
        }

        if (dto.getUserGroup() != null) {
            sql.append(" and user_group like  '%' || :userGroup  || '%' ");
        }

        sql.append(" ORDER BY name asc");
        sql.append(" LIMIT :limit OFFSET :offset ");

        Query userQuery = entityManager.createNativeQuery(sql.toString());


        if (dto.getId() != null) {
            userQuery.setParameter("id", dto.getId());
        }


        if (dto.getCode() != null) {
            userQuery.setParameter("code", dto.getCode());
        }

        if (dto.getName() != null) {
            userQuery.setParameter("name", dto.getName());
        }

        if (dto.getPhone() != null) {
            userQuery.setParameter("phone", dto.getPhone());
        }

        if (dto.getUserGroup() != null) {
            userQuery.setParameter("userGroup", dto.getUserGroup());
        }


        userQuery.setParameter("limit", dto.getPageSize());
        userQuery.setParameter("offset", dto.getPage());


        // Fetch the paginated results
        List<Map<String, Object>> results = userQuery.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<VariousUserDto> list = new ArrayList<>();

        for (Map<String, Object> result : results) {

            VariousUserDto item = VariousUserDto.builder()
                    .id(ParseHelper.INT.parse(result.get("id")))
                    .code(ParseHelper.STRING.parse(result.get("code")))
                    .name(ParseHelper.STRING.parse(result.get("name")))
                    .phone(ParseHelper.STRING.parse(result.get("phone")))
                    .userGroup(ParseHelper.STRING.parse(result.get("user_group")))
                    .build();
            list.add(item);
        }


        StringBuilder countSql = new StringBuilder("SELECT count(1) FROM pos.d_various_user_v  WHERE is_active = 'Y' "
        );

        if (dto.getId() != null) {
            countSql.append(" and id = :id ");
        }

        if (dto.getCode() != null) {
            countSql.append(" and lower(code) like lower( '%' || :code  || '%' ) ");
        }

        if (dto.getName() != null) {
            countSql.append(" and lower(name) like lower( '%' || :name  || '%' ) ");
        }

        if (dto.getPhone() != null) {
            countSql.append(" and phone like  '%' || :phone  || '%' ");
        }

        if (dto.getUserGroup() != null) {
            countSql.append(" and user_group like  '%' || :userGroup  || '%' ");
        }


        // Get the total number of records
        Query totalRecordsQuery = entityManager.createNativeQuery(countSql.toString());

        if (dto.getId() != null) {
            totalRecordsQuery.setParameter("id", dto.getId());
        }


        if (dto.getCode() != null) {
            totalRecordsQuery.setParameter("code", dto.getCode());
        }

        if (dto.getName() != null) {
            totalRecordsQuery.setParameter("name", dto.getName());
        }

        if (dto.getPhone() != null) {
            totalRecordsQuery.setParameter("phone", dto.getPhone());
        }

        if (dto.getUserGroup() != null) {
            totalRecordsQuery.setParameter("userGroup", dto.getUserGroup());
        }


        Long totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();


        return GlobalReponsePagination.builder()
                .data(list)
                .pageSize(dto.getPageSize())
                .currentPage(dto.getPage())
                .totalItems(totalRecords)
                .totalPages((int) Math.ceil((double) totalRecords / dto.getPageSize()))
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /** thanhnc: test exception handling
     *
     * @param listUser
     * @return
     */
    @Override
    public GlobalReponse handleEx(List<UserDto> listUser) {
        String Status = "";
        List<UserDto> lisFailed = new ArrayList<>();
        if (listUser.size() == 2) {
            for (UserDto i : listUser
            ) {
                try {
                    this.userRepository.saveAndFlush(UserMappingHelper.map(i));
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Status = "FAI";
                }
            }
        } else if (listUser.size() > 3 && listUser.size() < 5) {
            try {
                for (UserDto i : listUser
                ) {
                    this.userRepository.saveAndFlush(UserMappingHelper.map(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Status = "FAI";
            }

        } else {
            for (UserDto i : listUser) {
                this.saveSigUser(i);
            }
        }

        return GlobalReponse.builder().status(HttpStatus.CREATED.value())
                .message("Status")
                .errors("").build();
    }

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSigUser(UserDto param) {
        try {
            this.userRepository.saveAndFlush(UserMappingHelper.map(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createCustomer(VariousUserDto dto) {

        log.info("** Create Customer * ");
        List<Customer> customers = customerRepository.findByPhone1(dto.getPhone());
        if (!customers.isEmpty())
            throw new PosException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));
        Integer maxId = customerRepository.getMaxId();
        String code = DocHelper.generateDocNo("CUS", maxId + 1);
        Customer customer = Customer.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .code(code)
                .name(dto.getName())
                .phone1(dto.getPhone())
                .address1(dto.getAddress())
                .city(dto.getCity())
                .wards(dto.getWards())
                .description(dto.getDescription())
                .build();
        customer = customerRepository.save(customer);
        dto.setCode(customer.getCode());
        dto.setId(customer.getId());
    }

    public void createVendor(VariousUserDto dto) {

        log.info("** Create Vendor * ");
        List<Vendor> vendors = vendorRepository.findByPhone1(dto.getPhone());
        if (!vendors.isEmpty())
            throw new PosException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));
        Integer maxId = vendorRepository.getMaxId();
        String code = DocHelper.generateDocNo("VEN", maxId + 1);
        Vendor vendor = Vendor.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .code(code)
                .name(dto.getName())
                .phone1(dto.getPhone())
                .address1(dto.getAddress())
                .city(dto.getCity())
                .wards(dto.getWards())
                .description(dto.getDescription())
                .build();
        vendor = vendorRepository.save(vendor);
        dto.setCode(vendor.getCode());
        dto.setId(vendor.getId());
    }


    public void createUser(VariousUserDto dto) {

        log.info("** Create User * ");
        List<User> users = userRepository.findAllByPhone(dto.getPhone());
        if (!users.isEmpty())
            throw new PosException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));
        User user = User.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .userName(dto.getPhone())
                .fullName(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .city(dto.getCity())
                .wards(dto.getWards())
                .description(dto.getDescription())
                .build();
        user = userRepository.save(user);
        dto.setCode(user.getUserName());
        dto.setId(user.getUserId());
    }

    public void createUserOther(VariousUserDto dto) {

        log.info("** Create User * ");
        List<UserOther> users = userOtherRepository.findAllByPhone(dto.getPhone());
        if (!users.isEmpty())
            throw new PosException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));
        Integer maxId = userOtherRepository.getMaxId();
        String code = DocHelper.generateDocNo("UTH", maxId + 1);
        UserOther user = UserOther.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .code(code)
                .name(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .city(dto.getCity())
                .wards(dto.getWards())
                .description(dto.getDescription())
                .build();
        user = userOtherRepository.save(user);
        dto.setCode(user.getCode());
        dto.setId(user.getId());
    }


    public GetTokenRespDto getToken(String userName, String password, Integer userId) {


        try {
            GetTokenDto dto = GetTokenDto.builder()
                    .username(userName)
                    .password(password)
                    .language(AuditContext.getAuditInfo().getLanguage())
                    .d_tenant_id(AuditContext.getAuditInfo().getMainTenantId())
                    .userId(userId)
                    .d_org_id(0)
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GetTokenDto> requestEntity = new HttpEntity<>(dto, headers);

            GetTokenRespDto response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.GET_TOKEN, requestEntity, GetTokenRespDto.class);

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            throw new PosException("Failed to get user info");
        }
        return null;
    }

    @Override
    public GlobalReponse findByIdRes(Integer id) {
        log.info("** Find User Service * ");
        GlobalReponse reponse = new GlobalReponse();
        User user = this.userRepository.findByUserIdAndTenantId(id, AuditContext.getAuditInfo().getTenantId()).orElseThrow(() -> new RuntimeException(messageSource.getMessage("vendor_notFound", null, LocaleContextHolder.getLocale())));

        UserLoginDto dto = UserLoginDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .image(user.getImage() != null ? modelMapper.map(user.getImage(), ImageDto.class) : null)
                .email(user.getEmail())
                .phone(user.getPhone())
                .erpUserId(user.getErpUserId())
                .build();
        List<GetUserRoleAccessV> userRoleAccess = getUserRoleAccessVRepository.findAllByUserId(dto.getUserId());
        dto.setRoleAccess(userRoleAccess.stream().map((element) -> modelMapper.map(element, GetUserRoleAccessVDto.class)).collect(Collectors.toList()));

        List<GetUserOrgAccessV> userOrgAccesses = getUserOrgAccessVRepository.findAllByUserId(dto.getUserId());
        dto.setOrgAccess(userOrgAccesses.stream().map((element) -> modelMapper.map(element, GetUserOrgAccessVDto.class)).collect(Collectors.toList()));

        if (userOrgAccesses.size() == 1) {
            List<PosterminalOrgAccessV> posTerminalOrgAccesses = posterminalOrgAccessVRepository.findAllByUserIdAndOrgId(dto.getUserId(), userOrgAccesses.get(0).getOrgId());
            if (!posTerminalOrgAccesses.isEmpty())
                dto.setPosTerminalAccess(posTerminalOrgAccesses.stream().map((element) -> modelMapper.map(element, PosterminalOrgAccessVDto.class)).collect(Collectors.toList()));
            else
                dto.setPosTerminalAccess(new ArrayList<>());

            // neu chi 1 role va la serve staff hoac kitchen staff
            if (userRoleAccess.size() == 1) {
                GetUserRoleAccessV userRole = userRoleAccess.get(0);
                if (userRole.getCode().equals("SSF") || userRole.getCode().equals("KST")) {
                    sql = new StringBuilder("select d_tenant_id, d_org_id, d_warehouse_id, d_user_id, is_active, created, created_by, updated, updated_by, name, description from d_org_warehouse_access_v " +
                            " where d_user_id = :userId and d_org_id = :orgId and d_user_id = :userId and is_active = 'Y' and d_tenant_id = :tenantId");
                    List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                            .setParameter("userId", userRole.getUserId())
                            .setParameter("orgId", userOrgAccesses.get(0).getOrgId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getUserId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    List<OrgWarehouseAccessDto> resultDto = new ArrayList<>();
                    for (Map<String, Object> row : results) {
                        OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                                .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                                .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                                .nameWarehouse(ParseHelper.STRING.parse(row.get("name")))
                                .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                                .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                                .build();
                        resultDto.add(accessDto);
                    }
                    dto.setOrgWarehouseAccessDto(resultDto);
                }
            }
        }
        reponse.setData(dto);
        reponse.setMessage(messageSource.getMessage("user_find_success", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    @Transactional
    public GlobalReponsePagination findAll(UserQueryRequest request) {
        log.info("** Find All User Service *");
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<User> spec = UserSpecification.getEntitySpecification(request);
        if (request.getRoleSearch() != null) {
            List<Integer> userIds = this.userRoleAccessRepository.findByRoleId(request.getRoleSearch());
            spec = spec.and((root, query, criteriaBuilder) -> root.get("userId").in(userIds));
        }
        if (request.getOrgId() != null) {
            List<Integer> userIds = this.userOrgAccessRepository.findByOrgId(request.getOrgId());
            spec = spec.and((root, query, criteriaBuilder) -> root.get("userId").in(userIds));
        }
        Page<User> users = userRepository.findAll(spec, pageable);
        // get user org access
        List<UserDto> listData = new ArrayList<>();
        for (User item : users.getContent()) {
            List<UserOrgAccessDto> userOrgAccessDtos = new ArrayList<>();
            Role roleCheck = this.roleRepository.findById(request.getRoleId()).get();
            if (roleCheck.getCode().equals(AppConstant.ROLECODE.ROLE_ADMIN)) {
                sql = new StringBuilder("select dog.d_org_id, dog.code,dog.name,dog.phone,dog.wards ,dua.d_user_id,dog.d_tenant_id,dog.is_active, dua.is_active as is_assign \n" +
                        "from d_org dog\n" +
                        "         left join d_userorg_access dua  on dog.d_org_id = dua.d_org_id  and dua.d_user_id = :userId and dog.d_tenant_id = :tenantId ;");
            } else {
                sql = new StringBuilder("SELECT dog.d_org_id, dog.code, dog.name, dog.phone, dog.wards, dua.d_user_id, dog.d_tenant_id, dog.is_active, dua.is_active as is_assign   \n" +
                        "FROM (select drg.d_org_id,drg.name, drg.code, drg.phone, drg.wards, drg.d_tenant_id, drg.is_active from d_org drg\n" +
                        "      join d_userorg_access dua1 on drg.d_org_id = dua1.d_org_id and dua1.d_user_id = :currentUserId and drg.d_tenant_id = :tenantId) dog\n" +
                        "LEFT JOIN d_userorg_access dua on dog.d_org_id = dua.d_org_id\n" +
                        "     AND dua.d_user_id = :userId\n" +
                        "     AND dog.d_tenant_id = :tenantId");

            }
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter("userId", item.getUserId());
            query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
            if (!roleCheck.getCode().equals(AppConstant.ROLECODE.ROLE_ADMIN)) {
                query.setParameter("currentUserId", request.getCurrentUserId());
            }
            query.unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<Map<String, Object>> results = query.getResultList();
            for (Map<String, Object> row : results) {

                UserOrgAccessDto accessDto = UserOrgAccessDto.builder()
                        .orgWards(ParseHelper.STRING.parse(row.get("wards")))
                        .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                        .orgCode(ParseHelper.STRING.parse(row.get("code")))
                        .orgName(ParseHelper.STRING.parse(row.get("name")))
                        .orgPhone(ParseHelper.STRING.parse(row.get("phone")))
                        .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                        .isActive(ParseHelper.STRING.parse(row.get("is_active"))) //
                        .isAssign((row.get("d_user_id") != null && ParseHelper.STRING.parse(row.get("is_assign")).equals("Y")) ? "Y" : "N")
                        .build();
                userOrgAccessDtos.add(accessDto);
            }

            //get user role access
            List<UserRoleAccessDto> userRoleAccessDtos = new ArrayList<>();
            if (roleCheck.getCode().equals(AppConstant.ROLECODE.ROLE_ADMIN)) {
                sql = new StringBuilder("SELECT dura.is_active as is_assign,\n" +
                        "       dr.is_active, " +
                        "       dr.d_role_id,\n" +
                        "       dr.d_tenant_id,\n" +
                        "       dura.d_user_id,\n" +
                        "       dr.name,\n" +
                        "       dr.code\n" +
                        "FROM pos.d_role dr\n" +
                        "         LEFT JOIN pos.d_user_role_access dura ON dr.d_role_id = dura.d_role_id and dura.d_user_id = :userId  and dr.d_tenant_id = :tenantId where dr.d_role_id <> 0");
            } else {
                sql = new StringBuilder("SELECT\n" +
                        "    dura.is_active as is_assign,\n" +
                        "    dr.is_active, " +
                        "    dr.d_role_id,\n" +
                        "    dr.d_tenant_id,\n" +
                        "    dura.d_user_id,\n" +
                        "    dr.name,\n" +
                        "    dr.code\n" +
                        "FROM\n" +
                        "   ( select dr2.d_role_id,dr2.d_tenant_id,dr2.code,dr2.name, dr2.is_active from d_role dr2  join d_user_role_access dura2 on dr2.d_role_id = dura2.d_role_id and dura2.d_user_id = :currentUserId and dr2.d_tenant_id = :tenantId )dr\n" +
                        "        LEFT JOIN\n" +
                        "    pos.d_user_role_access dura\n" +
                        "    ON dr.d_role_id = dura.d_role_id\n" +
                        "        AND dura.d_user_id = :userId\n" +
                        "        AND dr.d_tenant_id = :tenantId where dr.d_role_id <> 0");
            }
            query = entityManager.createNativeQuery(sql.toString());
            query.setParameter("userId", item.getUserId());
            query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
            if (!roleCheck.getCode().equals(AppConstant.ROLECODE.ROLE_ADMIN)) {
                query.setParameter("currentUserId", request.getCurrentUserId());
            }
            results = query.unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                UserRoleAccessDto accessDto = UserRoleAccessDto.builder()
                        .roleId(ParseHelper.INT.parse(row.get("d_role_id")))
                        .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                        .roleName(ParseHelper.STRING.parse(row.get("name")))
                        .created(ParseHelper.STRING.parse(row.get("created")))
                        .updated(ParseHelper.STRING.parse(row.get("updated")))
                        .isAssign((row.get("d_user_id") != null) && ParseHelper.STRING.parse(row.get("is_assign")).equals("Y") ? "Y" : "N")
                        .build();
                userRoleAccessDtos.add(accessDto);
            }

            //get warehouse access
//            List<OrgWarehouseAccessDto> rsWarehouse = new ArrayList<>();
//            StringBuilder sqlGetWarehouse = new StringBuilder("select dowa.d_user_id,dowa.d_tenant_id, dog.d_org_id,dog.name as org_name,dw.d_warehouse_id,dw.name as warehouse_name,dowa.is_active  from d_org_warehouse_access dowa join d_org dog on dowa.d_org_id = dog.d_org_id\n" +
//                    "join d_warehouse dw on dowa.d_warehouse_id = dw.d_warehouse_id where dowa.d_user_id = :userId ");
//            List<Tuple> rsGetWarehouse = entityManager.createNativeQuery(sqlGetWarehouse.toString(), Tuple.class)
//                    .setParameter("userId", item.getUserId()).getResultList();
//            for (Tuple rsItem : rsGetWarehouse) {
//                OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
//                        .orgId(ParseHelper.INT.parse(rsItem.get("d_org_id")))
//                        .orgName(ParseHelper.STRING.parse(rsItem.get("org_name")))
//                        .warehouseId(ParseHelper.INT.parse(rsItem.get("d_warehouse_id")))
//                        .warehouseName(ParseHelper.STRING.parse(rsItem.get("warehouse_name")))
//                        .isActive(ParseHelper.STRING.parse(rsItem.get("is_active")))
//                        .userId(ParseHelper.INT.parse(rsItem.get("d_user_id")))
//                        .tenantId(ParseHelper.INT.parse(rsItem.get("d_tenant_id")))
//                        .build();
//                rsWarehouse.add(accessDto);
//            }
            List<OrgWarehouseAccessDto> rsWarehouse = new ArrayList<>();
            StringBuilder sqlGetWarehouse = new StringBuilder("select * from pos.d_user_warehouse_access_v where d_user_id = :userId ");
            List<Tuple> rsGetWarehouse = entityManager.createNativeQuery(sqlGetWarehouse.toString(), Tuple.class)
                    .setParameter("userId", item.getUserId()).getResultList();
            for (Tuple rsItem : rsGetWarehouse) {
                OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                        .orgId(ParseHelper.INT.parse(rsItem.get("d_org_id")))
                        .orgName(ParseHelper.STRING.parse(rsItem.get("org_name")))
                        .warehouseId(ParseHelper.INT.parse(rsItem.get("d_warehouse_id")))
                        .warehouseName(ParseHelper.STRING.parse(rsItem.get("warehouse_name")))
                        .warehouseCode(ParseHelper.STRING.parse(rsItem.get("code")))
                        .isActive(ParseHelper.STRING.parse(rsItem.get("is_active")))
                        .userId(ParseHelper.INT.parse(rsItem.get("d_user_id")))
                        .tenantId(ParseHelper.INT.parse(rsItem.get("d_tenant_id")))
                        .isAssign(ParseHelper.STRING.parse(rsItem.get("is_assign")).equals("Y") ? "Y" : "N")
                        .build();
                rsWarehouse.add(accessDto);
            }


            UserDto dto = modelMapper.map(item, UserDto.class);
            dto.setPassword(null);
            dto.setIServerId(null);
            if (item.getBirthDay() != null) {
                dto.setBirthDay(DateHelper.fromLocalDate(item.getBirthDay()));
            }

            if (item.getGender() != null) {
                dto.setGenderName(getSysNameByValue(item.getGender()));
            }
            dto.setUserOrgAccessDtos(userOrgAccessDtos);
            dto.setUserRoleAccessDtos(userRoleAccessDtos);
            dto.setOrgWarehouseAccessDtos(rsWarehouse);
            listData.add(dto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage(messageSource.getMessage("user_find_all_success", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(users.getTotalPages());
        globalReponsePagination.setPageSize(users.getSize());
        globalReponsePagination.setCurrentPage(users.getNumber());
        globalReponsePagination.setTotalItems(users.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse findPosTerminalIdAccessByUserIdAndOrgId(Integer userId, Integer orgId) {
        log.info("** Find Pos Terminal Id Access By User Id And Org Id Service *");
        GlobalReponse reponse = new GlobalReponse();
        List<PosterminalOrgAccessV> posTerminalOrgAccesses = posterminalOrgAccessVRepository.findAllByUserIdAndOrgId(userId, orgId);
        List<PosterminalOrgAccessVDto> listData = posTerminalOrgAccesses.stream().map((element) -> modelMapper.map(element, PosterminalOrgAccessVDto.class)).collect(Collectors.toList());
        if (listData.isEmpty()) {
            reponse.setMessage(messageSource.getMessage("posterminal_access_notFound", null, LocaleContextHolder.getLocale()));
            return reponse;
        }
        reponse.setData(listData);
        reponse.setMessage(messageSource.getMessage("postermianl_find_success", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    public GlobalReponse intSave(List<UserIntDto> users) {
        HttpHeaders headersCall = new HttpHeaders();
        headersCall.setContentType(MediaType.APPLICATION_JSON);
        headersCall.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersCall);
        log.info("** Save List User Service *");
        users.forEach(itemUser -> {
            String errorMess=  this.integrationService.saveSingleUser(itemUser);
            if(errorMess != null && !errorMess.isEmpty())
            {
                IUser i = modelMapper.map(itemUser, IUser.class);
                i.setErrorMessage(errorMess);
                i.setTenantId(AuditContext.getAuditInfo().getTenantId());
                this.iUserRepository.save(i);
            }
        });
        return GlobalReponse.builder()
                .errors(error.toString())
                .message(messageSource.getMessage("user.synced.successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     *
     * @param users
     * @return
     */
    @Override
    public GlobalReponse intSaveERPNext(List<UserIntDto> users) {
        log.info("** Save User ERPNext *");
        users.forEach(item -> {
            User userCheck = userRepository.findByUserName(item.getErpUserName()).orElse(null);
            if (userCheck == null) {
                userCheck = userMapper.toUserInt(item);
                userCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                this.userRepository.saveAndFlush(userCheck);
            } else {
                userCheck = userMapper.updateEntityInt(item, userCheck);
                this.userRepository.saveAndFlush(userCheck);
            }
        });

        return GlobalReponse.builder()
                .errors("")
                .message("Save list user success")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponsePagination getOrgAccess(Integer userId, Integer roleId, Integer page, Integer pageSize, String name, String searchKey, String area) {
        log.info(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new UserObjectNotFoundException("error"));
        StringBuilder countQuery;

        //
        if (role.getCode().equals("ADM")) {
            sql = new StringBuilder("select dog.d_org_id, dog.code, dog.name, dog.phone, dog.wards, dua.d_user_id, dog.d_tenant_id, dua.is_active \n" +
                    "from d_org dog \n" +
                    "left join d_userorg_access dua on dog.d_org_id = dua.d_org_id and dua.d_user_id = :userId where dog.d_tenant_id = :tenantId  ");
            if (name != null && !name.isEmpty())
                sql.append(" and lower(dog.name) like lower(:name)");
            if (searchKey != null && !searchKey.isEmpty()) // search by code or name or phone
                sql.append(" and (lower(dog.code) like lower(:searchKey) or lower(dog.name) like lower(:searchKey) or lower(dog.phone) like lower(:searchKey))");
            if (area != null && !area.isEmpty())
                sql.append(" and lower(dog.area) like lower(:area)");

            countQuery = new StringBuilder("select count(*) from d_org dog\n" +
                    "left join d_userorg_access dua on dog.d_org_id = dua.d_org_id and dua.d_user_id = :userId where dog.d_tenant_id = :tenantId ");
            if (name != null && !name.isEmpty())
                countQuery.append(" where lower(dog.name) like lower(:name)");
            if (searchKey != null && !searchKey.isEmpty()) // search by code or name or phone
                countQuery.append(" and (lower(dog.code) like lower(:searchKey) or lower(dog.name) like lower(:searchKey) or lower(dog.phone) like lower(:searchKey))");
            if (area != null && !area.isEmpty())
                countQuery.append(" and lower(dog.area) like lower(:area)");
        } else {
            sql = new StringBuilder("select dog.d_org_id, dog.code, dog.name, dog.phone, dog.wards, dua.d_user_id, dog.d_tenant_id, dua.is_active \n" +
                    "from d_org dog\n" +
                    "join d_userorg_access dua on dog.d_org_id = dua.d_org_id and dua.d_user_id = :userId where dog.d_tenant_id = :tenantId ");
            if (name != null && !name.isEmpty())
                sql.append(" and lower(dog.name) like lower(:name)");
            if (searchKey != null && !searchKey.isEmpty()) // search by code or name or phone
                sql.append(" and (lower(dog.code) like lower(:searchKey) or lower(dog.name) like lower(:searchKey) or lower(dog.phone) like lower(:searchKey))");
            if (area != null && !area.isEmpty())
                sql.append(" and lower(dog.area) like lower(:area)");
            countQuery = new StringBuilder("select count(*) from d_org dog\n" +
                    "join d_userorg_access dua on dog.d_org_id = dua.d_org_id and dua.d_user_id = :userId where dog.d_tenant_id = :tenantId ");
            if (name != null && !name.isEmpty())
                countQuery.append(" where lower(dog.name) like lower(:name)");
            if (searchKey != null && !searchKey.isEmpty()) // search by code or name or phone
                countQuery.append(" and (lower(dog.code) like lower(:searchKey) or lower(dog.name) like lower(:searchKey) or lower(dog.phone) like lower(:searchKey))");
            if (area != null && !area.isEmpty())
                countQuery.append(" and lower(dog.area) like lower(:area)");
        }

        Query queryTotalItemsBigInt = entityManager.createNativeQuery(countQuery.toString());
        queryTotalItemsBigInt.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        queryTotalItemsBigInt.setParameter("userId", userId);
        if (name != null && !name.isEmpty())
            queryTotalItemsBigInt.setParameter("name", "%" + name + "%");
        if (searchKey != null && !searchKey.isEmpty())
            queryTotalItemsBigInt.setParameter("searchKey", "%" + searchKey + "%");
        if (area != null && !area.isEmpty())
            queryTotalItemsBigInt.setParameter("area", "%" + area + "%");
        // T
        BigInteger totalItemsBigInt = (BigInteger) queryTotalItemsBigInt.getSingleResult();
        Long totalItems = totalItemsBigInt.longValue();
        //
        int offset = page * pageSize;
        //
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        query.setParameter("userId", userId);
        if (name != null && !name.isEmpty())
            query.setParameter("name", "%" + name + "%");
        if (searchKey != null && !searchKey.isEmpty())
            query.setParameter("searchKey", "%" + searchKey + "%");
        if (area != null && !area.isEmpty())
            query.setParameter("area", "%" + area + "%");

        List<Map<String, Object>> results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();

        //
        List<UserOrgAccessDto> userOrgAccessDtos = new ArrayList<>();
        for (Map<String, Object> row : results) {
            UserOrgAccessDto accessDto = UserOrgAccessDto.builder()
                    .orgWards(ParseHelper.STRING.parse(row.get("wards")))
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .orgCode(ParseHelper.STRING.parse(row.get("code")))
                    .orgName(ParseHelper.STRING.parse(row.get("name")))
                    .orgPhone(ParseHelper.STRING.parse(row.get("phone")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .isAssign((row.get("d_user_id") != null && ParseHelper.STRING.parse(row.get("is_active")).equals("Y")) ? "Y" : "N")
                    .build();
            userOrgAccessDtos.add(accessDto);
        }

        //
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UserOrgAccessDto> userOrgAccessPage = new PageImpl<>(userOrgAccessDtos, pageable, totalItems);

        //
        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .data(userOrgAccessPage.getContent())  // Trả về toàn bộ đối tượng Page thay vì chỉ danh sách
                .currentPage(userOrgAccessPage.getNumber())
                .totalPages(userOrgAccessPage.getTotalPages())
                .pageSize(userOrgAccessPage.getSize())
                .totalItems(userOrgAccessPage.getTotalElements())
                .build();
    }


    /**
     * @param userId
     * @param orgId
     * @return
     */
    @Override
    public GlobalReponse getOrgWarehouseAccess(Integer userId, Integer orgId) {
        log.info("*** UserDto, service; fetch org warehouse access *");
        sql = new StringBuilder("select d_tenant_id, d_org_id, d_warehouse_id, d_user_id, is_active, created, created_by, updated, updated_by, name, description from d_org_warehouse_access_v " +
                " where d_user_id = :userId AND (:orgId IS NULL OR d_org_id = :orgId) and is_active = 'Y' and d_tenant_id = :tenantId");


        List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                .setParameter("userId", userId)
                .setParameter("orgId", orgId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        List<OrgWarehouseAccessDto> resultDto = new ArrayList<>();
        for (Map<String, Object> row : results) {
            OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                    .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .nameWarehouse(ParseHelper.STRING.parse(row.get("name")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .build();
            resultDto.add(accessDto);
        }
        return GlobalReponse.builder()
                .data(resultDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     *
     * @param userId
     * @return
     */
    @Override
    public GlobalReponse getWarehouseAccess(Integer userId) {
        log.info("*** UserDto, service; fetch org warehouse access *");
        sql = new StringBuilder("select d_tenant_id, d_org_id, d_warehouse_id, d_user_id, is_active, created, created_by, updated, updated_by, name, description from d_org_warehouse_access_v " +
                " where d_user_id = :userId  and d_tenant_id = :tenantId");


        List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                .setParameter("userId", userId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        List<OrgWarehouseAccessDto> resultDto = new ArrayList<>();
        for (Map<String, Object> row : results) {
            OrgWarehouseAccessDto accessDto = OrgWarehouseAccessDto.builder()
                    .warehouseId(ParseHelper.INT.parse(row.get("d_warehouse_id")))
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .nameWarehouse(ParseHelper.STRING.parse(row.get("name")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .build();
            resultDto.add(accessDto);
        }
        return GlobalReponse.builder()
                .data(resultDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @param userDto
     * @return
     */
    @Override

    @Transactional
    public GlobalReponse saveAll(UserDto userDto) {
        log.info("*** UserDto, service; save all user *");
        GlobalReponse response = new GlobalReponse();
        StringBuilder sql = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where is_default = 'Y' and d_tenant_id = :tenantId");
        BigDecimal posterminalId = (BigDecimal) entityManager.createNativeQuery(sql.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .getSingleResult();

        User userCheck = null;
        List<UserOrgAccessDto> listUserOrgDto = new ArrayList<>();
        List<UserRoleAccessDto> listUserRoleDto = new ArrayList<>();
        List<OrgWarehouseAccessDto> lisOrgWarehouseAccessDto = new ArrayList<>();
        if (userDto.getUserId() != null) { //update
            userCheck = userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new UserObjectNotFoundException(messageSource.getMessage("user_notFound", null, LocaleContextHolder.getLocale())));
            modelMapper.map(userDto, userCheck);
            if (userDto.getPassword() != null)
                userCheck.setPassword(passwordEncoder.encode(userDto.getPassword()));
            if (userDto.getBirthDay() != null)
                userCheck.setBirthDay(DateHelper.toLocalDate(userDto.getBirthDay()));
            userCheck = userRepository.save(userCheck);
            Integer userId = userCheck.getUserId();
            if (userDto.getUserRoleAccessDtos() != null) {
                /*
                update is active = N
                xu ly role access
                 */
                userRoleAccessRepository.updateAllByUserId(userCheck.getUserId(), "N");
                List<UserRoleAccess> entitiesToSave = new ArrayList<>();
                userDto.getUserRoleAccessDtos().forEach(item -> {
                    UserRoleAccess userRoleAccess = userRoleAccessRepository.findByUserIdAndRoleId(userId, item.getRoleId());
                    if (userRoleAccess == null) {
                        userRoleAccess = UserRoleAccess.builder()
                                .userId(userId)
                                .roleId(item.getRoleId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        userRoleAccess.setIsActive("Y");
                    } else {
                        userRoleAccess.setIsActive("Y");
                    }
                    entitiesToSave.add(userRoleAccess);
                    listUserRoleDto.add(modelMapper.map(userRoleAccess, UserRoleAccessDto.class));
                });

                userRoleAccessRepository.saveAll(entitiesToSave); // Lưu hàng loạt vào DB
            }
               /*
                update is active = N
                xu ly org access
                 */
            if (userDto.getUserOrgAccessDtos() != null) {
                userOrgAccessRepository.updateAllByUserIdAndTenantId(userCheck.getUserId(), "N", AuditContext.getAuditInfo().getTenantId());
                userDto.getUserOrgAccessDtos().forEach(item -> {
                    UserOrgAccess userOrgAccess = userOrgAccessRepository.findByUserIdAndOrgId(userId, item.getOrgId());
                    if (userOrgAccess == null) {
                        userOrgAccess = UserOrgAccess.builder()
                                .userId(userId)
                                .orgId(item.getOrgId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        userOrgAccessRepository.save(userOrgAccess);

                        PosTerminalOrgAccess posTerminalOrgAccess = PosTerminalOrgAccess.builder()
                                .userId(userId)
                                .orgId(item.getOrgId())
                                .posTerminalId(posterminalId.intValue())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        posTerminalOrgAccessRepository.save(posTerminalOrgAccess);
                    } else {
                        userOrgAccess.setIsActive("Y");
                        userOrgAccessRepository.save(userOrgAccess);
                    }
                    listUserOrgDto.add(modelMapper.map(userOrgAccess, UserOrgAccessDto.class));
                });
            }

            // xu ly warehouse org access
            if (userDto.getOrgWarehouseAccessDtos() != null) {
//                orgWarehouseAccessRepository.updateIsActive(userCheck.getUserId(), "N", AuditContext.getAuditInfo().getTenantId());
                userDto.getOrgWarehouseAccessDtos().forEach(item -> {
                    OrgWarehouseAccess warehouseCheck = orgWarehouseAccessRepository.findByUserIdAndWarehouseIdAndOrgIdAndTenantId(userId, item.getWarehouseId(), item.getOrgId(), AuditContext.getAuditInfo().getTenantId());
                    if (warehouseCheck == null) {
                        OrgWarehouseAccess orgWarehouseAccess = OrgWarehouseAccess.builder()
                                .userId(userId)
                                .orgId(item.getOrgId())
                                .warehouseId(item.getWarehouseId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .isActive(item.getIsActive())
                                .build();
                        orgWarehouseAccessRepository.save(orgWarehouseAccess);
                    } else {
                        modelMapper.map(item, warehouseCheck);
                        orgWarehouseAccessRepository.save(warehouseCheck);
                    }
                });
            }
            userDto = modelMapper.map(userCheck, UserDto.class);

            response.setMessage(messageSource.getMessage("user_update", null, LocaleContextHolder.getLocale()));
        } else {
            //insert
            if (userRepository.findByUserName(userDto.getUserName()).isPresent())
                throw new UserServiceException(messageSource.getMessage("user_name_exist", null, LocaleContextHolder.getLocale()));
            if (!userRepository.findAllByPhone(userDto.getPhone()).isEmpty())
                throw new UserServiceException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));

            userCheck = modelMapper.map(userDto, User.class);
            userCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
            userCheck.setPassword(passwordEncoder.encode(userDto.getPassword()));
            if (userDto.getBirthDay() != null)
                userCheck.setBirthDay(DateHelper.toLocalDate(userDto.getBirthDay()));
            userCheck = userRepository.save(userCheck);
            Integer userId = userCheck.getUserId();
            //xu ly user role
            if (userDto.getUserRoleAccessDtos() != null) {
                userDto.getUserRoleAccessDtos().forEach(item -> {
                    UserRoleAccess userRoleAccess = UserRoleAccess.builder()
                            .userId(userId)
                            .roleId(item.getRoleId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    userRoleAccessRepository.save(userRoleAccess);
                    listUserRoleDto.add(modelMapper.map(userRoleAccess, UserRoleAccessDto.class));
                });
            }

            //xu ly user org access
            if (userDto.getUserOrgAccessDtos() != null) {
                userDto.getUserOrgAccessDtos().forEach(item -> {
                    UserOrgAccess userOrgAccess = UserOrgAccess.builder()
                            .userId(userId)
                            .orgId(item.getOrgId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    userOrgAccessRepository.save(userOrgAccess);
                    listUserOrgDto.add(modelMapper.map(userOrgAccess, UserOrgAccessDto.class));

                    log.info("posterminalId: " + posterminalId);
                    PosTerminalOrgAccess posTerminalOrgAccess = PosTerminalOrgAccess.builder()
                            .userId(userId)
                            .orgId(item.getOrgId())
                            .posTerminalId(posterminalId.intValue())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    posTerminalOrgAccessRepository.save(posTerminalOrgAccess);
                });
            }
            // xu ly warehouse org access
            if (userDto.getOrgWarehouseAccessDtos() != null) {
                userDto.getOrgWarehouseAccessDtos().forEach(item -> {
                    OrgWarehouseAccess orgWarehouseAccess = OrgWarehouseAccess.builder()
                            .userId(userId)
                            .orgId(item.getOrgId())
                            .warehouseId(item.getWarehouseId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    orgWarehouseAccess.setIsActive("Y");
                    orgWarehouseAccessRepository.save(orgWarehouseAccess);
                });
            }
            userDto = modelMapper.map(userCheck, UserDto.class);
            response.setMessage(messageSource.getMessage("user_create", null, LocaleContextHolder.getLocale()));
        }
        userDto.setPassword(null);
        response.setData(userDto);
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    /**
     * @param
     * @return
     */
    @Override
    public GlobalReponse getByIdAndRoleId(Integer currentUserId, Integer userId, Integer roleId) {

        User userGet = userRepository.findById(userId).orElseThrow(() -> new UserObjectNotFoundException(messageSource.getMessage("user_notFound", null, LocaleContextHolder.getLocale())));
        UserDto userDto = modelMapper.map(userGet, UserDto.class);
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new UserObjectNotFoundException(messageSource.getMessage("role_notFound", null, LocaleContextHolder.getLocale())));
        if (role.getCode().equals("ADM")) {
            sql = new StringBuilder("select dog.d_org_id, dog.code, dog.name, dog.phone, dog.wards, dua.d_user_id, dog.d_tenant_id, dua.is_active \n" +
                    "from d_org dog\n" +
                    "         left join d_userorg_access dua on dog.d_org_id = dua.d_org_id and dua.d_user_id = :userId and dog.d_tenant_id = :tenantId");

        } else {
            sql = new StringBuilder("SELECT dog.d_org_id, dog.code, dog.name, dog.phone, dog.wards, dua.d_user_id, dog.d_tenant_id, dua.is_active\n" +
                    "FROM (select drg.d_org_id,drg.name, drg.code, drg.phone, drg.wards, drg.d_tenant_id from d_org drg\n" +
                    "      join d_userorg_access dua1 on drg.d_org_id = dua1.d_org_id and dua1.d_user_id = :currentUserId and drg.d_tenant_id = :tenantId) dog\n" +
                    "LEFT JOIN d_userorg_access dua on dog.d_org_id = dua.d_org_id\n" +
                    "     AND dua.d_user_id = :userId\n" +
                    "     AND dog.d_tenant_id = :tenantId");

        }
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        if (!role.getCode().equals("ADM")) {
            query.setParameter("currentUserId", currentUserId);
        }
        List<Map<String, Object>> results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        List<UserOrgAccessDto> userOrgAccessDtos = new ArrayList<>();
        for (Map<String, Object> row : results) {

            UserOrgAccessDto accessDto = UserOrgAccessDto.builder()
                    .orgWards(ParseHelper.STRING.parse(row.get("wards")))
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .orgCode(ParseHelper.STRING.parse(row.get("code")))
                    .orgName(ParseHelper.STRING.parse(row.get("name")))
                    .orgPhone(ParseHelper.STRING.parse(row.get("phone")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active"))) //
                    .isAssign((row.get("d_user_id") != null && ParseHelper.STRING.parse(row.get("is_active")).equals("Y")) ? "Y" : "N")
                    .build();
            userOrgAccessDtos.add(accessDto);
        }

        List<UserRoleAccessDto> userRoleAccessDtos = new ArrayList<>();
        if (role.getCode().equals("ADM")) {
            sql = new StringBuilder("SELECT dura.is_active,\n" +
                    "       dr.d_role_id,\n" +
                    "       dr.d_tenant_id,\n" +
                    "       dura.d_user_id,\n" +
                    "       dr.name,\n" +
                    "       dr.code\n" +
                    "FROM pos.d_role dr\n" +
                    "         LEFT JOIN pos.d_user_role_access dura ON dr.d_role_id = dura.d_role_id and dura.d_user_id = :userId  and dr.d_tenant_id = :tenantId");
        } else {
            sql = new StringBuilder("SELECT\n" +
                    "    dura.is_active,\n" +
                    "    dr.d_role_id,\n" +
                    "    dr.d_tenant_id,\n" +
                    "    dura.d_user_id,\n" +
                    "    dr.name,\n" +
                    "    dr.code\n" +
                    "FROM\n" +
                    "   ( select dr2.d_role_id,dr2.d_tenant_id,dr2.code from d_role dr2  join d_user_role_access dura2 on dr2.d_role_id = dura2.d_role_id and dura2.d_user_id = :currentUserId and dr2.d_tenant_id = :tenantId )dr\n" +
                    "        LEFT JOIN\n" +
                    "    pos.d_user_role_access dura\n" +
                    "    ON dr.d_role_id = dura.d_role_id\n" +
                    "        AND dura.d_user_id = :userId\n" +
                    "        AND dr.d_tenant_id = :tenantId");
        }
        query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        if (!role.getCode().equals("ADM")) {
            query.setParameter("currentUserId", currentUserId);
        }
        results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        for (Map<String, Object> row : results) {
            UserRoleAccessDto accessDto = UserRoleAccessDto.builder()
                    .roleId(ParseHelper.INT.parse(row.get("d_role_id")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .roleName(ParseHelper.STRING.parse(row.get("name")))
                    .created(ParseHelper.STRING.parse(row.get("created")))
                    .updated(ParseHelper.STRING.parse(row.get("updated")))
                    .isAssign((row.get("d_user_id") != null) && ParseHelper.STRING.parse(row.get("is_active")).equals("Y") ? "Y" : "N")
                    .build();
            userRoleAccessDtos.add(accessDto);
        }
        userDto.setUserRoleAccessDtos(userRoleAccessDtos);
        userDto.setUserOrgAccessDtos(userOrgAccessDtos);
        userDto.setPassword(null);


        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .data(userDto)
                .build();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public GlobalReponse getOrgAccess(Integer userId) {
        log.info(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        List<UserOrgAccess> userOrgAccesses = userOrgAccessRepository.findAllByUserIdAndIsActive(userId, "Y");

        Integer[] id = userOrgAccesses.stream().map(UserOrgAccess::getOrgId).toArray(Integer[]::new);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .data(id)
                .build();
    }

    /**
     * @param userDto
     * @return
     */
    @Override

    @Transactional
    public GlobalReponse registerNoToken(UserDto userDto) {
        log.info("*** UserDto, service; register user without token *");
        GlobalReponse response = new GlobalReponse();
        User userRegister = userMapper.toUser(userDto);
        userRegister.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userRegister);
        return GlobalReponse.builder()
                .data(userDto)
                .message("success")
                .status(HttpStatus.OK.value()).build();
    }

    /**
     * @param erpUserId
     * @return
     */
    @Override
    public GlobalReponse getByErpUserId(Integer erpUserId) {
        log.info("*** UserDto, service; get user by erpUserId *");
        User user = userRepository.findByErpUserId(erpUserId);
        if (user == null)
            throw new UserObjectNotFoundException(messageSource.getMessage("user_notFound", null, LocaleContextHolder.getLocale()));
        return GlobalReponse.builder()
                .data(modelMapper.map(user, UserDto.class))
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

//    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_SEND_CUS, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, UserIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("listener received message");
////        log.info("Received message: " + consumerRecord.value());
//        try {
//            String key = consumerRecord.key(); // could be null
//            UserIntKafkaDto value = consumerRecord.value();
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
//            if (value.getTenantId() != 0) {
//                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//            } else {
//                dataSourceContextHolder.setCurrentTenantId(null);
//
//            }
//            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                    "0", 0, "vi", value.getTenantId()));
//
//
//            log.info("Received message:");
//            log.info("Key: " + key);
//            log.info("laspage: " + value.getLastPage());
    ////                log.info("Value: " + value);
//            UserIntKafkaDto result = this.intSaveKafka(value.getUserIntDto());
//            result.setLastPage(value.getLastPage());
//            kafkaTemplate.send(TOPIC_RECEIVE_CUS, result);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        acknowledgment.acknowledge();
//    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }


    @Transactional
    public UserIntKafkaDto intSaveKafka(List<UserIntDto> users) {
        log.info("start kafka save user - size: " + users.size());
        UserIntKafkaDto result = new UserIntKafkaDto();
        result.setError("");
        result.setStatus("COM");
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());

        try {
            HttpHeaders headersCall = new HttpHeaders();
            headersCall.setContentType(MediaType.APPLICATION_JSON);
            headersCall.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

            HttpEntity<String> entityHeader = new HttpEntity<>(headersCall);


            StringBuilder error = new StringBuilder();
            log.info("** Save List User Service *");
            users.forEach(itemUser -> {
                itemUser.setPassword(passwordEncoder.encode(itemUser.getPassword()));
                User userCheck = userRepository.findByErpUserId(itemUser.getErpUserId());
                try {
                    if (userCheck == null) {
                        userCheck = userMapper.toUserInt(itemUser);
                        userCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                    if (!userRepository.findAllByPhone(itemUser.getPhone()).isEmpty())
//                        throw new PosException(messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
//                    if (userRepository.findByEmail(itemUser.getEmail()) != null)
//                        throw new PosException(messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));
                        userCheck = userRepository.save(userCheck);
                    } else {
                        userCheck = userMapper.updateEntityInt(itemUser, userCheck);
                        userRepository.save(userCheck);
                    }
                } catch (Exception e) {
                    error.append(e.getMessage());
                    return;
                }
                UserDto userSave = userMapper.toUserDto(userCheck);
                Integer userId = userSave.getUserId();
                // luu org access cua user
                itemUser.getUserOrgAccessDtos().forEach(itemOrgAccess -> {

                    StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                    List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                            .setParameter("orgId", itemOrgAccess.getOrgId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    if (resultList.isEmpty())
                        throw new PosException(messageSource.getMessage("org.not.synced", null, LocaleContextHolder.getLocale()));
                    Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));


                    UserOrgAccess userOrgAccess = userOrgAccessRepository.findByUserIdAndOrgId(userId, orgDtoId);
//                this.userOrgAccessRepository.updateAllByUserIdAndTenantId(userId, "N", AuditContext.getAuditInfo().getTenantId());
                    if (userOrgAccess == null) {
                        UserOrgAccess userOrgAccessSave = UserOrgAccess.builder()
                                .userId(userId)
                                .orgId(orgDtoId)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        userOrgAccessRepository.save(userOrgAccessSave);
                    } else {
                        userOrgAccess.setUserId(userId);
                        userOrgAccess.setOrgId(orgDtoId);
                        userOrgAccess.setIsActive(itemOrgAccess.getIsActive());
                        userOrgAccessRepository.save(userOrgAccess);
                    }

                    // luu warehouse
                    itemOrgAccess.getOrgWarehouseAccessIntDtos().forEach(itemWarehouse -> {
                        GlobalReponse resWarehouse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_GET_WAREHOUSE_BY_ERP_ID + "/" + itemWarehouse.getWarehouseId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                        if (resWarehouse.getStatus() != HttpStatus.OK.value())
                            throw new PosException(messageSource.getMessage("warehouse.not.synced", null, LocaleContextHolder.getLocale()));
                        WarehouseDto wh = modelMapper.map(resWarehouse.getData(), WarehouseDto.class);
//                    this.orgWarehouseAccessRepository.updateIsActive(userId, wh.getId(), "N", orgDto.getId(), AuditContext.getAuditInfo().getTenantId());
//                    this.orgWarehouseAccessRepository.updateIsActive(userId,"N",AuditContext.getAuditInfo().getTenantId());
                        OrgWarehouseAccess orgWarehouseAccess = orgWarehouseAccessRepository.findByUserIdAndWarehouseIdAndOrgIdAndTenantId(userId, wh.getId(), orgDtoId, AuditContext.getAuditInfo().getTenantId());
                        if (orgWarehouseAccess == null) {
                            OrgWarehouseAccess orgWarehouseAccessSave = OrgWarehouseAccess.builder()
                                    .userId(userId)
                                    .orgId(orgDtoId)
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
                        headers.set("Accept-Language", "en");
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<PosTerminalDto> requestEntity = new HttpEntity<>(itemPosTerminal.getInfor(), headers);

                        GlobalReponse exResponsePos = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.CHECK_POSTERMINALBYERP_ID, requestEntity, GlobalReponse.class);
                        if (exResponsePos.getData() == null)
                            throw new PosException(messageSource.getMessage("org.not.synced", null, LocaleContextHolder.getLocale()));

                        PosTerminalDto posTerminalDto = modelMapper.map(exResponsePos.getData(), PosTerminalDto.class);
                        PosTerminalOrgAccess posterminalOrgAccess = posTerminalOrgAccessRepository.findByUserIdAndOrgIdAndPosTerminalId(userId, orgDtoId, posTerminalDto.getId());
                        if (posterminalOrgAccess == null) {
                            PosTerminalOrgAccess posterminalOrgAccessSave = PosTerminalOrgAccess.builder()
                                    .userId(userId)
                                    .orgId(orgDtoId)
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


            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(e.getMessage());
            result.setStatus("FAI");
        }
        return result;
    }

    public boolean isFirstLogin() {
        try {
            String sql = "SELECT 1 " +
                    " FROM pos.d_tenant WHERE d_tenant_id = :tenantId " +
                    " AND is_first_login = 'Y' ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
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

    public String getSysNameByValue(String value) {

        String sql = "SELECT name  " +
                " FROM pos.d_reference_list WHERE d_tenant_id = :tenantId " +
                " AND value = :refName ";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("refName", value)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PaymentInfoDto> listPayment = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            return ParseHelper.STRING.parse(row.get("name"));
        }

        return null;
    }
}










