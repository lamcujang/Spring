package com.dbiz.app.systemservice.resource;

//import com.dbiz.app.config.audit.AopAudit;


import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import com.dbiz.app.systemservice.service.ReferenceListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ReferenceListDto;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/referenceList"})
@Slf4j
@RequiredArgsConstructor
public class RefereneListResource {
    private final ReferenceListService service;

    @GetMapping("/{id}")
        public GlobalReponse findById(@PathVariable Integer id) {
//        String testSession = (String) session.getAttribute("test_session");
//        System.out.println("call Reference resource session: " + testSession +" ID: "+session.getId());
//        log.info("*** Reference, resource; fetch Reference by id: {} ***", ReferenceId);
//        session.setAttribute("resource","Session resource");
        return this.service.findById(id);
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute ReferenceListQueryRequest request) {
        log.info("*** Reference List, resource; fetch all Reference *");

        return new ResponseEntity<>(this.service.findAll(request), HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ReferenceListDto dto) {
        log.info("*** Reference, resource; save Reference: {} ***", dto);

        return this.service.save(dto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody ReferenceListDto dto) {
        log.info("*** Reference, resource; update Reference: {} ***", dto);
        return this.service.save(dto);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalReponse deleteById(@PathVariable Integer id) {
        log.info("*** Reference, resource; delete Reference by id: {} ***", id);

        return this.service.deleteById(id);
    }

    @GetMapping("/findByValue/{value}")
    public GlobalReponse findByValue(@PathVariable(value = "value") String value) {
        log.info("*** Reference, resource; fetch Reference by value: {} ***", value);

        return this.service.findByValue(value);
    }

    @GetMapping("/findByRefValue")
    public GlobalReponse findByRefValue (@RequestParam(value = "value") String value, @RequestParam(value = "domain") String domainName, @RequestParam(value = "column")String column)
    {
        log.info("*** Reference, resource; fetch Reference by value: {} ***", value);

//        return this.service.findByRefValue(value, domainName);
        return this.service.findRefValue(value, domainName, column);
    }

    @GetMapping("/findByReferenceNameAndValue")
    public GlobalReponse findByReferenceNameAndValue(@RequestParam(value = "nameReference") String nameReference, @RequestParam(value = "value") String value) {
        log.info("*** Reference, resource; fetch Reference by nameReference: {} and value: {} ***", nameReference, value);

        return this.service.findByReferenceNameAndValue(nameReference, value);
    }

    @GetMapping("/findByReferenceName")
    public GlobalReponse findByReferenceName(@RequestParam(value = "nameReference") String nameReference) {
        log.info("*** Reference, resource; fetch Reference by nameReference: {}  ***", nameReference);
        return this.service.findByReferenceName(nameReference);
    }
}
