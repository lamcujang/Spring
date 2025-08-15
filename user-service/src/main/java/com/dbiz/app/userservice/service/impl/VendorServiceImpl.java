package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.Image;
import com.dbiz.app.userservice.domain.PartnerGroup;
import com.dbiz.app.userservice.domain.Vendor;

import com.dbiz.app.userservice.helper.ImageHelper;
import com.dbiz.app.userservice.helper.ImageMapper;
import com.dbiz.app.userservice.helper.VendorMapper;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.exception.wrapper.VendorObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.VendorQueryRequest;
import com.dbiz.app.userservice.repository.ImageRepository;
import com.dbiz.app.userservice.repository.PartnerGroupRepository;
import com.dbiz.app.userservice.repository.VendorRepository;
import com.dbiz.app.userservice.service.VendorService;
import com.dbiz.app.userservice.specification.VendorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private  final VendorRepository vendorRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final VendorMapper vendorMapper;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    private final MessageSource messageSource;

    private final RestTemplate restTemplate;

    private final PartnerGroupRepository partnerGroupRepository;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    private final ImageHelper imageHelper;

    @Override
    public GlobalReponsePagination findAll(VendorQueryRequest request) {

        log.info("*** Vendor List, service; fetch all vendors ***");
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Vendor> spec = VendorSpecification.getCustomerSpecification(request);

        // lay ra id giao dich  va them dieu kien vao where
        if(request.getTransactionAmountFrom()!= null && request.getTransactionAmountTo()!= null)
        {
            List<Integer> vendors = vendorRepository.findByTransactionAmountBetween( request.getTransactionAmountFrom(),request.getTransactionAmountTo(),
                    AuditContext.getAuditInfo().getTenantId());
            if(!vendors.isEmpty())
            {
                Specification<Vendor> vendorIdSpec = (root, query, criteriaBuilder) ->
                        root.get("id").in(vendors);
                spec = spec.and(vendorIdSpec);
            }
        }

        Page<Vendor> vendors = vendorRepository.findAll( spec,pageable);
        List<VendorDto> listData = new ArrayList<>();
        for(Vendor vendor : vendors.getContent()){
            BigDecimal transactionAmout = vendorRepository.getTotalTransactionAmountByVendorId(vendor.getId());
            PartnerGroupDto groupDto = null;
            if(vendor.getPartnerGroupId()!= null)
            {
                PartnerGroup group = partnerGroupRepository.findById(vendor.getPartnerGroupId()).orElseThrow(()->new ObjectNotFoundException("Partner Group not found"));
                groupDto = modelMapper.map(group, PartnerGroupDto.class);
            }


            VendorDto vendorDto = vendorMapper.toVendorDto(vendor);
            vendorDto.setPartnerGroup(groupDto);
            vendorDto.setTotalTransactionAmount(transactionAmout);
            listData.add(vendorDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage(messageSource.getMessage("vendor_fechAll", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(vendors.getTotalPages());
        globalReponsePagination.setPageSize(vendors.getSize());
        globalReponsePagination.setCurrentPage(vendors.getNumber());
        globalReponsePagination.setTotalItems(vendors.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse save(VendorDto vendorDto) {
        log.info("Save VendorDto: {}", vendorDto);

        GlobalReponse response = new GlobalReponse();
        Vendor vendorSave = null;
        PartnerGroupDto groupDto = null;
//       try {
        if(vendorDto.getId() != null)
        {// update
            vendorSave = this.vendorRepository.findById(vendorDto.getId()).orElseThrow(()->new VendorObjectNotFoundException("Vendor not found"));
            if(vendorDto.getImage()!= null)
            {
                Image updateImage = imageHelper.saveImage(vendorDto.getImage());
                vendorSave.setImage(updateImage);

            }
            vendorSave = vendorMapper.updateEntity(vendorDto,vendorSave);
            this.vendorRepository.save(vendorSave);
            if(vendorSave.getPartnerGroupId()!=null)
            {
                groupDto = partnerGroupRepository.findById(vendorSave.getPartnerGroupId()).map(group -> modelMapper.map(group, PartnerGroupDto.class)).orElse(null);
            }
            response.setMessage(messageSource.getMessage("vendor_update",null, LocaleContextHolder.getLocale()));

        }
        else {
            if(vendorDto.getCode() == null)
            {
                String docNo = "VEN" + (vendorRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                vendorDto.setCode(docNo);
            }
            else{
                if(!vendorRepository.findAllByCodeAndTenantId(vendorDto.getCode(), AuditContext.getAuditInfo().getTenantId()).isEmpty())
                {
                    throw new PosException(messageSource.getMessage("vendor.code.exist",null, LocaleContextHolder.getLocale()));
                }
            }
            List<Vendor> vendorList = vendorRepository.findByPhone1(vendorDto.getPhone1());
            if(!vendorList.isEmpty())
                throw  new PosException(messageSource.getMessage("vendor.phone.exist",null, LocaleContextHolder.getLocale()));
            Vendor entity = modelMapper.map(vendorDto, Vendor.class);
            entity.setTenantId(com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId());
            vendorSave = this.vendorRepository.save(entity);

            if (vendorDto.getImage() != null)
            {
                Image updateImage = imageHelper.saveImage(vendorDto.getImage());
                vendorSave.setImage(updateImage);
            }

            if(vendorSave.getPartnerGroupId() != null)
                groupDto = partnerGroupRepository.findById(vendorSave.getPartnerGroupId()).map(group -> modelMapper.map(group, PartnerGroupDto.class)).orElse(null);
            response.setMessage(messageSource.getMessage("vendor_create",null, LocaleContextHolder.getLocale()));
        }
        vendorDto = vendorMapper.toVendorDto(vendorSave);
        vendorDto.setPartnerGroup(groupDto);
        vendorDto.setTotalTransactionAmount(vendorRepository.getTotalTransactionAmountByVendorId(vendorSave.getId()));
        response.setData(vendorDto);
//       }catch (Exception e){
//           e.printStackTrace();
//       }
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer vendorId) {
        log.info("Delete Vendor by id: {}", vendorId);
        GlobalReponse response = new GlobalReponse();

        Vendor vendor = this.vendorRepository.findById(vendorId).orElseThrow(()->new RuntimeException(messageSource.getMessage("vendor_notFound",null, LocaleContextHolder.getLocale())));
        this.vendorRepository.delete(vendor);
        response.setMessage(messageSource.getMessage("vendor_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer vendorId) {
        log.info("Find Vendor by id: {}", vendorId);
        GlobalReponse reponse = new GlobalReponse();
        Vendor vendor = this.vendorRepository.findById(vendorId).orElseThrow(()->new RuntimeException(messageSource.getMessage("vendor_notFound",null, LocaleContextHolder.getLocale())));
        VendorDto dto = vendorMapper.toVendorDto(vendor);
        dto.setTotalTransactionAmount(vendorRepository.getTotalTransactionAmountByVendorId(vendorId));
        reponse.setData(dto);
        reponse.setMessage(messageSource.getMessage("vendor_fetch_success", null, LocaleContextHolder.getLocale()));
        return reponse;
    }

    @Override
    public GlobalReponse deleteAllIds(VendorDto vendorDto) {
        GlobalReponse response = new GlobalReponse();
        for(Integer item : vendorDto.getIds())
        {
            vendorRepository.deleteById(item);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setData("");
        response.setMessage(messageSource.getMessage("vendor_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse intSave(List<VendorDto> vendorDto) {
        StringBuilder message = new StringBuilder();
        this.saveListIdempiere(vendorDto);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(message.toString())
                .data(vendorDto)
                .errors("")
                .build();
    }

    @Transactional
    public void saveListIdempiere(List<VendorDto>vendorDto )
    {
        vendorDto.forEach(item -> {

            PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
            Vendor vendorCheck = vendorRepository.findByErpVendorId(item.getErpVendorId());
            if (vendorCheck == null) {
//                if(vendorRepository.findByPhone1(item.getPhone1()).isPresent())
//                {
//                    message.append("Phone1: ").append(item.getPhone1()).append(" already exists").append("\n");
//                    return;
//                }
                Vendor vendor = vendorMapper.toVendor(item);
                vendor.setPartnerGroupId(group.getId());
                vendor.setTenantId(AuditContext.getAuditInfo().getTenantId());
                vendorRepository.save(vendor);
                item = vendorMapper.toVendorDto(vendor);
            } else {
//                if(vendorRepository.findByPhone1(item.getPhone1()).isPresent())
//                {
//                    message.append("Phone1: ").append(item.getPhone1()).append(" already exists").append("\n");
//                    return;
//                }
                vendorMapper.updateEntity(item, vendorCheck);
                vendorCheck.setPartnerGroupId(group.getId());
                vendorRepository.save(vendorCheck);
            }
        });
    }
//    @Override
//    public GlobalReponse intSave(List<VendorDto> vendorDto) {
//        StringBuilder errors = new StringBuilder();
//
//        vendorDto.forEach(item -> {
//            try {
//                GlobalReponse result = saveVendor(item); // Tách logic lưu vendor ra ngoài
//                if (result.getErrors() != null && !result.getErrors().isEmpty()) {
//                    errors.append(result.getErrors()).append("\n");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                errors.append(e.getMessage()).append("\n");
//            }
//        });
//
//        return GlobalReponse.builder()
//                .status(HttpStatus.OK.value())
//                .message(errors.toString().isEmpty() ? "Vendor saved successfully" : errors.toString())
//                .data(vendorDto)
//                .errors(errors.toString())
//                .build();
//    }
//    @Transactional
//    public GlobalReponse saveVendor(VendorDto item) {
//        StringBuilder errors = new StringBuilder();
//
//        try {
//            PartnerGroup group = partnerGroupRepository.findByErpBpGroupId(item.getPartnerGroupId());
//            Vendor vendorCheck = vendorRepository.findByErpVendorId(item.getErpVendorId());
//
//            if (vendorCheck == null) {
//                if (vendorRepository.findByPhone1(item.getPhone1()).isPresent())
//                    errors.append("Phone1: ").append(item.getPhone1()).append(" already exists");
//
//                Vendor vendor = vendorMapper.toVendor(item);
//                vendor.setPartnerGroupId(group.getId());
//                vendor.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                vendorRepository.save(vendor);
//            } else {
//                vendorMapper.updateEntity(item, vendorCheck);
//                vendorCheck.setPartnerGroupId(group.getId());
//                vendorRepository.save(vendorCheck);
//            }
//            vendorRepository.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//            errors.append(e.getMessage());
//        }
//
//        return GlobalReponse.builder()
//                .errors(errors.toString())
//                .build();
//    }
}
