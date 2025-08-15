package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.userservice.domain.EmployeeContact;
import com.dbiz.app.userservice.repository.EmployeeContactRepository;
import com.dbiz.app.userservice.service.EmployeeContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeContactDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeContactQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeContactServiceImpl implements EmployeeContactService {

    private final ModelMapper modelMapper;

    private final EmployeeContactRepository employeeContactRepository;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(EmployeeContactQueryRequest request) {
        return null;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(EmployeeContactDto entity) {
        EmployeeContact entitySave = modelMapper.map(entity, EmployeeContact.class);
        this.employeeContactRepository.save(entitySave);
        return GlobalReponse.builder()
                .data(modelMapper.map(entitySave,EmployeeContactDto.class))
                .message("Success").build();
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
