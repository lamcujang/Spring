package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.User;
import com.dbiz.app.userservice.helper.PasswordHelper;
import com.dbiz.app.userservice.repository.UserRepository;
import com.dbiz.app.userservice.service.UserService;
import com.dbiz.app.userservice.service.UserV2Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.IdentityDto.Tenant.IDTenantLoginInfoDto;
import org.common.dbiz.dto.IdentityDto.User.*;
import org.common.dbiz.dto.kafka.EmailKafkaDto;
import org.common.dbiz.dto.userDto.AuthDto;
import org.common.dbiz.dto.userDto.password.ChangePasswordDto;
import org.common.dbiz.dto.userDto.password.EmailDto;
import org.common.dbiz.dto.userDto.password.VerifyCodeDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.*;


@Service
@Slf4j
@DependsOn("dataSourceRouting")
@RequiredArgsConstructor
public class UserV2ServiceImpl implements UserV2Service {

    private final UserRepository userRepository;
    private final UserService userService;
    private final DataSourceContextHolder dataSourceContextHolder;

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final EntityManager entityManager;

    private final PasswordHelper passwordHelper;

    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String TOPIC_SEND_HTML_MAIL  = "send-html-mail";
    @Value("${google.username}")
    private String googleUsername;
    private final KafkaAuditUserHelper kafkaAuditUserHelper;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    @Override
    public GlobalReponse login(UserDto userLoginDto) {

        com.dbiz.app.tenantservice.domain.AuditInfo auditInfo = AuditContext.getAuditInfo();
        log.info("*** UserDto, resource; fetch user with username and password and tenant Id  *");

        IDLoginSocialDto idLoginSocialDto = null;
        String attribute = null;
        String filter = null;


        Optional<User> user = null;




        if(userLoginDto.getGoogleId() != null
                && !userLoginDto.getGoogleId().isEmpty() && !userLoginDto.getGoogleId().isBlank()){

            attribute = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.googleid";
            filter = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.googleid eq " + userLoginDto.getGoogleId();
            user = this.userRepository.findByGoogleIdAndTenantId(userLoginDto.getGoogleId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userLoginDto.getZaloId() != null
                && !userLoginDto.getZaloId().isEmpty() && !userLoginDto.getZaloId().isBlank()){

            attribute = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.zaloid";
            filter = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.zaloid eq " + userLoginDto.getZaloId();
            user = this.userRepository.findByZaloIdAndTenantId(userLoginDto.getZaloId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userLoginDto.getFacebookId() != null
                && !userLoginDto.getFacebookId().isEmpty() && !userLoginDto.getFacebookId().isBlank()){

            attribute = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.facebookid";
            filter = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.facebookid eq " + userLoginDto.getFacebookId();
            user = this.userRepository.findByFacebookIdAndTenantId(userLoginDto.getFacebookId(),AuditContext.getAuditInfo().getTenantId());
        }else if (userLoginDto.getAppleId() != null
                && !userLoginDto.getAppleId().isEmpty() && !userLoginDto.getAppleId().isBlank()){

            attribute = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.appleid";
            filter = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.appleid eq " + userLoginDto.getAppleId();
            user = this.userRepository.findByAppleIdAndTenantId(userLoginDto.getAppleId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userLoginDto.getUserName() != null
                && !userLoginDto.getUserName().isEmpty() && !userLoginDto.getUserName().isBlank()){

        }
        boolean isSocial = false;
        if(attribute != null && filter != null){

            if(!user.isPresent()) throw new PosException(messageSource.getMessage("not.registered.social", null, LocaleContextHolder.getLocale()));

            idLoginSocialDto = IDLoginSocialDto.builder()
                    .schemas(List.of("urn:ietf:params:scim:api:messages:2.0:SearchRequest"))
                    .attributes(List.of(attribute))
                    .filter(filter)
                    .domain("PRIMARY")
                    .startIndex(1)
                    .count(10)
                    .build();
            isSocial =  true;
        }

//        boolean checkLoginIDServer = checkIdentityLogin(userLoginDto.getUserName(), userLoginDto.getPassword(),idLoginSocialDto,isSocial);

        UserLoginDto userDto  = null;
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Failed";
//        if(checkLoginIDServer){
//            if(!isSocial){
//                userDto = this.userService.findByUserNdTenantPass(userLoginDto.getUserName(), userLoginDto.getPassword(),auditInfo.getTenantId());
//            }else{
//                userDto = this.userService.findBySocial(userLoginDto,auditInfo.getTenantId());
//            }
//            message = messageSource.getMessage("login_sucess",null , LocaleContextHolder.getLocale());
//            status = HttpStatus.OK.value();
//        }else{
//            if(isSocial){
//                throw new PosException(messageSource.getMessage("not.registered.social", null, LocaleContextHolder.getLocale()));
//            }else{
//                throw new PosException(messageSource.getMessage("incorrect_username_or_password", null, LocaleContextHolder.getLocale()));
//            }
//        }

        if(!isSocial){
            userDto = this.userService.findByUserNdTenantPass(userLoginDto.getUserName(), userLoginDto.getPassword(),auditInfo.getTenantId());
        }else{
            userDto = this.userService.findBySocial(userLoginDto,auditInfo.getTenantId());
        }
        message = messageSource.getMessage("login_sucess",null , LocaleContextHolder.getLocale());
        status = HttpStatus.OK.value();

        if(userDto != null){
            if(isFirstLogin()){
                userDto.setIsFirstLogin("Y");
            }else{
                userDto.setIsFirstLogin("N");
            }
        }

        GlobalReponse globalReponse = new GlobalReponse();
        //lay thong tin login

        globalReponse.setMessage(message);
        globalReponse.setData(userDto);
        globalReponse.setStatus(status);
        globalReponse.setErrors("");
        return globalReponse;
    }

    public boolean checkIdentityLogin(String userName, String password, IDLoginSocialDto dto, boolean isSocial){

        dataSourceContextHolder.setCurrentTenantId(null);
        IDTenantLoginInfoDto tenantDto = getTenantIDINFO();
        if(tenantDto == null)
            throw new PosException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        String domainUrl = getSysByValue(AppConstant.SYS_VALUE.ID_DOMAIN_URL);
        String uri = isSocial ? getSysByValue(AppConstant.SYS_VALUE.ID_LOGIN_SOCIAL_URL)
                : getSysByValue(AppConstant.SYS_VALUE.ID_LOGIN_USRPASS_URL);

        String tenantCode = tenantDto.getIndustryCode() + "." + tenantDto.getCode();
        uri = uri.replace("@TENANT_CODE@",tenantCode);

        //IDENTITY USER INFO


        HttpHeaders headers = new HttpHeaders();
        if(isSocial){
            headers.set("Content-Type", "application/scim+json");  // Đặt Content-Type với charset
        }else{
            headers.set("Content-Type", "application/json");  // Đặt Content-Type với charset
        }
//        headers.setAccept(List.of(MediaType.AP));
        if(isSocial){
            String userNameAdmin = tenantDto.getOwnerUserName() + "@" + tenantCode;
            headers.setBasicAuth(userNameAdmin,tenantDto.getOwnerPassword());
        }else{
            userName = userName + "@" + tenantCode;
            headers.setBasicAuth(userName,password);
        }
        HttpEntity<IDLoginSocialDto> requestEntity = null;
        ResponseEntity<String> rsp = null;
        try{
            String url = domainUrl + uri;
            if(isSocial){
                log.info("payload: " + objectMapper.writeValueAsString(dto));
                requestEntity = new HttpEntity<>(dto, headers);
            }else{
                requestEntity = new HttpEntity<>( headers);

            }

            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){

                log.info("response from id server: " + rsp.getBody());

                //Login social failed
                if(isSocial){
                    JsonNode jsonNode = objectMapper.readTree(rsp.getBody());
                    if(jsonNode.get("totalResults") != null && jsonNode.get("totalResults").asInt() == 0){
                        return false;
                    }
                }
                dataSourceContextHolder.setCurrentTenantId(new Long (AuditContext.getAuditInfo().getMainTenantId()));
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //GET TENENT ID INFO
    public IDTenantLoginInfoDto getTenantIDINFO() {

        String sql = "SELECT owner_username , " +
                "owner_password, " +
                "code, " +
                "industry_code " +
                " FROM pos.d_tenant WHERE d_tenant_id = :tenantId " +
                " AND db_name is not null ";



        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getMainTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        IDTenantLoginInfoDto tenantDto = null;
        for ( Map<String, Object> row : results ) {
            log.info("Row: {}", row);

            tenantDto = IDTenantLoginInfoDto.builder()
                    .ownerUserName(ParseHelper.STRING.parse(row.get("owner_username")))
                    .ownerPassword(ParseHelper.STRING.parse(row.get("owner_password")))
                    .code(ParseHelper.STRING.parse(row.get("code")))
                    .industryCode(ParseHelper.STRING.parse(row.get("industry_code")))
                    .build();
        }

        return tenantDto;
    }

    //GET TENENT ID INFO
    public String getSysByValue(String value) {

        String sql = "SELECT value  " +
                " FROM pos.d_config WHERE d_tenant_id = :tenantId " +
                " AND name = :name ";



        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("name", value)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        IDTenantLoginInfoDto tenantDto = null;
        for ( Map<String, Object> row : results ) {
            log.info("Row: {}", row);

            return ParseHelper.STRING.parse(row.get("value"));
        }

        return "";
    }


    @Override
    public GlobalReponse register(UserDto userDto) {
        Optional<User> user = null;
        Optional<User> userSocial = null;
        List<User> userByPhone = null;



        if(userDto.getGoogleId() != null
                && !userDto.getGoogleId().isEmpty() && !userDto.getGoogleId().isBlank()){

            userSocial = this.userRepository.findByGoogleIdAndTenantId(userDto.getGoogleId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userDto.getZaloId() != null
                && !userDto.getZaloId().isEmpty() && !userDto.getZaloId().isBlank()){
            userSocial = this.userRepository.findByZaloIdAndTenantId(userDto.getZaloId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userDto.getFacebookId() != null
                && !userDto.getFacebookId().isEmpty() && !userDto.getFacebookId().isBlank()){

            userSocial = this.userRepository.findByFacebookIdAndTenantId(userDto.getFacebookId(),AuditContext.getAuditInfo().getTenantId());

        }else if (userDto.getUserName() != null
                && !userDto.getUserName().isEmpty() && !userDto.getUserName().isBlank()){
            user = this.userRepository.findByUserNameAndTenantId(userDto.getUserName() ,AuditContext.getAuditInfo().getTenantId());

            if(!passwordHelper.isValidPassword(userDto.getPassword())){
                throw new PosException(messageSource.getMessage("password.not.valid", null, LocaleContextHolder.getLocale()));
            }
        }

        if(userSocial != null && userSocial.isPresent()) throw new PosException(messageSource.getMessage("already.registered.social", null, LocaleContextHolder.getLocale()));
        if(user != null && user.isPresent()) throw new PosException(messageSource.getMessage("user_name_exist", null, LocaleContextHolder.getLocale()));

        if(userDto.getPhone() != null && !userDto.getPhone().isEmpty() && !userDto.getPhone().isBlank()){
            userByPhone = this.userRepository.findByPhoneAndTenantId(userDto.getPhone(), AuditContext.getAuditInfo().getTenantId());
            if(userByPhone != null && !userByPhone.isEmpty()) throw new PosException(messageSource.getMessage("phone_exist", null, LocaleContextHolder.getLocale()));
        }
        if(userDto.getEmail() != null && !userDto.getEmail().isEmpty() && !userDto.getEmail().isBlank()){
            userByPhone = this.userRepository.findByEmailAndTenantId(userDto.getEmail(), AuditContext.getAuditInfo().getTenantId());
            if(userByPhone != null && !userByPhone.isEmpty()) throw new PosException(messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));
        }

        boolean check = IDCreateUser(userDto);
        if (!check) throw new PosException(messageSource.getMessage("register.failed", null, LocaleContextHolder.getLocale()));

        return this.userService.saveAll(userDto);

    }

    @Override
    public GlobalReponse sendVerifyEmail(EmailDto emailDto) {

        log.info("Send verify email" + emailDto.getEmail());

        List<User> userList = this.userRepository
                .findByPhoneOrUserNameAndTenantId(emailDto.getUserName(),
                        emailDto.getUserName() ,
                        AuditContext.getAuditInfo().getTenantId());
        if (userList == null || userList.isEmpty()) {
            throw new PosException(messageSource.getMessage("invalid.info", null, LocaleContextHolder.getLocale()));
        }
        User user = userList.get(0);

        if (user.getEmail() == null) {
            throw new PosException(messageSource.getMessage("email.not.exist", null, LocaleContextHolder.getLocale()));
        }
        // Generate a random verification code
        String verificationCode = generateVerificationCode();

        user.setVerifyCode(verificationCode);
        this.userRepository.save(user);

        // Save the verification code in a database or cache (not shown here)

        // Send the verification email
        boolean emailSent = sendEmail(user.getEmail(), verificationCode,user.getFullName());
        if (!emailSent) {
            throw new PosException(messageSource.getMessage("email.sent.failed", null, LocaleContextHolder.getLocale()));
        }
        emailDto.setEmail(user.getEmail());

        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setData(emailDto);
        globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return globalReponse;

    }

    private String generateVerificationCode() {
        int codeLength = 6;
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));  // Generates a digit from 0 to 9
        }

        return code.toString();
    }

    private boolean sendEmail(String to, String verificationCode, String fullName) {

        try {
            // HTML content with placeholders for fullName and verificationCode
            String htmlContent = String.format(
                    "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                            + "<p>Xin chào <strong>%s</strong>,</p>"
                            + "<p>Bạn nhận được email này vì đã yêu cầu lấy lại mật khẩu đăng nhập. "
                            + "Để bảo vệ tài khoản của bạn, vui lòng nhập mã xác minh bên dưới để hoàn tất quá trình xác thực."
                            + "<br>Lưu ý rằng mã này chỉ có hiệu lực trong 2 phút, và nếu bạn yêu cầu mã mới, mã hiện tại sẽ tự động hết hiệu lực.</p>"
                            + "<p>Mã OTP của bạn là: <strong style='color: #0000FF;'>%s</strong></p>"
                            + "<p><i>Quý khách không trả lời lại mail này.</i></p>"
                            + "<p>Trân trọng,<br><b>Quản trị hệ thống DBIZ POS</b></p>"
                            + "</div>",
                    fullName, verificationCode
            );

            EmailKafkaDto dto = EmailKafkaDto.builder()
                    .to(to)
                    .from(googleUsername)
                    .message(htmlContent)
                    .subject("DBIZ POS - THÔNG BÁO MÃ XÁC THỰC")
                    .build();
            kafkaTemplate.send(TOPIC_SEND_HTML_MAIL, dto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public GlobalReponse verifyEmail(VerifyCodeDto verifyCodeDto) {

        log.info("Verify email code" + verifyCodeDto.getVerifyCode());

        List<User> userList = this.userRepository
                .findByPhoneOrUserNameAndTenantId(verifyCodeDto.getUserName(),
                        verifyCodeDto.getUserName() ,
                        AuditContext.getAuditInfo().getTenantId());
        if (userList == null || userList.isEmpty()) {
            throw new PosException(messageSource.getMessage("invalid.info", null, LocaleContextHolder.getLocale()));
        }

        User user = userList.get(0);
        if (!verifyCodeDto.getVerifyCode().equals(user.getVerifyCode())) {
            throw new PosException(messageSource.getMessage("verify_code.incorrect", null, LocaleContextHolder.getLocale()));
        }

        user.setVerifyCode(null);
        this.userRepository.save(user);

        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return globalReponse;

    }

    @Override
    public GlobalReponse changePassword(ChangePasswordDto dto) {

        log.info("Change password for user: " + dto.getUserName() + " with new password: "
                + dto.getNewPassword() + " and old password: " + dto.getOldPassword() );

        if(!passwordHelper.isValidPassword(dto.getNewPassword())){
            throw new PosException(messageSource.getMessage("password.not.valid", null, LocaleContextHolder.getLocale()));
        }

        List<User> userList = this.userRepository
                .findByPhoneOrUserNameAndTenantId(dto.getUserName(),
                        dto.getUserName() ,
                        AuditContext.getAuditInfo().getTenantId());
        if (userList == null || userList.isEmpty()) {
            throw new PosException(messageSource.getMessage("invalid.info", null, LocaleContextHolder.getLocale()));
        }
        User user = userList.get(0);
        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new PosException(messageSource.getMessage("old.password.incorrect", null, LocaleContextHolder.getLocale()));
        }

        boolean check = IDChangePassword(dto,user.getIServerId());
        if (!check) throw new PosException(messageSource.getMessage("change.password.failed", null, LocaleContextHolder.getLocale()));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.userRepository.save(user);

        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return globalReponse;
    }

    @Override
    public GlobalReponse cancelVerifyEmail(EmailDto emailDto) {

        log.info("Cancel verify email" + emailDto.getUserName());

        List<User> userList = this.userRepository
                .findByPhoneOrUserNameAndTenantId(emailDto.getUserName(),
                        emailDto.getUserName() ,
                        AuditContext.getAuditInfo().getTenantId());
        if (userList == null || userList.isEmpty()) {
            throw new PosException(messageSource.getMessage("invalid.info", null, LocaleContextHolder.getLocale()));
        }
        User user = userList.get(0);

        user.setVerifyCode(null);
        this.userRepository.save(user);

        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return globalReponse;
    }

    @Override
    public GlobalReponse forgotPassword(ChangePasswordDto dto) {
        log.info("Forgot password for user: " + dto.getUserName()  );

        if(!passwordHelper.isValidPassword(dto.getNewPassword())){
            throw new PosException(messageSource.getMessage("password.not.valid", null, LocaleContextHolder.getLocale()));
        }
        List<User> userList = this.userRepository
                .findByPhoneOrUserNameAndTenantId(dto.getUserName(),
                        dto.getUserName() ,
                        AuditContext.getAuditInfo().getTenantId());
        if (userList == null || userList.isEmpty()) {
            throw new PosException(messageSource.getMessage("invalid.info", null, LocaleContextHolder.getLocale()));
        }
        User user = userList.get(0);

        boolean check = IDChangePassword(dto,user.getIServerId());
        if (!check) throw new PosException(messageSource.getMessage("change.password.failed", null, LocaleContextHolder.getLocale()));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.userRepository.save(user);

        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return globalReponse;
    }

    @Override
    public GlobalReponse update(UserDto dto ) {

        Optional<User> userOp = this.userRepository.findById(dto.getUserId());
        if(!userOp.isPresent()) {
            throw new PosException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        User user = userOp.get();
        if(user.getIServerId() != null) {
            boolean check = IDUpdateUser(dto, user.getIServerId());
            if (!check) throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        GlobalReponse response = this.userService.saveAll(dto);

        try { // Send Kafka save Audit for user
            Map<String, String> userInfo = Map.of(
                    "name", userOp.get().getFullName(),
                    "document_no", userOp.get().getEmail()
            );
            String userInfoJson = objectMapper.writeValueAsString(userInfo);

            // Send Kafka save AuditUser for Save User
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(userOp.get()),
                    userOp.get().getUserId(),
                    "UPDATE",
                    AuditContext.getAuditInfo().getUserId(),
                    userInfoJson);
        } catch (Exception e) {
            log.info("Error: UserServiceImpl: save(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: UserServiceImpl: save(): send Kafka save AuditLogUser"); // thêm messageSource
        }
        return response;
    }

    @Override
    public GlobalReponse authentication(AuthDto dto) {

//        Optional<User> userOp = this.userRepository.findById(dto.getUserId());

        Optional<User> user = dto.getClientSecret() != null && dto.getGrantType() != null
                ? this.userRepository.findByClientIdAndClientSecretAndGrantType(
                dto.getClientId(), dto.getClientSecret(), dto.getGrantType()) : this.userRepository.findByClientId(dto.getClientId()) ;

        if(!user.isPresent()) {
            return GlobalReponse.builder()
                    .message(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }


    public boolean IDCreateUser(UserDto dto){


        dataSourceContextHolder.setCurrentTenantId(null);
        IDTenantLoginInfoDto tenantDto = getTenantIDINFO();

        String domainUrl = getSysByValue(AppConstant.SYS_VALUE.ID_DOMAIN_URL);
        String uri = getSysByValue(AppConstant.SYS_VALUE.ID_CREATE_USER_URL);

        String tenantCode = tenantDto.getIndustryCode() + "." + tenantDto.getCode();
        uri = uri.replace("@TENANT_CODE@",tenantCode);

        //IDENTITY USER INFO
        IDUserDto userDto = IDUserDto.builder()
                .emails(dto.getEmail() != null ? List.of(dto.getEmail()) : new ArrayList<>())
                .password(dto.getPassword())
                .schemas(new ArrayList<>())
                .userName(dto.getUserName())
                .phoneNumbers(List.of(IDPhoneListDto.builder()
                        .type("mobile")
                        .value(dto.getPhone())
                        .build()))
                .full_name(dto.getFullName())
                .scimUserExtension(IDUserDto.SCIMUserExtension.builder()
                        .googleid(dto.getGoogleId() != null ? dto.getGoogleId() : "")
                        .zaloid(dto.getZaloId() != null ? dto.getZaloId() : "")
                        .facebookid(dto.getFacebookId()   != null ? dto.getFacebookId() : "")
                        .tiktokid(dto.getTiktokId() != null ? dto.getTiktokId() : "")
                        .build())
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/scim+json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
        String userNameAdmin = tenantDto.getOwnerUserName() + "@" + tenantCode;
        headers.setBasicAuth(userNameAdmin,tenantDto.getOwnerPassword());
        ResponseEntity<String> rsp = null;

        try{
            log.info("payload: " + objectMapper.writeValueAsString(userDto));
            HttpEntity<IDUserDto> requestEntity = new HttpEntity<>(userDto, headers);
            String url = domainUrl + uri;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.CREATED.value()){
                JsonNode jsonNode = objectMapper.readTree(rsp.getBody());
                String iServerId = jsonNode.path("id").asText();
                dto.setIServerId(iServerId);
                log.info("response from id server: " + rsp.getBody());
                dataSourceContextHolder.setCurrentTenantId(new Long (AuditContext.getAuditInfo().getMainTenantId()));
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean IDChangePassword(ChangePasswordDto dto,String id){


        dataSourceContextHolder.setCurrentTenantId(null);
        IDTenantLoginInfoDto tenantDto = getTenantIDINFO();

        String domainUrl = getSysByValue(AppConstant.SYS_VALUE.ID_DOMAIN_URL);
        String uri = getSysByValue(AppConstant.SYS_VALUE.ID_UPDATE_USER_URL);

        String tenantCode = tenantDto.getIndustryCode() + "." + tenantDto.getCode();
        uri = uri.replace("@TENANT_CODE@",tenantCode);

        //IDENTITY USER INFO
        IDChangePasswordDto idChangePassword = IDChangePasswordDto.builder()
                .schemas(List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"))
                .Operations(List.of(IDChangePasswordDto.IDOperations.builder()
                        .op("replace")
                        .value(IDChangePasswordDto.IDPassword.builder()
                                .password(dto.getNewPassword())
                                .build())
                        .build()))
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/scim+json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
        String userNameAdmin = tenantDto.getOwnerUserName() + "@" + tenantCode;
        headers.setBasicAuth(userNameAdmin,tenantDto.getOwnerPassword());
        ResponseEntity<String> rsp = null;

        try{
            log.info("payload: " + objectMapper.writeValueAsString(idChangePassword));
            HttpEntity<IDChangePasswordDto> requestEntity = new HttpEntity<>(idChangePassword, headers);
            String url = domainUrl + uri + "/" + id;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                log.info("response from id server: " + rsp.getBody());
                dataSourceContextHolder.setCurrentTenantId(new Long (AuditContext.getAuditInfo().getMainTenantId()));
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
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

    public boolean IDUpdateUser(UserDto dto,String iServerId){


        dataSourceContextHolder.setCurrentTenantId(null);
        IDTenantLoginInfoDto tenantDto = getTenantIDINFO();

        String domainUrl = getSysByValue(AppConstant.SYS_VALUE.ID_DOMAIN_URL);
        String uri = getSysByValue(AppConstant.SYS_VALUE.ID_UPDATE_USER_URL);

        String tenantCode = tenantDto.getIndustryCode() + "." + tenantDto.getCode();
        uri = uri.replace("@TENANT_CODE@",tenantCode);

        //IDENTITY USER INFO
        IDUpdateUserDto userDto = IDUpdateUserDto.builder()
                .emails(dto.getEmail() != null ? List.of(dto.getEmail()) : new ArrayList<>())
                .schemas(new ArrayList<>())
                .userName(dto.getUserName())
                .phoneNumbers(List.of(IDPhoneListDto.builder()
                        .type("mobile")
                        .value(dto.getPhone())
                        .build()))
                .full_name(dto.getFullName())
                .scimUserExtension(IDUpdateUserDto.SCIMUserExtension.builder()
                        .googleid(dto.getGoogleId() != null ? dto.getGoogleId() : "")
                        .zaloid(dto.getZaloId() != null ? dto.getZaloId() : "")
                        .facebookid(dto.getFacebookId()   != null ? dto.getFacebookId() : "")
                        .tiktokid(dto.getTiktokId() != null ? dto.getTiktokId() : "")
                        .build())
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/scim+json");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.AP));
        String userNameAdmin = tenantDto.getOwnerUserName() + "@" + tenantCode;
        headers.setBasicAuth(userNameAdmin,tenantDto.getOwnerPassword());
        ResponseEntity<String> rsp = null;

        try{
            log.info("payload: " + objectMapper.writeValueAsString(userDto));
            HttpEntity<IDUpdateUserDto> requestEntity = new HttpEntity<>(userDto, headers);
            String url = domainUrl + uri + "/" + iServerId;
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){

                log.info("response from id server: " + rsp.getBody());
                dataSourceContextHolder.setCurrentTenantId(new Long (AuditContext.getAuditInfo().getMainTenantId()));
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
