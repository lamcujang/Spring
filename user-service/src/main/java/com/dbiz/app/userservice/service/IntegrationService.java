package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.userDto.CustomerDto;

public interface IntegrationService {
    String saveSingleUser(UserIntDto userIntDto);

    String saveSingleCustomer(CustomerDto paramCustomerDto);

}
