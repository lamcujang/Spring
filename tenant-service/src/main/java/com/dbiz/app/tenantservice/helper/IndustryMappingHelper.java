package com.dbiz.app.tenantservice.helper;

import com.dbiz.app.tenantservice.domain.Industry;
import com.dbiz.app.tenantservice.domain.view.IndustryV;
import org.common.dbiz.dto.tenantDto.IndustryDto;
import org.common.dbiz.dto.tenantDto.IndustryInitDto;

public interface IndustryMappingHelper {

    public static IndustryDto map(final Industry industry) {
        return IndustryDto.builder()
                .id(industry.getId())
                .code(industry.getCode())
                .name(industry.getName())
                .build();
    }

    public static IndustryInitDto mapToInitDto(final IndustryV industry) {
        return IndustryInitDto.builder()
                .id(industry.getId())
                .code(industry.getCode())
                .name(industry.getName())
                .businesModal(industry.getBusinessModel())
                .groupType(industry.getGroupTypeCode())
                .groupTypeName(industry.getGroupTypeName())
                .isActive(industry.getIsActive())
                .build();
    }

    public static Industry map(final IndustryDto industryDto) {
        return Industry.builder()
                .id(industryDto.getId())
                .code(industryDto.getCode())
                .name(industryDto.getName())
                .build();
    }
}
