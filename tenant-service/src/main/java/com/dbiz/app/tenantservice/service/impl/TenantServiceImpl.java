package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.*;
import com.dbiz.app.tenantservice.enums.DatabaseCreationStatus;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RandomHelper;
import com.dbiz.app.tenantservice.helper.TenantMappingHelper;
import com.dbiz.app.tenantservice.helper.TenantUtils;
import com.dbiz.app.tenantservice.repository.BrandPartnerRepository;
import com.dbiz.app.tenantservice.repository.ConfigForTenantRepository;
import com.dbiz.app.tenantservice.repository.IndustryRepository;
import com.dbiz.app.tenantservice.repository.TenantRepository;

import com.dbiz.app.tenantservice.repository.db.dao.TenantDao;
import com.dbiz.app.tenantservice.service.CommonService;
import com.dbiz.app.tenantservice.service.TenantService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.dbiz.app.tenantservice.service.db.LiquibaseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.common.dbiz.dto.IdentityDto.Tenant.IDCreateTenantDto;
import org.common.dbiz.dto.IdentityDto.Tenant.IDTenantLoginInfoDto;
import org.common.dbiz.dto.IdentityDto.User.IDOwnerDto;
import org.common.dbiz.dto.IdentityDto.User.IDPhoneListDto;
import org.common.dbiz.dto.IdentityDto.User.IDUserDto;
import org.common.dbiz.dto.MDMDto.BPartner.ListMDMCreateBPartnerDto;
import org.common.dbiz.dto.MDMDto.BPartner.MDMCreateBPartnerDto;
import org.common.dbiz.dto.MDMDto.Contract.ListMDMCreateContractDto;
import org.common.dbiz.dto.MDMDto.Contract.MDMCreateContractDto;
import org.common.dbiz.dto.tenantDto.*;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.UserObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.TenantQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@DependsOn("dataSourceRouting")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final ModelMapper modelMapper;
    private final DataSourceConfigService datasourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final TenantDao tenantDao;
    private final LiquibaseService liquibaseService;
    private final RedisTemplate redisTemplate;
    private final TenantUtils tenantUtils;
    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final IndustryRepository industryRepository;
    private final CommonService commonService;
    private final ConfigForTenantRepository configForTenantRepository;
    private final BrandPartnerRepository brandPartnerRepository;
    private final RandomHelper randomHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;


    @Override
    public Page<TenantDto> findAll(Pageable pageable) {
        return (Page<TenantDto>) this.tenantRepository.findAll(pageable).stream().map(TenantMappingHelper::map)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public GlobalReponse findByDomainURL(String domainUrl) {


        try {
            String sql = "SELECT d_tenant_id ,  " +
                    "  tenant_code , " +
                    "  tenant_name , " +
                    "  domain_url , " +
                    "  tax_code , " +
                    "  d_industry_id , " +
                    "  industry_code , " +
                    "  industry_name , " +
                    "  d_user_id , " +
                    "  user_name , " +
                    "  password  " +
                    " FROM pos.d_get_tenant_info_by_url(:url) ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("url", domainUrl)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            TenantByUrlDto tenantDto = new TenantByUrlDto();
            if (results.size() == 0) {
                throw new PosException(messageSource.getMessage("tenant.not.found", null, AppConstant.LOCALE_VI));
            }

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                tenantDto = TenantByUrlDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_tenant_id")))
                        .code(ParseHelper.STRING.parse(row.get("tenant_code")))
                        .name(ParseHelper.STRING.parse(row.get("tenant_name")))
                        .domainUrl(ParseHelper.STRING.parse(row.get("domain_url")))
                        .taxCode(ParseHelper.STRING.parse(row.get("tax_code")))
                        .industryCode(ParseHelper.STRING.parse(row.get("industry_code")))
                        .industry(IndustryDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_industry_id")))
                                .code(ParseHelper.STRING.parse(row.get("industry_code")))
                                .name(ParseHelper.STRING.parse(row.get("industry_name")))
                                .build())
                        .user(UserDto.builder()
                                .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                                .userName(ParseHelper.STRING.parse(row.get("user_name")))
                                .password(ParseHelper.STRING.parse(row.get("password")))
                                .build())
                        .build();
            }
            GlobalReponse globalReponse = new GlobalReponse();
            globalReponse.setData(tenantDto);
            globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            globalReponse.setErrors("");
            globalReponse.setStatus(HttpStatus.OK.value());
            return globalReponse;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
//            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
            throw new PosException(e.getMessage());
        }
    }

    public boolean IDCreateTenant(TenantDto tenantDto, String tenantCode,
                                  String ownerUserName, String ownerPassword) {


        log.info("CREATE TENANT");
        String domainUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.ID_DOMAIN_URL,0).get().getValue();
        String createTenantUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.ID_CREATE_TENANT_URL,0).get().getValue();
        String userNameAdmin = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.ID_USER_ADMIN,0).get().getValue();
        String passwordAdmin = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.ID_PASSWORD_ADMIN,0).get().getValue();


        //USER INFO
        String password = randomHelper.generateRandomString();
        String email = tenantDto.getUser().getEmail() != null ? tenantDto.getUser().getEmail()
                : "danls@ssg.vn";
        String username = tenantDto.getUser().getUserName() != null ? tenantDto.getUser().getUserName()
                : tenantDto.getUser().getPhone();

        tenantDto.getUser().setPassword(password);
        tenantDto.getUser().setUserName(username);


        List<IDOwnerDto> owners = List.of(IDOwnerDto.builder()
                .username(ownerUserName)
                .password(ownerPassword)
                .email(email)
                .firstname("dbiz")
                .lastname("dbiz")
                .provisioningMethod("inline-password")
                .build());

        //IDENTITY TENANT INFO
        IDCreateTenantDto createTenantDto = IDCreateTenantDto.builder()
                .domain(tenantCode)
                .owners(owners)
                .build();

        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(userNameAdmin, passwordAdmin);
        ResponseEntity<String> rsp = null;

        try {
            log.info("payload: " + objectMapper.writeValueAsString(createTenantDto));
            HttpEntity<IDCreateTenantDto> requestEntity = new HttpEntity<>(createTenantDto, headers);
            String url = domainUrl + createTenantUrl;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.CREATED.value()) {

                log.info("response from Tenant: " + objectMapper.writeValueAsString(rsp.getBody()));
                return IDCreateUser(tenantDto, domainUrl, email, tenantCode, ownerUserName, ownerPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public boolean IDCreateUser(TenantDto tenantDto, String domainUrl, String email,
                                String tenantCode, String userNameAdmin, String passwordAdmin) {


        log.info("CREATE USER");
        String createUserUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.ID_CREATE_USER_URL,0).get().getValue();
        String hasToCreateMDM = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.D_HAS_TO_CREATE_MDM,0).get().getValue();



        createUserUrl = createUserUrl.replace("@TENANT_CODE@", tenantCode);

        //IDENTITY USER INFO
        IDUserDto userDto = IDUserDto.builder()
                .emails(List.of(email))
                .password(tenantDto.getUser().getPassword())
                .schemas(new ArrayList<>())
                .userName(tenantDto.getUser().getUserName())
                .phoneNumbers(List.of(IDPhoneListDto.builder()
                        .type("mobile")
                        .value(tenantDto.getUser().getPhone())
                        .build()))
                .full_name(tenantDto.getUser().getFullName())
                .scimUserExtension(IDUserDto.SCIMUserExtension.builder()
                        .googleid(tenantDto.getGoogleId() != null ? tenantDto.getGoogleId() : "")
                        .zaloid(tenantDto.getZaloId() != null ? tenantDto.getZaloId() : "")
                        .facebookid(tenantDto.getFacebookId() != null ? tenantDto.getFacebookId() : "")
                        .tiktokid(tenantDto.getTiktokId() != null ? tenantDto.getTiktokId() : "")
                        .build())
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/scim+json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
        userNameAdmin = userNameAdmin + "@" + tenantCode;
        headers.setBasicAuth(userNameAdmin, passwordAdmin);
        ResponseEntity<String> rsp = null;

        try {
            log.info("payload: " + objectMapper.writeValueAsString(userDto));
            HttpEntity<IDUserDto> requestEntity = new HttpEntity<>(userDto, headers);
            String url = domainUrl + createUserUrl;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.CREATED.value()) {
                JsonNode jsonNode = objectMapper.readTree(rsp.getBody());
                String iServerId = jsonNode.path("id").asText();
                tenantDto.setIServerId(iServerId);
                log.info("response from User: " + objectMapper.writeValueAsString(rsp.getBody()));

                if ("Y".equals(hasToCreateMDM)) {

                    // Nếu có yêu cầu tạo MDM, gọi hàm IDCreateBPartner
                    return IDCreateBPartner(tenantDto,email);
                } else {

                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean IDCreateBPartner(TenantDto tenantDto,String email){

        log.info("CREATE BPARTNER");

        String domainMDMUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.MDM_DOMAIN_ASSET_URL,0).get().getValue();
        String createBPartnerUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.MDM_CREATE_BPARTNER_URL,0).get().getValue();



        MDMCreateBPartnerDto bpartnerDto = MDMCreateBPartnerDto.builder()
                .searchKey(null)
                .bPartnerName(tenantDto.getName())
                .address(tenantDto.getAddress())
                .state(tenantDto.getCity())
                .country(tenantDto.getCountry())
                .taxCode(tenantDto.getTaxCode())
                .phone(tenantDto.getUser().getPhone())
                .email(email)
                .representative(tenantDto.getUser().getFullName())
                .typeOfBusiness(tenantDto.getTypeOfBusiness() != null ? tenantDto.getTypeOfBusiness() : new ArrayList<>())
                .industrySector(tenantDto.getIndustrySector() != null ? tenantDto.getIndustrySector() : new ArrayList<>())
                .numberOfTables(tenantDto.getNumberOfTables())
                .numberOfEmployees(tenantDto.getNumberOfEmployees())
                .registerDate(DateHelper.fromInstantddMMyyyyHHmmss(Instant.now()))
                .active("yes")
                .logo(null)
                .salesRepresentative(tenantDto.getSalesRepresentative())
                .googleId(tenantDto.getGoogleId())
                .facebookId(tenantDto.getFacebookId())
                .zaloId(tenantDto.getZaloId())
                .lineId(tenantDto.getLineId())
                .tiktokId(tenantDto.getTiktokId())
                .build();
        ListMDMCreateBPartnerDto listMDMCreateBPartnerDto = ListMDMCreateBPartnerDto.builder()
                .data(List.of(bpartnerDto))
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
//        headers.setBasicAuth(userNameAdmin,passwordAdmin);
        ResponseEntity<String> rsp = null;

        try{
            log.info("payload: " + objectMapper.writeValueAsString(listMDMCreateBPartnerDto));
            HttpEntity<ListMDMCreateBPartnerDto> requestEntity = new HttpEntity<>(listMDMCreateBPartnerDto, headers);
            String url = domainMDMUrl + createBPartnerUrl;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                String responseBody = rsp.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                log.info("response from BPartner: " + objectMapper.writeValueAsString(rsp.getBody()));
                if(jsonNode.path("resultCode").asText().equals("00")){
                    String searchKey = null;
                    for (JsonNode detail : jsonNode.path("data")) {
                        searchKey = detail.path("baseInfo").path("searchKey").asText();
                    }
                    return  IDCreateContract(tenantDto,email,searchKey);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean IDCreateContract(TenantDto tenantDto,String email,String partnerSearchKey){

        log.info("CREATE CONTRACT");
        String domainMDMUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.MDM_DOMAIN_ASSET_URL,0).get().getValue();
        String createContractUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.MDM_CREATE_CONTRACT_URL,0).get().getValue();

        MDMCreateContractDto contractDto = MDMCreateContractDto.builder()
                .searchKey(null)
                .contractName(tenantDto.getName())
                .businessOfLine(tenantDto.getIndustryCode())
                .bPartnerSearchKey(partnerSearchKey)
                .startDate(DateHelper.fromLocalDateddMMyyyy(LocalDate.now()))
                .productPackage(tenantDto.getProductPackage())
                .contractTermPackage(tenantDto.getContractTermPackage())
                .domain(tenantDto.getDomainUrl())
                .email(email)
                .status(tenantDto.getStatus())
                .active("yes")
                .attachment(null)
                .build();
        ListMDMCreateContractDto listMDMCreateContractDto = ListMDMCreateContractDto.builder()
                .data(List.of(contractDto))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
//        headers.setBasicAuth(userNameAdmin,passwordAdmin);
        ResponseEntity<String> rsp = null;

        try{
            log.info("payload: " + objectMapper.writeValueAsString(listMDMCreateContractDto));
            HttpEntity<ListMDMCreateContractDto> requestEntity = new HttpEntity<>(listMDMCreateContractDto, headers);
            String url = domainMDMUrl + createContractUrl;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                log.info("response from Contract: " + objectMapper.writeValueAsString(rsp.getBody()));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public TenantDto updateIndustry(TenantDto dTenantDto) {
        Optional<Tenant> dTenant = this.tenantRepository.findById(dTenantDto.getId());

        dTenant.get().setIndustryId(dTenantDto.getIndustryId());
        tenantRepository.save(dTenant.get());

        return TenantMappingHelper.map(dTenant.get());
    }

    @Override
    public Tenant save(Tenant dTenant) {
        return this.tenantRepository.save(dTenant);
    }

    @Override
    public GlobalReponse findById(Integer tenantId) {
        return null;
    }

    @Override
    public GlobalReponsePagination findAll(TenantQueryRequest request) {
        return null;
    }

    @Override
    public TenantDto getById(Integer dTenantDto) {
        TenantDto dTenant = this.tenantRepository.findById(dTenantDto).map(TenantMappingHelper::map)
                .orElseThrow(() -> new UserObjectNotFoundException(String.format("Tenant with id: %d not found", dTenantDto)));

        if (dTenant.getAgentCode() != null) {

            dataSourceContextHolder.setCurrentTenantId(null);
            BrandPartner brandPartner = brandPartnerRepository.findByCodeAndIsActive(dTenant.getAgentCode(), "Y").orElse(null);
            dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

            if (brandPartner != null) {

                dTenant.setLogoUrl(brandPartner.getLogoUrl());
            }
        }
        return dTenant;
    }

    @Override
    public TenantAndOrgDto getByTenantAndOrgCode(Integer tenantId) {
        String industryCode = null;
        Tenant tenant = this.tenantRepository.findById(tenantId).get();
        if(tenant.getIndustryId() == null)
        {
            industryCode = tenant.getIndustryCode();
        }else{
            Optional<Industry> industry = industryRepository.findById(tenant.getIndustryId());
            if(industry.isPresent())
            {
                industryCode = industry.get().getCode();
            }
        }

        TenantAndOrgDto dTenant = new TenantAndOrgDto(tenant.getCode(), industryCode);

        return dTenant;
    }


    @Override
    public GlobalReponse createTenant(TenantDto tenantDto) {

        //Generate DB Info (DBNAME, USERNAME, PASSWORD)
//        String dbName1 = tenantUtils.generateHashedDatabaseName(tenantDto.getName());
//        String dbUserName1 = tenantUtils.generateUsername(tenantDto.getName());
//        String dbPassword1 = tenantUtils.generatePasswordUsingUUID();
//        tenantDao.createTenantDb(dbName1, dbUserName1, dbPassword1,tenantDto.getIndustryCode(),tenantDto.getCode());
//
//        if(dbName1 != null) return GlobalReponse.builder().status(HttpStatus.CREATED.value()).message("Successfully").build();

        String industryCode = tenantDto.getIndustryCode();
        if (industryCode != null) tenantDto.setIndustryCode(industryCode.toLowerCase(Locale.ROOT));

        String posWebDomainUrl = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.D_POS_WEB_DOMAIN_URL,0).get().getValue();
        posWebDomainUrl = posWebDomainUrl.replace("@TENANT_CODE@", tenantDto.getIndustryCode());
        String expiryString = configForTenantRepository.findByNameAndTenantId(AppConstant.SYS_VALUE.D_EXPIRY_TIME_TENANT,0).get().getValue();

        if (tenantDto.getAgentCode() != null && !tenantDto.getAgentCode().isEmpty()) {

            brandPartnerRepository.findByCodeAndIsActive(tenantDto.getAgentCode(), "Y").orElseThrow(
                    () -> new PosException(messageSource.getMessage("brand_partner.not.found", null, LocaleContextHolder.getLocale())));
        }

        tenantDto.setDomainUrl(posWebDomainUrl + tenantDto.getCode());

        Tenant tenant = modelMapper.map(tenantDto, Tenant.class);
        UserDto userDto = tenantDto.getUser();

        this.tenantRepository.findByName(tenantDto.getName()).ifPresent(t -> {
            throw new PosException(messageSource.getMessage("tenant.already.exist", null, AppConstant.LOCALE_VI));
        });

        String tenantCode = tenantDto.getIndustryCode() + "." + tenantDto.getCode();
        String ownerUserName = tenantCode + "_admin";
        String ownerPassword = randomHelper.generateRandomString();
        boolean checkIdentityServer = IDCreateTenant(tenantDto, tenantCode,
                ownerUserName, ownerPassword);
        if (!checkIdentityServer) {
            throw new PosException(messageSource.getMessage("create.tenant.failed", null, AppConstant.LOCALE_VI));
        }

        //Generate DB Info (DBNAME, USERNAME, PASSWORD)
        String dbName = tenantUtils.generateHashedDatabaseName(tenantDto.getName());
        String dbUserName = tenantUtils.generateUsername(tenantDto.getName());
        String dbPassword = tenantUtils.generatePasswordUsingUUID();

        tenantDto.setExpiredDate(computeExpiryDate(expiryString));

        tenant.setExpiredDate(tenantDto.getExpiredDate());
        tenant.setDbUserName(dbUserName);
        tenant.setDbPassword(dbPassword);
        tenant.setDbName(dbName);
        tenant.setOwnerUserName(ownerUserName);
        tenant.setOwnerPassword(ownerPassword);
        tenant.setAddress(null);
        tenant.setCity(null);

        tenant.setCreationStatus(DatabaseCreationStatus.IN_PROGRESS.getValue());
        tenant = this.tenantRepository.saveAndFlush(tenant);
//
        GlobalReponse response = new GlobalReponse();

        try {

            tenantDao.createTenantDb(dbName, dbUserName, dbPassword,tenantDto.getIndustryCode(),tenantDto.getCode());
            tenant.setCreationStatus(DatabaseCreationStatus.CREATED.getValue());

        } catch (Exception e) {

            log.error("Failed to create tenant db: " + e.getMessage());
            tenant.setCreationStatus(DatabaseCreationStatus.FAILED_TO_CREATE.getValue());

        } finally {

            tenant = this.tenantRepository.saveAndFlush(tenant);
        }
//        entityManager.flush();

        if (DatabaseCreationStatus.CREATED.getValue().equals(tenant.getCreationStatus())) {

//            liquibaseService.enableMigrationsToTenantDatasource(dbName, dbUserName, dbPassword);

            UserDto admin = new UserDto();
            admin.setTenantId(tenant.getId());
            admin.setUserName("WebService");
            admin.setPassword("WebService");
            admin.setFullName("WebService");

            Integer mainTenantId = tenant.getId();

            createUser(admin, true, mainTenantId);
            Map<Object, Object> configuredDataSources = datasourceConfigService
                    .configureDataSources();

            dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);

            dataSourceContextHolder.setCurrentTenantId(new Long(mainTenantId));

            Optional<Tenant> opNewTenant = this.tenantRepository.findById(0);
            Tenant newTenant = opNewTenant.isPresent() ? opNewTenant.get() : tenant;
            newTenant.setIsFirstLogin("Y");
            modelMapper.map(tenantDto, newTenant);

            newTenant = this.tenantRepository.saveAndFlush(newTenant);

//            try{
//                String listOrgJson = "[]";
//                if(tenantDto.getOrgs() != null && tenantDto.getOrgs().size() > 0){
//                    listOrgJson = objectMapper.writeValueAsString(tenantDto.getOrgs());
//                }
//                log.info("listOrgJson: {}", listOrgJson);
//                Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_pos_masterdata_gen(:industryCode, :numberTableCode, cast(:listOrg as jsonb))");
//                queryProcedure.setParameter("industryCode", tenantDto.getIndustryCode());
//                queryProcedure.setParameter("numberTableCode", tenantDto.getNumberOfTables());
//                queryProcedure.setParameter("listOrg", listOrgJson);
//                queryProcedure.executeUpdate();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }


            userDto.setTenantId(newTenant.getId());
            userDto.setGoogleId(tenantDto.getGoogleId());
            userDto.setZaloId(tenantDto.getZaloId());
            userDto.setFacebookId(tenantDto.getFacebookId());
            userDto.setTiktokId(tenantDto.getTiktokId());
            userDto.setIServerId(tenantDto.getIServerId());
            userDto.setAppleId(tenantDto.getAppleId());
            userDto.setIsTenantAdmin("Y");
            userDto.setOrgs(tenantDto.getOrgs());
            userDto.setIndustryCode(tenantDto.getIndustryCode());
            userDto.setNumberOfTables(tenantDto.getNumberOfTables());
            createUser(userDto, false, mainTenantId);
        }


//        response.setData(tenant != null ? modelMapper.map(tenant, TenantDto.class) : null);
        response.setData(CreateTenantRespDto.builder()
                .id(tenant.getId())
                .tenantName(tenant.getName())
                .tenantCode(tenant.getCode())
                .industryCode(tenant.getIndustryCode())
                .domainUrl(tenant.getDomainUrl())
                .ownerUserName(tenantDto.getUser().getUserName())
                .ownerPassword(tenantDto.getUser().getPassword()).build());

        response.setStatus(DatabaseCreationStatus.CREATED.getValue().equals(tenant.getCreationStatus()) ? HttpStatus.CREATED.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(messageSource.getMessage("successfully", null, AppConstant.LOCALE_VI));

        return response;
    }

    private LocalDate computeExpiryDate(String expiryString) {
        LocalDate today = LocalDate.now();
        if (expiryString.contains("T")) {
            Duration duration = Duration.parse(expiryString);

            // Option A: round down to whole days
            long days = duration.toDays();
            return today.plusDays(days);

            // Option B: preserve hours/minutes, then truncate back to LocalDate
            // LocalDateTime dt = today.atStartOfDay().plus(duration);
            // return dt.toLocalDate();
        } else {
            Period period = Period.parse(expiryString);
            return today.plus(period);
        }
    }


    @Override
    public GlobalReponse getByIdForIDServerInfo(Integer tenantId) {

        Tenant tenant = this.tenantRepository.findById(tenantId).get();
        IDTenantLoginInfoDto dto = modelMapper.map(tenant, IDTenantLoginInfoDto.class);
        GlobalReponse response = new GlobalReponse();
        response.setData(dto);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));

        return response;
    }

    /**
     * @param getAssetsImageDto
     * @return
     */
    @Override
    public GlobalReponse getAssetsImage(GetAssetsImageDto getAssetsImageDto) {
        String urlGetImage = commonService.getSysValueByName("D_MDM_GET_IMAGE_ASSET");
        HttpHeaders headers = new HttpHeaders();
        Tenant tenant = this.getTenantById(AuditContext.getAuditInfo().getMainTenantId());
        try {
            headers.setContentType(MediaType.APPLICATION_JSON);
//            getAssetsImageDto.setIndustryCode(tenant.getIndustryCode());
//            getAssetsImageDto.setTenantCode(tenant.getCode());
            // tam thoi code cung a Dan keu
            getAssetsImageDto.setTenantCode("onsen");
            getAssetsImageDto.setIndustryCode("FNB");

            List<GetAssetsImageDto> dataList = new ArrayList<>();
            dataList.add(getAssetsImageDto);
            Map<String, List<GetAssetsImageDto>> dataMap = new HashMap<>();
            dataMap.put("data", dataList);
            String jsonString = objectMapper.writeValueAsString(dataMap);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response = externalRestTemplate.exchange(urlGetImage, HttpMethod.POST, requestEntity, String.class);
            String responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.get("resultCode").asText().equals("00")) {
                return GlobalReponse.builder()
                        .data(jsonNode.get("data"))
                        .status(HttpStatus.OK.value())
                        .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                        .errors("").build();
            } else {
                return GlobalReponse.builder()
                        .message(jsonNode.get("errorMessage").asText())
                        .status(HttpStatus.OK.value())
                        .data(null).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     *
     * @param tenantDto
     * @return
     */
    @Override
    public GlobalReponse update(TenantDto tenantDto) {
        Tenant tenant = tenantRepository.findById(tenantDto.getId()).get();
        modelMapper.map(tenantDto, tenant);
        tenant = tenantRepository.save(tenant);
        return GlobalReponse.builder().status(HttpStatus.OK.value()).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();
    }

    @Override
    public void changeTenantByMerchantCode(String merchantCode) {

        String sql1 = "select d_tenant_id from pos.d_tenant where 1 = 1 and np_merchant_code = :code";

        List<?> resultList = entityManager.createNativeQuery(sql1)
                .setParameter("code", merchantCode)
                .getResultList();

        if (resultList.isEmpty()) {
            throw new PosException("Tenant not found");
        }

        Integer tenantId = ((Number) resultList.get(0)).intValue();

        try {
            int tenantNumbers = getTenantNumbers();
            if (tenantNumbers != datasourceConfigService.getTenantNumbers()) {
                Map<Object, Object> configuredDataSources = datasourceConfigService
                        .configureDataSources();

                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
            }
            if (tenantId != 0) {
                dataSourceContextHolder.setCurrentTenantId(Long.valueOf(tenantId.toString()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);
            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "vi", tenantId));

        } catch (Exception e) {
            log.error("Something went wrong", e);
        }

    }

    @Transactional  // Ensure the method runs in a transaction
    public void saveNewTenant(Tenant tenantDto) {
        Tenant newTenant = modelMapper.map(tenantDto, Tenant.class);

        entityManager.createNativeQuery("INSERT INTO pos.d_tenant (id, name, code, domain_url, d_industry_id, tax_code, is_active, created_by, updated_by) " +
                        "VALUES (:id, :name, :code, :domainUrl, :industryId, :taxCode, :isActive, :createdBy, :updatedBy)")
                .setParameter("id", newTenant.getId())
                .setParameter("name", newTenant.getName())
                .setParameter("code", newTenant.getCode())
                .setParameter("domainUrl", newTenant.getDomainUrl())
                .setParameter("industryId", newTenant.getIndustryId())
                .setParameter("taxCode", newTenant.getTaxCode())
                .setParameter("isActive", newTenant.getIsActive())
                .setParameter("createdBy", 0)  // Pass created_by parameter
                .setParameter("updatedBy", 0)  // Pass updated_by parameter
                .executeUpdate();
        entityManager.flush();
    }


    public boolean createUser(UserDto dto, boolean isTenantAdmin, Integer mainTenantId) {

        // Create headers
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
// Add headers as needed
        if (!isTenantAdmin) headers.set("tenantId", mainTenantId.toString());

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(dto, headers);
        //Response from Invoice Service
        GlobalReponse reponseInvoice = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.USER_SERVICE_CREATE_API_URL,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();
        if (reponseInvoice.getStatus().intValue() != HttpStatus.OK.value()
                && reponseInvoice.getStatus().intValue() != HttpStatus.CREATED.value()) {
            throw new PosException(messageSource.getMessage("create.tenant.failed", null, AppConstant.LOCALE_VI));
        }

        return true;
    }

    public int getTenantNumbers() {

        try {
            Object ob = redisTemplate.opsForValue().get("tenant");

            if (ob != null) {
                String count = String.valueOf(ob);
                return Integer.parseInt(count);
            }
        }catch (Exception e){
            log.error("Error while getting tenant numbers from Redis: {}", e.getMessage());
        }

        return 0;
    }

    @Override
    public GlobalReponse save(TenantDto entity) {

        GlobalReponse response = new GlobalReponse();
        try{
            Tenant tenant = tenantRepository.findById(AuditContext.getAuditInfo().getTenantId()).get();
            modelMapper.map(entity, tenant);
            tenant = this.tenantRepository.save(tenant);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            response.setData(entity);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    private Tenant getTenantById(Integer tenantId) {
        dataSourceContextHolder.setCurrentTenantId(null);
        Tenant tenant = tenantRepository.findById(tenantId).get();
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        return tenant;
    }
}
