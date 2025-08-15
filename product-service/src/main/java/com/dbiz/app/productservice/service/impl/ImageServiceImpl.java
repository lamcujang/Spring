package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.Image;
import com.dbiz.app.productservice.repository.ImageRepository;
import com.dbiz.app.productservice.service.ImageService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.externalRequest.SaveImageMDMRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {


    private final ModelMapper modelMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final ImageRepository imageRepository;

    @Override
    public ImageDto save(ImageDto image) {
        Image imageEntity = saveImage(image);
        return modelMapper.map(imageEntity, ImageDto.class);
    }

    /**
     * @param imageId
     * @return
     */
    @Override
    public ImageDto findById(Integer imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        return image.map(value -> modelMapper.map(value, ImageDto.class)).orElse(null);
    }

    @Override
    public void deleteById(Integer imageId) {
    }

    public Image saveImage(ImageDto dto) {
        HttpHeaders headersh = new HttpHeaders();
        headersh.setContentType(MediaType.APPLICATION_JSON);
        headersh.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersh);

        log.info("save Image ");
        GlobalReponse callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE + "/" + com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId() + "/" + AuditContext.getAuditInfo().getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
        TenantAndOrgDto tenantAndOrgDto = modelMapper.map(callExService.getData(), TenantAndOrgDto.class);
        SaveImageMDMRequest mdmRequest = new SaveImageMDMRequest();
        mdmRequest.setTenantCode(tenantAndOrgDto.getCodeTenant());
        mdmRequest.setBranchCode(tenantAndOrgDto.getCodeIndustry());
        mdmRequest.setImage64(dto.getImage64());
        mdmRequest.setImageCode(dto.getImageCode());
        callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_HOST_FINDBYNAME + AppConstant.NameSystemConfig.MDM_URL_SAVE_IMAGE,HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
        String urlMDM = String.valueOf(callExService.getData());

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
                        image = imageRepository.save(image);
                        return image;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            System.out.println("API call failed with status code: " + response.getStatusCode());
        }
        return null;
    }
}
