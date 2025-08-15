package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.EmployeeBank;
import com.dbiz.app.userservice.repository.EmployeeBankRepository;
import com.dbiz.app.userservice.service.EmployeeBankService;
import com.dbiz.app.userservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.userDto.EmployeeBankDto;
import org.common.dbiz.dto.userDto.EmployeeDto;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeBankQueryRequest;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.common.dbiz.request.userRequest.EmployeeQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeBankServiceImpl implements EmployeeBankService {

    private final EmployeeBankRepository employeeBankRepository;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final ModelMapper modelMapper;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(EmployeeBankQueryRequest request) {
        return null;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }


    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(EmployeeBankDto entity) {
        log.info("save EmployeeBank");
        BankAccountDto bankAccountDto = BankAccountDto.builder()
                .bankId(entity.getBankId())
                .accountNo(entity.getAccountNo())
                .isDefault(entity.getIsDefault())
                .name(entity.getName())
                .orgId(entity.getOrgId())
                .isActive("Y")
                .isDefault("N")
                .bankAccountType("CAR")
                .branch(entity.getBranch()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
        HttpEntity<BankAccountDto> request = new HttpEntity<>(bankAccountDto, headers);
        ResponseEntity<GlobalReponse> response = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.BANKACCOUNT_SERVICE_SAVE,
                HttpMethod.POST,
                request,
                GlobalReponse.class
        );
        GlobalReponse responseSaveBankAccount = response.getBody();

        Object data = responseSaveBankAccount.getData();
        GlobalReponse reponse =  GlobalReponse.builder()

                .message("Success")
                .status(HttpStatus.OK.value()).build();
        if (data instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) data;

            String name = (String) map.get("name");
            String accountNo = (String) map.get("accountNo");
            String bankAccountType = (String) map.get("bankAccountType");
            BankAccountDto bankAccountDtoResponse = BankAccountDto.builder()
                    .id((Integer) map.get("id"))
                    .name(name)
                    .accountNo(accountNo)
                    .bankAccountType(bankAccountType)
                    .build();
            EmployeeBank entitySave = modelMapper.map(entity, EmployeeBank.class);
            entitySave.setBankAccountId(bankAccountDtoResponse.getId());
            reponse.setData(modelMapper.map(entitySave, EmployeeBankDto.class));
        }

        return reponse;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
