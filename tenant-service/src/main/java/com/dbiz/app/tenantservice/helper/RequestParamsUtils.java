package com.dbiz.app.tenantservice.helper;

import org.common.dbiz.request.BaseQueryRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class RequestParamsUtils {
    public Sort getSort(String sortBy, String order){
        return Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
    }

    public PageRequest getPageRequest(BaseQueryRequest baseQueryRequest){
        return PageRequest.of(baseQueryRequest.getPage(), baseQueryRequest.getPageSize(), getSort(baseQueryRequest.getSortBy(), baseQueryRequest.getOrder()));
    }
}
