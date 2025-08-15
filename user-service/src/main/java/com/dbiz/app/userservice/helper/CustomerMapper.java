package com.dbiz.app.userservice.helper;

import com.dbiz.app.userservice.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final ModelMapper modelMapper;

//    public Customer toCustomer(CustomerDto customerDto){
//        modelMapper.addMappings(new PropertyMap<CustomerDto, Customer>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        return modelMapper.map(customerDto, Customer.class);
//    }
//
//    public  CustomerDto toCustomerDto(Customer customer){
//
//        modelMapper.addMappings(new PropertyMap<Customer, CustomerDto>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        return modelMapper.map(customer, CustomerDto.class);
//    }
//
//    public Customer updateEntity(CustomerDto dto, Customer entity){
//        modelMapper.addMappings(new PropertyMap<CustomerDto, Customer>() {
//            @Override
//            protected void configure() {
//                map(source.getPartnerGroupId(), destination.getPartnerGroupId());
//            }
//        });
//        modelMapper.map(dto, entity);
//        return entity;
//    }

        @PostConstruct
        public void init() {
            modelMapper.addMappings(new PropertyMap<CustomerDto, Customer>() {
                @Override
                protected void configure() {
                    map(source.getPartnerGroupId(), destination.getPartnerGroupId());
                }
            });

            modelMapper.addMappings(new PropertyMap<Customer, CustomerDto>() {
                @Override
                protected void configure() {
                    map(source.getPartnerGroupId(), destination.getPartnerGroupId());
                }
            });
        }

    public Customer toCustomer(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    public CustomerDto toCustomerDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    public Customer updateEntity(CustomerDto dto, Customer entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

}
