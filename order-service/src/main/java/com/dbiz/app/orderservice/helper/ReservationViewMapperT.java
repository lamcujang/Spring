package com.dbiz.app.orderservice.helper;

import com.dbiz.app.orderservice.domain.view.ReservationVAll;
import org.common.dbiz.dto.orderDto.dtoView.ReservationVAllDto;
import org.common.dbiz.helper.DateHelper;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationViewMapperT extends BaseMapper<ReservationVAll, ReservationVAllDto> {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationViewMapperT() {
        super(ReservationVAll.class, ReservationVAllDto.class);
    }

    @PostConstruct
    public void init() {
        Converter<String, Instant> stringToInstantConverter = new AbstractConverter<String, Instant>() {
            @Override
            protected Instant convert(String source) {
                return DateHelper.toInstant(source); // Assuming this method converts String to Instant
            }
        };

        Converter<Instant, String> instantToStringConverter = new AbstractConverter<Instant, String>() {
            @Override
            protected String convert(Instant source) {
                return DateHelper.fromInstant(source); // Assuming this method converts Instant to String
            }
        };

        modelMapper.addMappings(new PropertyMap<ReservationVAllDto, ReservationVAll>() {
            @Override
            protected void configure() {
                map(source.getCustomerName(), destination.getCustomerName());
                map(source.getCustomer().getName(), destination.getCustomer().getName());
                using(stringToInstantConverter).map(source.getReservationTime(), destination.getReservationTime());
            }
        });

        modelMapper.addMappings(new PropertyMap<ReservationVAll, ReservationVAllDto>() {
            @Override
            protected void configure() {
                map(source.getCustomer().getName(), destination.getCustomer().getName());
                map(source.getCustomerName(), destination.getCustomerName());
                using(instantToStringConverter).map(source.getReservationTime(), destination.getReservationTime());
            }
        });
    }

    @Override
    public ReservationVAll convertToEntity(ReservationVAllDto dto, Object... args) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return modelMapper.map(dto, ReservationVAll.class);
    }


    @Override
    public ReservationVAllDto convertToDto(ReservationVAll entity, Object... args) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return modelMapper.map(entity, ReservationVAllDto.class);
    }

    @Override
    public List<ReservationVAllDto > convertToDtoList(Collection<ReservationVAll> entities, Object... args) {
        return super.convertToDtoList(entities, args);
    }
}
