package com.dbiz.app.userservice.resource;

import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.CustomerQueryRequest;
import com.dbiz.app.userservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/customers"})
@Slf4j
@RequiredArgsConstructor
public class CustomerResouce {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public GlobalReponse findById(@PathVariable("customerId") Integer customerId) {
        log.info("*** CustomerDto, resource; fetch customer by id *");

        return this.customerService.findById(customerId);
    }

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute CustomerQueryRequest request) {
        log.info("*** CustomerDto List, resource; fetch all customers *");

        return this.customerService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody CustomerDto customerDto) {
        log.info("*** CustomerDto, resource; save customer *");

        return this.customerService.save(customerDto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody CustomerDto customerDto) {
        log.info("*** CustomerDto, resource; update customer *");

        return this.customerService.save(customerDto);
    }

    @DeleteMapping("/delete/{customerId}")
    public GlobalReponse delete(@PathVariable("customerId") Integer customerId) {
        log.info("*** CustomerDto, resource; delete customer *");

        return this.customerService.deleteById(customerId);
    }

    @PostMapping("/updateDebit")
    public GlobalReponse updateDebit(@RequestBody CustomerDto customerDto) {
        log.info("*** CustomerDto, resource; save Update Debit *");

        return this.customerService.updateCusDebitAmount(customerDto);
    }

    @PostMapping("/deleteAllCustomerByIds")
    public GlobalReponse deleteAllCustomerByIds(@RequestBody CustomerDto ids) {
        log.info("*** CustomerDto, resource; delete all customers by ids *");
        GlobalReponse response;
        try {
            response = this.customerService.deleteAllCustomerByIds(ids);
        } catch (DataIntegrityViolationException e) {
            e.getMessage();
            throw new DataIntegrityViolationException("Cannot delete customer(s) with id(s): ");

        }
        return response;
    }

    @PostMapping("/intSave")
    public GlobalReponse intSave(@RequestBody List<CustomerDto> listInt) {
        log.info("*** CustomerDto, resource; save all customers ***");
        GlobalReponse response = new GlobalReponse();
        response = this .customerService.intSave(listInt);
        return response;
    }

    @GetMapping("/findByPhone1/{phone1}/{fullName}")
    public GlobalReponse getByCustomerPhone(@PathVariable("phone1") String phone1, @PathVariable("fullName") String fullName) {
        log.info("*** CustomerDto, resource; fetch customer by phone1 and fullName *");

        return this.customerService.getByCustomerPhone(phone1, fullName);
    }
    @PostMapping("/intSaveERPNext")
    public GlobalReponse intSaveERPNext(@RequestBody List<CustomerDto> listInt) {
        log.info("*** CustomerDto, resource; save all customers ***");
        GlobalReponse response = new GlobalReponse();
        response = this .customerService.intSaveERPNext(listInt);
        return response;
    }

    //E-menu use: Accept request order
    @GetMapping("/findByPhone2")
    public GlobalReponse getByCustomerPhone2(@RequestParam("phone1") String phone1, @RequestParam("fullName") String fullName) {
        log.info("*** CustomerDto, resource; fetch customer by phone1 and fullName *");

        return this.customerService.getByCustomerPhone(phone1, fullName);
    }

}
