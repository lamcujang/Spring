package com.dbiz.app.orderservice.helper;


import com.dbiz.app.orderservice.domain.view.RequestOrderGetAllV;
import org.common.dbiz.dto.orderDto.dtoView.RequestOrderGetAllVDto;
import org.common.dbiz.dto.orderDto.dtoView.RequestOrderLineGetAllVDto;
import org.common.dbiz.helper.DateHelper;
import org.hibernate.collection.internal.PersistentBag;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class RequestOrderViewMapper extends BaseMapper<RequestOrderGetAllV, RequestOrderGetAllVDto> {

    @Autowired
    private ModelMapper modelMapper;

    public RequestOrderViewMapper() {
        super(RequestOrderGetAllV.class, RequestOrderGetAllVDto.class);
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

        modelMapper.addMappings(new PropertyMap<RequestOrderGetAllVDto, RequestOrderGetAllV>() {
            @Override
            protected void configure() {
                using(stringToInstantConverter).map(source.getOrderTime(), destination.getOrderTime());
            }
        });

        modelMapper.addMappings(new PropertyMap<RequestOrderGetAllV, RequestOrderGetAllVDto>() {
            @Override
            protected void configure() {
                using(instantToStringConverter).map(source.getOrderTime(), destination.getOrderTime());
            }
        });
        Converter<PersistentBag, List> persistentBagToListConverter = new Converter<PersistentBag, List>() {
            @Override
            public List convert(MappingContext<PersistentBag, List> context) {
                return new ArrayList<>(context.getSource());
            }
        };
        modelMapper.addConverter(persistentBagToListConverter);
    }

    @Override
    public RequestOrderGetAllV convertToEntity(RequestOrderGetAllVDto dto, Object... args) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return modelMapper.map(dto, RequestOrderGetAllV.class);
    }


    @Override
    public RequestOrderGetAllVDto convertToDto(RequestOrderGetAllV entity, Object... args) {
        if (Objects.isNull(entity)) {
            return null;
        }
        List<RequestOrderLineGetAllVDto> requestOrderLinesDto = new ArrayList<>();
        if (entity.getRequestOrderLines() != null) {
            entity.getRequestOrderLines().forEach(line -> {
                RequestOrderLineGetAllVDto lineDto = modelMapper.map(line, RequestOrderLineGetAllVDto.class);
                requestOrderLinesDto.add(lineDto);
            });
        }

        // Sau đó ánh xạ phần còn lại
        RequestOrderGetAllVDto dto = modelMapper.map(entity, RequestOrderGetAllVDto.class);
        dto.setRequestOrderLines(requestOrderLinesDto);

        return dto;
    }

    @Override
    public List<RequestOrderGetAllVDto> convertToDtoList(Collection<RequestOrderGetAllV> entities, Object... args) {
        return super.convertToDtoList(entities, args);
    }
}
