package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.EmployeeContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/employeeContacts"})
@Slf4j
@RequiredArgsConstructor
public class EmployeeContactResource {
    private final EmployeeContactService employeeContactService;
}
