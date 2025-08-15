package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.userservice.domain.Timesheet;
import com.dbiz.app.userservice.repository.TimesheetRepository;
import com.dbiz.app.userservice.service.TimeSheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.TimesheetDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.TimeSheetQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeSheetServiceImpl implements TimeSheetService {

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final TimesheetRepository timesheetRepository;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(TimeSheetQueryRequest request) {
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
    public GlobalReponse save(TimesheetDto entity) {

        Timesheet timeSheet = modelMapper.map(entity, Timesheet.class);
        Timesheet timeSheetSave;
        if(entity.getId()!= null) {
            Timesheet timesheetCheck = this.timesheetRepository.findById( entity.getId()).orElseThrow(()->new RuntimeException("Timesheet not found"));
            modelMapper.map(entity, timesheetCheck);
            if(entity.getOrgDto()!= null)
                timesheetCheck.setOrgId(entity.getOrgDto().getId());
            if(entity.getEmployeeDto()!=null)
                timesheetCheck.setEmployeeId(entity.getEmployeeDto().getId());
            if(entity.getApprovedByDto()!=null)
                timesheetCheck.setApprovedBy(entity.getApprovedByDto().getId());
            timeSheetSave = this.timesheetRepository.save(timesheetCheck);
        }else{
            if(entity.getOrgDto()!= null)
                timeSheet.setOrgId(entity.getOrgDto().getId());
            if(entity.getEmployeeDto()!=null)
                timeSheet.setEmployeeId(entity.getEmployeeDto().getId());
            if(entity.getApprovedByDto()!=null)
                timeSheet.setApprovedBy(entity.getApprovedByDto().getId());
            timeSheetSave = this.timesheetRepository.save(timeSheet);
        }
        TimesheetDto entityDtoResponse = modelMapper.map(timeSheetSave, TimesheetDto.class);
        if(entity.getOrgDto()!= null)
            entityDtoResponse.setOrgDto(entity.getOrgDto());
        if(entity.getEmployeeDto()!=null)
            entityDtoResponse.setEmployeeDto(entity.getEmployeeDto());
        if(entity.getApprovedByDto()!=null)
            entityDtoResponse.setApprovedByDto(entity.getApprovedByDto());
        return GlobalReponse.builder()
                .data(entityDtoResponse)
                .message("save Succcess")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
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
