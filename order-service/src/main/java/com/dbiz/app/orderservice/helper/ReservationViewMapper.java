package com.dbiz.app.orderservice.helper;//package com.selimhorri.app.helper;
//
//import com.selimhorri.app.domain.ReservationOrder;
//import com.selimhorri.app.domain.view.ReservationVAll;
//import com.selimhorri.app.dto.ReservationOrderDto;
//import com.selimhorri.app.dto.dtoView.ReservationVAllDto;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class ReservationViewMapper {
//
//    private final ModelMapper modelMapper;
//
//    @PostConstruct
//    public void init() {
//        modelMapper.addMappings(new PropertyMap<ReservationVAllDto, ReservationVAll>() {
//            @Override
//            protected void configure() {
//                map(source.getCustomerName(), destination.getCustomerName());
//                map(source.getCustomer().getName(), destination.getCustomer().getName());
//
//            }
//        });
//
//        modelMapper.addMappings(new PropertyMap<ReservationVAll, ReservationVAllDto>() {
//            @Override
//            protected void configure() {
//                map(source.getCustomer().getName(), destination.getCustomer().getName());
//                map(source.getCustomerName(), destination.getCustomerName());
//            }
//        });
//    }
//    public ReservationVAll reservationOrder(ReservationVAllDto dto) {
//        return modelMapper.map(dto, ReservationVAll.class);
//    }
//
//    public ReservationVAllDto reservationOrderDto(ReservationVAll entity) {
//        return modelMapper.map(entity, ReservationVAllDto.class);
//    }
//}
