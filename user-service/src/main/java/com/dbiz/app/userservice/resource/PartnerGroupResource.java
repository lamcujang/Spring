package com.dbiz.app.userservice.resource;

import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DeletePartnerGroupByIds;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import com.dbiz.app.userservice.service.PartnerGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/partnerGroups"})
@Slf4j
@RequiredArgsConstructor
public class PartnerGroupResource {
    private final PartnerGroupService partnerGroupService;
    private final MessageSource messageSource;
    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute PartnerGroupQuery request) {
        log.info("*** PartnerGroup List, resource; fetch all vendor *");

        return this.partnerGroupService.findAll(request);
    }
    @PostMapping("/save")
    public GlobalReponse save(@RequestBody PartnerGroupDto dto) {
        log.info("*** PartnerGroup, resource; save vendor *");

        return this.partnerGroupService.save(dto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody PartnerGroupDto dto) {
        log.info("*** PartnerGroup, resource; update vendor *");

        return this.partnerGroupService.save(dto);
    }

    @GetMapping("/{id}")
    public GlobalReponse findById(@PathVariable final Integer id) {
        log.info("*** PartnerGroup, resource; fetch vendor by id: " + id + " ***");

        return this.partnerGroupService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalReponse deleteById(@PathVariable final Integer id) {
        log.info("*** PartnerGroup, resource; delete vendor by id: " + id + " ***");
        GlobalReponse globalReponse = new GlobalReponse();

            globalReponse =  this.partnerGroupService.deleteById(id);

        return globalReponse;
    }

    @PostMapping("/removeChildPartner")
    public GlobalReponse removeChildPartner(@RequestBody final PartnerGroupDto request)
    {
        log.info("*** PartnerGroup, resource; remove child partner ***");
        return this.partnerGroupService.removeChildPartner(request);
    }

    @PostMapping("/deleteAllByIds")
    public GlobalReponse deleteAllByIds(@RequestBody final DeletePartnerGroupByIds request) {
        log.info("*** PartnerGroup, resource; delete all vendors by ids ***");

        return this.partnerGroupService.deleteAllByIds(request);
    }

    @PostMapping("/intSave")
    public GlobalReponse intSave(@RequestBody final List<PartnerGroupDto> listInt)
    {
        log.info("*** PartnerGroup, resource; save all vendors ***");

        return this.partnerGroupService.intSave(listInt);
    }

    @PostMapping("/intSaveERPNext")
    public GlobalReponse intSaveERPNext(@RequestBody final List<PartnerGroupDto> listInt)
    {
        log.info("*** PartnerGroup, resource; save all vendors ***");

        return this.partnerGroupService.intSaveERPNext(listInt);
    }
}
