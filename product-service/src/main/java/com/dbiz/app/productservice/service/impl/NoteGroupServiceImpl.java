package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.domain.Note;
import com.dbiz.app.productservice.domain.NoteGroup;
import com.dbiz.app.productservice.repository.NoteGroupRepository;
import com.dbiz.app.productservice.repository.NoteRepository;
import com.dbiz.app.productservice.service.NoteGroupService;
import com.dbiz.app.productservice.specification.NoteGroupSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NoteGroupServiceImpl implements NoteGroupService {
    private final RequestParamsUtils requestParamsUtils;
    private final NoteGroupRepository NoteGroupRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final NoteRepository noteRepository;
    @Override
    public GlobalReponsePagination findAll(NoteGroupQueryRequest paramRequest) {
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<NoteGroup> spec = NoteGroupSpecification.getNoteGroupSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<NoteGroup> NoteGroups = NoteGroupRepository.findAll( spec,pageable);
        List<NoteGroupDto> listData = new ArrayList<>();
        for(NoteGroup item : NoteGroups.getContent()){
           NoteGroupDto dto= modelMapper.map(item,NoteGroupDto.class);
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(NoteGroups.getNumber());
        response.setPageSize(NoteGroups.getSize());
        response.setTotalPages(NoteGroups.getTotalPages());
        response.setTotalItems(NoteGroups.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse     save(NoteGroupDto Dto) {
        log.info("*** warehouse, service; save warehouse ***");
        GlobalReponse response = new GlobalReponse();
        NoteGroup NoteGroupSave = modelMapper.map(Dto,NoteGroup.class);

        if(NoteGroupSave.getId() !=null && NoteGroupSave.getId() > 0) // update
        {
            NoteGroupSave = this.NoteGroupRepository.findById(Dto.getId()).orElseThrow(()->
                    new ObjectNotFoundException(messageSource.getMessage("note_group_notFound",null, LocaleContextHolder.getLocale())));

            modelMapper.map(Dto,NoteGroupSave);
            this.NoteGroupRepository.save(NoteGroupSave);
            response.setMessage(messageSource.getMessage("note_group_update",null, LocaleContextHolder.getLocale()));
        }else
        {
            NoteGroupSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(NoteGroupSave.getOrgId()==null)
                NoteGroupSave.setOrgId(0);
            NoteGroupSave = this.NoteGroupRepository.save(NoteGroupSave);
            response.setMessage(messageSource.getMessage("note_group_create",null, LocaleContextHolder.getLocale()));

        }

        response.setData(modelMapper.map(NoteGroupSave, NoteGroupDto.class));
        response.setStatus(HttpStatus.OK.value());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        Optional<NoteGroup> entityDelete = this.NoteGroupRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("note_group_notFound",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        this.NoteGroupRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("note_group_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.NoteGroupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("note_group_notFound",null, LocaleContextHolder.getLocale()))), NoteGroupDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponsePagination findAllNoteGroupAndNote(NoteGroupQueryRequest request) {
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<NoteGroup> noteGroups = NoteGroupRepository.findAllByGroupId(request.getProductCategoryIds() == null ?"0" : request.getProductCategoryIds(), AuditContext.getAuditInfo().getTenantId(),pageable);
        List<NoteGroup> distinctResults = noteGroups.getContent().stream().distinct().collect(Collectors.toList());

        List<NoteGroupDto> dataReponse = distinctResults.stream().map(
                item->{
                    NoteGroupDto dto = modelMapper.map(item, NoteGroupDto.class);
                    List<Note> noteLine = noteRepository.findAllByNoteGroupId(item.getId());
                    dto.setNotes(noteLine.stream().map(
                            note->modelMapper.map(note, NoteDto.class)
                    ).collect(Collectors.toList()));
                    return dto;
                }
        ).collect(Collectors.toList());

        return GlobalReponsePagination.builder()
                .data(dataReponse)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .currentPage(noteGroups.getNumber())
                .totalItems(noteGroups.getTotalElements())
                .pageSize(noteGroups.getSize())
                .totalPages(noteGroups.getTotalPages())

                .build();
    }
}
