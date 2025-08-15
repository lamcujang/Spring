package com.dbiz.app.proxyclient.business.tenant.controller;

    import com.dbiz.app.proxyclient.business.tenant.service.TenantClientService;
import lombok.RequiredArgsConstructor;
    import org.common.dbiz.dto.tenantDto.GetAssetsImageDto;
    import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
public class TenantController {
    private static final Logger log = LoggerFactory.getLogger(TenantController.class);
    private final TenantClientService tenantClientService;

//    @GetMapping("/getAll")
//    public ResponseEntity<DTenantServiceCollectionDtoResponse> findAll(@RequestParam(defaultValue = "0") int page,
//                                                                       @RequestParam(defaultValue = "10") int size) {
//        DTenantServiceCollectionDtoResponse response = this.tenantClientService.getAllTenants(page,size).getBody();
//        return ResponseEntity.ok(this.tenantClientService.getAllTenants(page,size).getBody());
//    }

    @GetMapping("/getbyDomain")
    public ResponseEntity<GlobalReponse> findByDomainURL(@RequestParam("domainUrl") final String domainUrl) {
        log.info("domainUrl: " + domainUrl);
        return ResponseEntity.ok(this.tenantClientService.getByDomainURL(domainUrl).getBody());
    }

    @PutMapping("/updateDomain")
    public ResponseEntity<GlobalReponse> updateDomain(@RequestBody final TenantDto dTenantDto) {
        return ResponseEntity.ok(this.tenantClientService.updateDomain(dTenantDto).getBody());
    }

    @PostMapping("/registerDomain")
    ResponseEntity<GlobalReponse> insertDomain(@RequestBody TenantDto dTenantDto){
        return ResponseEntity.ok(this.tenantClientService.registerDomain(dTenantDto).getBody());
    };

    @GetMapping("/getAllIndustry")
    ResponseEntity<GlobalReponsePagination> getAllIndustries(@RequestParam("page") int page, @RequestParam("size") int size){
        return ResponseEntity.ok(this.tenantClientService.getAllIndustries(page,size).getBody());
    };

    @PostMapping("/createTenant")
    public ResponseEntity<GlobalReponse> createTenant(@RequestBody TenantDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantClientService.createTenant(dto)).getBody();
    };

    @PostMapping("/getImageAsset")
    public ResponseEntity<GlobalReponse> getImageAsset(@RequestBody GetAssetsImageDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantClientService.getImageAsset(dto)).getBody();
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody TenantDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantClientService.update(dto)).getBody();
    }

    @GetMapping("/findById")
    public ResponseEntity<GlobalReponse> findById(@RequestParam("tenantId") Integer id) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantClientService.findById(id)).getBody();
    }
}
