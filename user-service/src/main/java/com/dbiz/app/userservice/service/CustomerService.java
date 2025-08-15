package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.userRequest.CustomerQueryRequest;

import java.util.List;

public interface CustomerService extends BaseServiceGeneric<CustomerDto, Integer, CustomerQueryRequest>{


     GlobalReponse deleteAllCustomerByIds(CustomerDto ids);

     GlobalReponse intSave(List<CustomerDto> listInt);

     GlobalReponse getByCustomerPhone(String phone1,String fullName);

     GlobalReponse updateCusDebitAmount(CustomerDto customerDto);

     GlobalReponse intSaveERPNext(List<CustomerDto> listInt);
}
