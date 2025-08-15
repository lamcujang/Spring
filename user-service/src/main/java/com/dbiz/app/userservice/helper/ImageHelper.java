package com.dbiz.app.userservice.helper;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.TenantBanner;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.Image;
import com.dbiz.app.userservice.repository.ImageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.externalRequest.SaveImageMDMRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageHelper {
    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;
    public Image saveImage(ImageDto imageDto)
    {
        log.info("save Image ");
        TenantAndOrgDto tenantAndOrgDto;
        GlobalReponse callExService;
        if(imageDto.getTenantCode() == null)
        {
            callExService =restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE+"/"+ AuditContext.getAuditInfo().getTenantId()+"/"+AuditContext.getAuditInfo().getOrgId(), GlobalReponse.class);
            tenantAndOrgDto = modelMapper.map(callExService.getData(), TenantAndOrgDto.class);
        }else {
            tenantAndOrgDto = TenantAndOrgDto.builder().codeTenant(imageDto.getTenantCode()).codeIndustry(imageDto.getIndustryCode()).build();
        }
        SaveImageMDMRequest mdmRequest = new SaveImageMDMRequest();
        mdmRequest.setTenantCode(tenantAndOrgDto.getCodeTenant());
        mdmRequest.setBranchCode(tenantAndOrgDto.getCodeIndustry());
        mdmRequest.setImage64(imageDto.getImage64());
        mdmRequest.setImageCode(imageDto.getImageCode());
        callExService = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_HOST_FINDBYNAME+ AppConstant.NameSystemConfig.MDM_URL_SAVE_IMAGE, GlobalReponse.class);
        String urlMDM =String.valueOf( callExService.getData());

        Map<String, Object> finalRequest = new HashMap<>();
        finalRequest.put("data", List.of(mdmRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(finalRequest, headers);

        ResponseEntity<String> response = externalRestTemplate.exchange(urlMDM, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode root = objectMapper.readTree(responseBody);

                String resultCode = root.path("resultCode").asText();
                String errorMessage = root.path("errorMessage").asText(null);
                JsonNode data = root.path("data");
                log.info("resultCode: " + resultCode);
                log.info("errorMessage: " + errorMessage);
                log.info("data: " + data);

                if (resultCode.equals("00") && data.isArray()) {
                    for (JsonNode item : data) {
                        String urlValue = item.path("url").asText();
                        String imageCode = item.path("imageCode").asText();

                        Image  image = this.imageRepository.findByImageCode(imageCode);
                        if(image!=null)
                        {
                            image.setImageUrl(urlValue);
                        }else {
                            image = Image.builder()
                                    .imageUrl(urlValue)
                                    .imageCode(imageCode)
                                    .dTenantId(AuditContext.getAuditInfo().getTenantId()).build();
                        }

                        image =	imageRepository.save(image);
                        return image;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
          log.info("Error: " + response.getStatusCode());
        }
        return null;
    }

}
