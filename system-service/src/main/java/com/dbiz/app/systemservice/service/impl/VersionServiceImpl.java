package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Notification;
import com.dbiz.app.systemservice.domain.Version;
import com.dbiz.app.systemservice.repository.VersionRepository;
import com.dbiz.app.systemservice.service.VersionService;
import com.dbiz.app.systemservice.specification.NotificationSpecification;
import com.dbiz.app.systemservice.specification.VersionSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.dto.systemDto.VersionDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.VersionRequest;
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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {
    private final VersionRepository versionRepository;

    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    @Override
    public GlobalReponsePagination findAll(VersionRequest request) {
        Specification<Version> spec = VersionSpecification.getEntitySpecification(request);
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Page<Version> resultQuery = versionRepository.findAll(spec, pageable);
        List<VersionDto> resultResponse = new ArrayList<>();
        resultQuery.stream().forEach(
                item -> {
                    VersionDto itemDto = modelMapper.map(item, VersionDto.class);

                    resultResponse.add(itemDto);
                }
        );

        return GlobalReponsePagination.builder()
                .data(resultResponse)
                .pageSize(resultQuery.getSize())
                .totalPages(resultQuery.getTotalPages())
                .totalItems(resultQuery.getTotalElements())
                .status(HttpStatus.OK.value())
                .currentPage(resultQuery.getNumber())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
}
