package com.dbiz.app.tenantservice.service.impl;


import com.dbiz.app.tenantservice.domain.Industry;
import com.dbiz.app.tenantservice.domain.view.IndustryV;
import com.dbiz.app.tenantservice.helper.IndustryMappingHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.IndustryRepository;
import com.dbiz.app.tenantservice.service.IndustryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.IndustryDto;
import org.common.dbiz.dto.tenantDto.IndustryInitDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.IndustryQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class IndustryServiceImpl implements IndustryService {
   private final IndustryRepository industryRepository;

   private final RequestParamsUtils requestParamsUtils;
    @Override
    public GlobalReponsePagination findAll(IndustryQueryRequest request) {
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<Industry> dataresponse =  industryRepository.findAll(pageable);
        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setStatus(HttpStatus.OK.value());
        response.setData(dataresponse.getContent().stream().map(IndustryMappingHelper::map).collect(Collectors.toList()));
        response.setMessage("Industries fetched successfully!");
        response.setErrors("");
        response.setCurrentPage(dataresponse.getNumber());
        response.setTotalPages(dataresponse.getTotalPages());
        response.setPageSize(dataresponse.getSize());
        return response;

    }

    @Override
    public GlobalReponsePagination getAllGroupByType() {
        GlobalReponsePagination response = new GlobalReponsePagination();
        try {
            // 0. Lấy tất cả Industry và chuyển thành DTO
            List<IndustryV> entities = industryRepository.getAllGroupByType();
            List<IndustryInitDto> dtos = entities.stream()
                    .map(IndustryMappingHelper::mapToInitDto)
                    .collect(Collectors.toList());

            // Bước 1: Nhóm theo mã (giữ thứ tự nếu cần)
            Map<String, List<IndustryInitDto>> groupedByCode = dtos.stream()
                    .collect(Collectors.groupingBy(
                            IndustryInitDto::getGroupType,       // nhóm theo mã
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            // Bước 2: Chuyển sang Map dùng tên nhóm làm key
            Map<String, List<IndustryInitDto>> groupedByName = new LinkedHashMap<>();

            for (Map.Entry<String, List<IndustryInitDto>> entry : groupedByCode.entrySet()) {
                String groupTypeCode = entry.getKey();
                List<IndustryInitDto> list = entry.getValue();

                // Lấy tên hiển thị từ phần tử đầu tiên (vì cùng group thì cùng tên)
                String displayName = list.get(0).getGroupTypeName();

                groupedByName.put(displayName, list);
            }

            // 3. Đưa vào response
            response.setStatus(HttpStatus.OK.value());
            response.setData(groupedByName);
            response.setMessage("Industries fetched and grouped successfully!");
            response.setErrors("");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error fetching industries");
            response.setErrors(e.getMessage());
        }
        return response;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(IndustryDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
