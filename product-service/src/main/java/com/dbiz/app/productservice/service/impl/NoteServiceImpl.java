package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.domain.Note;
import com.dbiz.app.productservice.domain.NoteGroup;
import com.dbiz.app.productservice.domain.ProductCategory;
import com.dbiz.app.productservice.helper.Mapper.NoteMapper;
import com.dbiz.app.productservice.helper.Mapper.ProductCategoryMapper;
import com.dbiz.app.productservice.repository.NoteGroupRepository;
import com.dbiz.app.productservice.repository.NoteRepository;
import com.dbiz.app.productservice.repository.ProductCategoryRepository;
import com.dbiz.app.productservice.service.NoteService;
import com.dbiz.app.productservice.specification.NoteSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteQueryRequest;
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
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final RequestParamsUtils requestParamsUtils;
    private final NoteRepository NoteRepository;
    private final NoteGroupRepository noteGroupRepository;
    private final ModelMapper modelMapper;
    private final NoteMapper noteMapper;
    private final MessageSource messageSource;
    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper categoryMapper;
    @Override
    public GlobalReponsePagination findAll(NoteQueryRequest paramRequest ) {
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<Note> spec = NoteSpecification.getNoteSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Note> Notes = NoteRepository.findAll( spec,pageable);
        List<NoteDto> listData = new ArrayList<>();
        for(Note item : Notes.getContent()){
           NoteDto dto= modelMapper.map(item,NoteDto.class);
            if(dto.getNoteGroupId()!= null)
            {
                NoteGroup group = noteGroupRepository.findById(dto.getNoteGroupId()).orElseThrow(()->new ObjectNotFoundException("Partner Group not found"));
                NoteGroupDto groupDto = modelMapper.map(group, NoteGroupDto.class);
                dto.setNoteGroup(groupDto);
            }
            if(dto.getProductCategoryIds() != null){
                List<ProductCategoryDto> productCategoryDtos = new ArrayList<>();
                if(dto.getProductCategoryIds()!= null && !dto.getProductCategoryIds().isEmpty())
                {
                    String[] productCategoryIdsArray = dto.getProductCategoryIds().split(",");
                    for(String productCategoryId : productCategoryIdsArray){
                        ProductCategory entity = productCategoryRepository.findById(Integer.valueOf(productCategoryId)).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("product.category.not.found",null, LocaleContextHolder.getLocale()),Integer.valueOf(productCategoryId))));
                        ProductCategoryDto productCategoryDto = categoryMapper.toProductCategoryDto(entity);
                        if(entity.getProductCategoryParentId()!= null)
                        {
                            Optional<ProductCategory> parent = productCategoryRepository.findById(entity.getProductCategoryParentId());
                            productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
                        }
                        productCategoryDtos.add(productCategoryDto);
                    }
                }

                dto.setProductCategorys(productCategoryDtos);
            }
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(Notes.getNumber());
        response.setPageSize(Notes.getSize());
        response.setTotalPages(Notes.getTotalPages());
        response.setTotalItems(Notes.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(NoteDto data) {
        NoteDto Dto = (NoteDto) data;
        log.info("*** warehouse, service; save warehouse ***");
        GlobalReponse response = new GlobalReponse();
        Note NoteSave = modelMapper.map(Dto,Note.class);

        if(NoteSave.getId() !=null && NoteSave.getId() > 0) // update
        {
            NoteSave = this.NoteRepository.findById(Dto.getId()).orElseThrow(()->
                    new ObjectNotFoundException(messageSource.getMessage("note_notFound",null, LocaleContextHolder.getLocale())));

            noteMapper.updateEntity(Dto,NoteSave);
            this.NoteRepository.save(NoteSave);
            response.setMessage(messageSource.getMessage("note_update",null, LocaleContextHolder.getLocale()));
        }else
        {
            NoteSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(NoteSave.getOrgId()== null)
                NoteSave.setOrgId(0);
            NoteSave = this.NoteRepository.save(NoteSave);
            response.setMessage(messageSource.getMessage("note_create",null, LocaleContextHolder.getLocale()));

        }

        response.setData(noteMapper.tonoteDto(NoteSave));
        response.setStatus(HttpStatus.OK.value());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        Optional<Note> entityDelete = this.NoteRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("note_notFound",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        this.NoteRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("note_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        response.setData(noteMapper.tonoteDto(this.NoteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("note_notFound",null, LocaleContextHolder.getLocale())))));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }


}
