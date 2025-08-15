package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.service.NapasConfigService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.BankIntegrationInfoDto;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSet;


@Service
@Slf4j
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class NapasConfigServiceImpl implements NapasConfigService {

    private final QueryEngine queryEngine;
    private final ModelMapper modelMapper;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Override
    public BankIntegrationInfoDto getBankIntegrationInfo(Integer orgId, Integer posTerminalId) {

        BankIntegrationInfoDto data = null;
        try {

            if(posTerminalId == null) posTerminalId = -1;
            Parameter parameter = new Parameter();
            parameter.add("d_org_id", orgId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
            parameter.add("d_pos_terminal_id", posTerminalId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
            parameter.add("method_name", AppConstant.QRCodeType.NAPAS, Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
            ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_bank_integration_v",parameter);
            while (rs.next()) {
                data = BankIntegrationInfoDto.builder()
                        .binCode(rs.getString("bin_code"))
                        .merchantCode(rs.getString("np_merchant_code"))
                        .branchCode(rs.getString("np_branch_code"))
                        .posCode(right("0" + Integer.toString(rs.getInt("sequence_no")), 2) )
                        .industryCode(rs.getString("industry_code"))
                        .build();
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return data;
    }

    @Override
    public NapasConfigResDto getNapasConfig(String type) {

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        // Add headers as needed
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());

        NapasConfigReqDto napasConfigReqDto = NapasConfigReqDto.builder()
                .type(type)
                .build();

        // Create an HttpEntity with the headers and the request body
        HttpEntity<NapasConfigReqDto> requestEntity = new HttpEntity<>(napasConfigReqDto, headers);
        //Response from Invoice Service
        GlobalReponse response = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_NAPAS_CONFIG ,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();

        if (response == null || response.getStatus() != HttpStatus.OK.value()) {
            throw new PosException("Failed to get Napas token");
        } else {
            return  modelMapper.map(response.getData(), NapasConfigResDto.class );
        }
    }


    public static String right(String value, int length) {
        // To get right characters from a string, change the begin index.
        return value.substring(value.length() - length);
    }
}
