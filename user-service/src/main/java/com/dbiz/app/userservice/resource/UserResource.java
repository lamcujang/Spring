package com.dbiz.app.userservice.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.userservice.helper.UserMapper;
import com.fasterxml.jackson.annotation.JsonView;
import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.VariousUserDto;
import org.common.dbiz.dto.userDto.VariousUserParamDto;
import org.common.dbiz.dto.userDto.jsonView.JsonViewUserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.UserQueryRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dbiz.app.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/users"})
@Slf4j
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public GlobalReponse findById(@PathVariable Integer id) {
        log.info("*** UserDto, resource; fetch user by id ***");
        return this.userService.findByIdRes(id);
    }

    @GetMapping("/username/{username}/{dTenantId}")
    public ResponseEntity<UserDto> findByUsername(
            @PathVariable("username")

            @NotBlank(message = "Input must not blank")
            @Valid final String username,
            @PathVariable("dTenantId") Integer dTenantId) {
        log.info("*** UserDto, resource; fetch user with username and tenant Id  *");
        return ResponseEntity.ok(this.userService.findByUsernameAndDTenantId(username, dTenantId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> findByUsername(
            @PathVariable("username")

            @NotBlank(message = "Input must not blank")
            @Valid final String username) {
        log.info("*** UserDto, resource; fetch user with username *");
        return ResponseEntity.ok(this.userService.findByUsername(username));
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalReponse> Login(
            @RequestBody
            @NotBlank(message = "Input must not blank")
            @Valid final UserDto paramLogin) {
        com.dbiz.app.tenantservice.domain.AuditInfo auditInfo = AuditContext.getAuditInfo();
        log.info("*** UserDto, resource; fetch user with username and password and tenant Id  *");

        GlobalReponse globalReponse = new GlobalReponse();
        //lay thong tin login
        UserLoginDto userDto = this.userService.findByUserNdTenantPass(paramLogin.getUserName(), paramLogin.getPassword(), auditInfo.getTenantId());

        globalReponse.setMessage(messageSource.getMessage("login_sucess", null, LocaleContextHolder.getLocale()));
        globalReponse.setData(userDto);
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return ResponseEntity.ok(globalReponse);
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalReponse> register(
            @RequestBody
            @NotBlank(message = "Input must not blank")
            @Valid final UserDto userRegister) {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        log.info("*** UserDto, resource; fetch user with username and password and tenant Id  *");

        GlobalReponse globalReponse = new GlobalReponse();
        UserLoginDto userDto = userMapper.userLoginDto(this.userService.save(userRegister));
        globalReponse.setMessage(messageSource.getMessage("login_sucess", null, LocaleContextHolder.getLocale()));
        globalReponse.setData(userDto);
        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setErrors("");
        return ResponseEntity.ok(globalReponse);
    }

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(UserQueryRequest request) {
        log.info("*** List<UserDto>, resource; fetch all users ***");
        return this.userService.findAll(request);
    }

    @GetMapping("/posTerminalIdAccess/{userId}/{orgId}")
    public ResponseEntity<GlobalReponse> findPosTerminalIdAccessByUserIdAndOrgId(
            @PathVariable("userId") Integer userId,
            @PathVariable("orgId") Integer orgId) {
        log.info("*** UserDto, resource; fetch posTerminalIdAccess by userId and orgId ***");
        return ResponseEntity.ok(this.userService.findPosTerminalIdAccessByUserIdAndOrgId(userId, orgId));
    }

    @JsonView(JsonViewUserDto.viewJsonIntUser.class)
    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody List<UserIntDto> userDto) {
        log.info("*** UserDto, resource; save user ***");
        GlobalReponse response = new GlobalReponse();
        try {
            response = this.userService.intSave(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
            response.setErrors(e.getMessage());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/intSaveERPNext")
    public ResponseEntity<GlobalReponse> intSaveERPNext(@RequestBody List<UserIntDto> userDto) {
        log.info("*** UserDto, resource; save user ***");
        GlobalReponse response = new GlobalReponse();
        response = this.userService.intSaveERPNext(userDto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getOrgAcc")
    public ResponseEntity<GlobalReponsePagination> getOrgA(@RequestParam("userId") Integer userId,
                                                           @RequestParam("roleId") Integer roleId,
                                                           @RequestParam("page") Integer page,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("name") String name,
                                                           @RequestParam(value = "searchKey", required = false) String searchKey,
                                                           @RequestParam(value = "area", required = false) String area) {
        log.info("*** UserDto, resource; fetch user by userId *");
        return ResponseEntity.ok(this.userService.getOrgAccess(userId, roleId, page, pageSize, name, searchKey, area));
    }

    @GetMapping("/getOrgAccess/{userId}")
    public ResponseEntity<GlobalReponse> getOrgA(@PathVariable("userId") Integer userId) {
        log.info("*** UserDto, resource; fetch user by userId *");
        return ResponseEntity.ok(this.userService.getOrgAccess(userId));
    }

    @GetMapping("/getOrgWarehouseAccess")
    public ResponseEntity<GlobalReponse> getOrgWarehouseAccess(@RequestParam("userId") Integer userId, @RequestParam("orgId") Integer orgId) {
        log.info("*** UserDto, resource; fetch user by userId *");
        return ResponseEntity.ok(this.userService.getOrgWarehouseAccess(userId, orgId));
    }

    @GetMapping("/getWarehouseAccess")
    public ResponseEntity<GlobalReponse> getWarehouseAccess(@RequestParam("userId") Integer userId) {
        log.info("*** UserDto, resource; fetch user by userId *");
        return ResponseEntity.ok(this.userService.getWarehouseAccess(userId));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody UserDto userDto) {
        log.info("*** UserDto, resource; update user ***");
        UserDto user = this.userService.update(userDto);
        user.setPassword(null);
        GlobalReponse globalReponse = GlobalReponse.builder()
                .data(user)
                .message(messageSource.getMessage("user_update", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
        return ResponseEntity.ok(globalReponse);
    }


    @PostMapping("/save")
    public GlobalReponse save(@RequestBody UserDto userDto) {
        log.info("*** UserDto, resource; save user ***");
        return this.userService.saveAll(userDto);
    }

    @GetMapping("/getById")
    public ResponseEntity<GlobalReponse> getByIdAndRoleId(@RequestParam("currentUserId") Integer currentUserId, @RequestParam("userId") Integer userId, @RequestParam("roleId") Integer roleId) {
        log.info("*** UserDto, resource; fetch user by userId *");
        return ResponseEntity.ok(this.userService.getByIdAndRoleId(currentUserId, userId, roleId));
    }


    @PostMapping("/registerNoToken")
    public ResponseEntity<GlobalReponse> registerNoToken(@RequestBody UserDto userDto) {
        log.info("*** UserDto, resource; register user without token ***");
        return ResponseEntity.ok(this.userService.registerNoToken(userDto));
    }


    @GetMapping("/getByErpUserId/{erpUserId}")
    public ResponseEntity<GlobalReponse> getByErpUserId(@PathVariable("erpUserId") Integer erpUserId) {
        log.info("*** UserDto, resource; fetch user by erpUserId *");
        return ResponseEntity.ok(this.userService.getByErpUserId(erpUserId));
    }

    @PostMapping("/saveOrgAccess")
    public ResponseEntity<GlobalReponse> saveOrgAccess(@RequestBody UserDto userDto) {
        log.info("*** UserDto, resource; save user org access ***");
        return ResponseEntity.ok(this.userService.saveOrgAccess(userDto));
    }

    @PostMapping("/variety")
    public ResponseEntity<GlobalReponse> createVariousUser(@RequestBody VariousUserDto userDto) {
        log.info("*** UserDto, resource; save various user ***");
        return ResponseEntity.ok(this.userService.createVariousUser(userDto));
    }

    @GetMapping("/variety")
    public ResponseEntity<GlobalReponsePagination> getVariousUser(@ModelAttribute VariousUserParamDto userDto) {
        log.info("*** UserDto, resource; save various user ***");
        return ResponseEntity.ok(this.userService.getVariousUser(userDto));
    }


    @PostMapping("/handleEx")
    public ResponseEntity<GlobalReponse> handleEx( @RequestBody List<UserDto> param)
    {
        log.info("*** UserDto, resource; handleEx ***");
        return ResponseEntity.ok(this.userService.handleEx(param));
    }
}










