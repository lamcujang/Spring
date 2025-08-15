package com.dbiz.app.systemservice.resource;




import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import com.dbiz.app.systemservice.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/reference"})
@Slf4j
@RequiredArgsConstructor
public class RefereneResource {
    private final ReferenceService service;

    @GetMapping("/{id}")
        public GlobalReponse findById(@PathVariable Integer id) {
//        String testSession = (String) session.getAttribute("test_session");
//        System.out.println("call vendor resource session: " + testSession +" ID: "+session.getId());
//        log.info("*** Vendor, resource; fetch vendor by id: {} ***", vendorId);
//        session.setAttribute("resource","Session resource");
        return this.service.findById(id);
    }
    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute ReferenceQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");

        return this.service.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ReferenceDto dto) {
        log.info("*** Vendor, resource; save vendor: {} ***", dto);

        return this.service.save(dto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody ReferenceDto dto) {
        log.info("*** Vendor, resource; update vendor: {} ***", dto);

        return this.service.save(dto);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalReponse deleteById(@PathVariable Integer id) {
        log.info("*** Vendor, resource; delete vendor by id: {} ***", id);

        return this.service.deleteById(id);
    }

}
