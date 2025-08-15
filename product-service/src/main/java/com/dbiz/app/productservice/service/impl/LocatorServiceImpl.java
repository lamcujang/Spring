package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Locator;
import com.dbiz.app.productservice.helper.Helper;
import com.dbiz.app.productservice.repository.LocatorRepository;
import com.dbiz.app.productservice.repository.WarehouseRepository;
import com.dbiz.app.productservice.service.LocatorService;
import com.dbiz.app.productservice.specification.LocatorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.LocatorQueryRequest;
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
public class LocatorServiceImpl implements LocatorService {
    private final RequestParamsUtils requestParamsUtils;
    private final LocatorRepository locatorRepository;
    private final ModelMapper modelMapper;
    private final WarehouseRepository warehouseRepository;
    private final MessageSource messageSource;
    private final Helper helper;

    @Override
    public GlobalReponsePagination findAll(LocatorQueryRequest locatorQueryRequest) {
        log.info("*** Locator List, service; fetch all locator *");

        Pageable pageable = requestParamsUtils.getPageRequest(locatorQueryRequest);
        Specification<Locator> spec = LocatorSpecification.getLocatorSpecification(locatorQueryRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Locator> locators = locatorRepository.findAll(spec, pageable);
        List<LocatorDto> listData = new ArrayList<>();
        for (Locator item : locators.getContent()) {
            listData.add(modelMapper.map(item, LocatorDto.class));
        }
        response.setMessage(messageSource.getMessage("locator.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(locators.getNumber());
        response.setPageSize(locators.getSize());
        response.setTotalPages(locators.getTotalPages());
        response.setTotalItems(locators.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(LocatorDto locatorDto) {

        log.info("*** locatorDto, service; save locator ***");
        GlobalReponse response = new GlobalReponse();
        Locator locatorSave = modelMapper.map(locatorDto, Locator.class);

        if (locatorDto.getId() != null) // update
        {
            locatorSave = this.locatorRepository.findById(locatorDto.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                    String.format(messageSource.getMessage("locator.error.notfound", null, LocaleContextHolder.getLocale()), locatorDto.getId())
                      ));
//            if (locatorDto.getWarehouse() != null && locatorDto.getWarehouse().getId() != null) {
//                Warehouse warehouse = warehouseRepository.findById(locatorDto.getWarehouse().getId()).orElseThrow(() -> new ProductNotFoundException(
//                        String.format(messageSource.getMessage("warehouse.not.found", null, LocaleContextHolder.getLocale()), locatorDto.getWarehouse().getId())
//                ));
//
//                locatorSave.setWarehouse(warehouse);
//            }
            if (locatorDto.getOrgId() != null) {
                locatorSave.setOrgId(locatorDto.getOrgId());

            }
            response.setMessage(messageSource.getMessage("locator.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            modelMapper.map(locatorDto, locatorSave);
            this.locatorRepository.save(locatorSave);
        } else {
            if(locatorSave.getCode()==null)
            {
                locatorSave.setCode("LOC"+helper.getSequence("d_locator_sq"));
            }
            locatorSave = this.locatorRepository.save(locatorSave);
            response.setMessage(messageSource.getMessage("locator.created", null, LocaleContextHolder.getLocale() ));
            response.setStatus(HttpStatus.CREATED.value());
        }

        response.setData(modelMapper.map(locatorSave, LocatorDto.class));

        log.info("Locator saved successfully with ID: {}", locatorSave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete locator by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<Locator> locatorDelete = this.locatorRepository.findById(id);
        if (locatorDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("locator.not.found", null, LocaleContextHolder.getLocale()), id));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.locatorRepository.delete(locatorDelete.get());
        response.setMessage(String.format(messageSource.getMessage("locator.deleted", null, LocaleContextHolder.getLocale()), id));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** LocatorId, service; fetch locator by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.locatorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("locator.not.found",null,LocaleContextHolder.getLocale()), id))), LocatorDto.class));
        response.setMessage("LocatorDto fetched successfully");
        return response;
    }
}
