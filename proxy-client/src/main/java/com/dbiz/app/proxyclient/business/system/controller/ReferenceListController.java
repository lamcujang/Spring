package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ReferenceListClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.external.ReferenceListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/referenceList")
@RequiredArgsConstructor
public class ReferenceListController {
    private final ReferenceListClientService clientService;

//    redis
//    private final RedisConnectionFactory redisConnectionFactory;
    @GetMapping("/{id}")
//    @Cacheable(value = "vendors", key = "#vendorId")
    public GlobalReponse findById(@PathVariable(name = "id") Integer id, HttpSession session) {
        return this.clientService.findById(id);
    }

    @GetMapping("/findByReferenceName")
//    @Cacheable(value = "vendors", key = "#vendorId")
    public GlobalReponse findByReferenceName(@RequestParam(value = "nameReference") String nameReference) {
        return this.clientService.findByReferenceName(nameReference);
    }

    @GetMapping("/findAll")
//    @Cacheable(value = "allVendors",key = "'allVendorsKey'")
    public ResponseEntity <GlobalReponsePagination> findAll(@SpringQueryMap ReferenceListQueryRequest  request) {
//        String testSession = (String) session.getAttribute("test_session_id");
//        System.out.println("call find all vendor session: " + testSession +" ID: "+session.getId());
//        session.setAttribute("test_session", page);
//        System.out.println("SessionID: " + session.getId());

        return this.clientService.findALl(request);
    }

    @PostMapping("/save")
//    @Caching(
//            evict = { @CacheEvict(value = "allVendors",key = "'allVendorsKey'", allEntries = true) },
//            put = { @CachePut(value = "vendors", key = "#result.data.id") }
//    )
    public GlobalReponse save(@RequestBody ReferenceListDto  dto) {
        return this.clientService.save(dto);
    }

    @PutMapping("/update")
//    @CachePut(value = "vendors", key = "#vendorDto.id")
//    @CacheEvict(value = "allVendors", allEntries = true)
    public GlobalReponse update(@RequestBody ReferenceListDto dto) {
        return this.clientService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
//    @Caching(
//            evict = {
//                    @CacheEvict(value = "vendors", key = "#vendorId"),
//                    @CacheEvict(value = "allVendors",key = "'allVendorsKey'", allEntries = true)
//            }
//    )
    public GlobalReponse deleteById(@PathVariable(name = "id") Integer id) {
        return this.clientService.delete(id);
    }
}
