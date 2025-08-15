package com.dbiz.app.userservice.resource;

//import com.dbiz.app.userservice.config.audit.AopAudit;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.VendorQueryRequest;
import com.dbiz.app.userservice.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/vendors"})
@Slf4j
@RequiredArgsConstructor
public class VendorResource {
    private final VendorService vendorService;

    @GetMapping("{vendorId}")
        public GlobalReponse findById(@PathVariable Integer vendorId) {
//        String testSession = (String) session.getAttribute("test_session");
//        System.out.println("call vendor resource session: " + testSession +" ID: "+session.getId());
//        log.info("*** Vendor, resource; fetch vendor by id: {} ***", vendorId);
//        session.setAttribute("resource","Session resource");
        log.info("*** Vendor, resource; fetch vendor by id: {} ***", vendorId);
        return this.vendorService.findById(vendorId);
    }
    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute VendorQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");

        return this.vendorService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody VendorDto vendorDto) {
        log.info("*** Vendor, resource; save vendor: {} ***", vendorDto);

        return this.vendorService.save(vendorDto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody VendorDto vendorDto) {
        log.info("*** Vendor, resource; update vendor: {} ***", vendorDto);

        return this.vendorService.save(vendorDto);
    }

    @DeleteMapping("/delete/{vendorId}")
    public GlobalReponse deleteById(@PathVariable Integer vendorId) {
        log.info("*** Vendor, resource; delete vendor by id: {} ***", vendorId);

        return this.vendorService.deleteById(vendorId);
    }

    @PostMapping("/deleteAllByIds")
    public GlobalReponse deleteAllByIds(@RequestBody VendorDto vendorDto) {
        log.info("*** Vendor, resource; delete all vendors by ids: {} ***", vendorDto);

        GlobalReponse response ;
        try {
            response = this.vendorService.deleteAllIds(vendorDto);
        }catch (Exception e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException("Cannot delete vendor(s) with id(s): " + Arrays.toString(vendorDto.getIds()));
        }
        return response;
    }

    @PostMapping("/intSave")
    public GlobalReponse intSave(@RequestBody final List<VendorDto> vendorDto) {
        log.info("*** Vendor, resource; save all vendors: {} ***", vendorDto);

        return this.vendorService.intSave(vendorDto);
    }

}
