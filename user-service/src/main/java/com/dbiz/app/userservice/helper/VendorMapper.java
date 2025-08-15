package com.dbiz.app.userservice.helper;

import com.dbiz.app.userservice.domain.Vendor;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.userDto.VendorDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class VendorMapper {
    private final ModelMapper modelMapper;
//    public Vendor toVendor(final VendorDto vendorDto)
//    {
//        modelMapper.addMappings(new PropertyMap<VendorDto, Vendor>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        return modelMapper.map(vendorDto, Vendor.class);
//    }
//
//
//    public Vendor updateEntity(VendorDto dto, Vendor entity) {
//        modelMapper.addMappings(new PropertyMap<VendorDto, Vendor>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        modelMapper.map(dto, entity);
//        return entity;
//    }
//
//    public VendorDto toVendorDto(final Vendor vendor)
//    {
//        modelMapper.addMappings(new PropertyMap<Vendor, VendorDto>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        return modelMapper.map(vendor, VendorDto.class);
//    }
@PostConstruct
public void init() {
    modelMapper.addMappings(new PropertyMap<VendorDto, Vendor>() {
        @Override
        protected void configure() {
            map(source.getPartnerGroupId(), destination.getPartnerGroupId());
        }
    });

    modelMapper.addMappings(new PropertyMap<Vendor, VendorDto>() {
        @Override
        protected void configure() {
            map(source.getPartnerGroupId(), destination.getPartnerGroupId());
        }
    });
}

    public Vendor toVendor(final VendorDto vendorDto) {
        return modelMapper.map(vendorDto, Vendor.class);
    }

    public Vendor updateEntity(VendorDto dto, Vendor entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public VendorDto toVendorDto(final Vendor vendor) {
        return modelMapper.map(vendor, VendorDto.class);
    }
}
