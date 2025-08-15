package com.dbiz.app.paymentservice.helper;


import com.dbiz.app.paymentservice.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.dto.orderDto.response.KitchenOrderDtoV;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<BankAccountDto, BankAccount>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getBankId(),destination.getBankId());

            }
        });

        modelMapper.addMappings(new PropertyMap<BankAccount,BankAccountDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getBankId(),destination.getBankId());
            }
        });
    }

    public BankAccount toEntity(BankAccountDto dto) {
        return modelMapper.map(dto, BankAccount.class);
    }

    public BankAccountDto toDto(BankAccount entity) {
        return modelMapper.map(entity, BankAccountDto.class);
    }

    public BankAccount updateEntity(BankAccountDto dto, BankAccount entity) {
        modelMapper.map(dto, entity);
        return entity;
    }


}
