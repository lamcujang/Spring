package com.dbiz.app.tenantservice.config.filterHeader;


import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditFilter implements Filter {

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Init method if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        log.info("AuditFilter - start");

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Integer dOrgId = Optional.ofNullable(httpRequest.getHeader("orgId")).map(Integer::valueOf).orElse(0);
        Integer dClientId =Optional.ofNullable(httpRequest.getHeader("tenantId")).map(Integer::valueOf).orElse(0);

        String createBy =Optional.ofNullable(httpRequest.getHeader("createBy")).orElse("0");
        String updateBy =Optional.ofNullable(httpRequest.getHeader("updateBy")).orElse("0");
        Integer dUserId = Optional.ofNullable(httpRequest.getHeader("userId")).map(Integer::valueOf).orElse(0);

        String acceptLanguage = httpRequest.getHeader("Accept-Language");
        try {
            if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
                setLocale(acceptLanguage);
            }
        }catch (IllegalArgumentException e){
            log.info("Error caught from extracting headers: ", e);
            e.printStackTrace();
            setLocale("en");
        }

        Integer mainTenantId = dClientId.intValue();
        try{
            int tenantNumbers = getTenantNumbers();
            if(tenantNumbers != dataSourceConfigService.getTenantNumbers()){
                Map<Object, Object> configuredDataSources = dataSourceConfigService
                        .configureDataSources();

                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
            }

//            if(dataSourceConfigService.checkExistTenantRedis(dClientId) == 0){
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
        }catch (Exception e){
            log.info("Error caught from re-configuring data sources: ", e);
            e.printStackTrace();
        }

        if (dClientId != 0) {
            dataSourceContextHolder.setCurrentTenantId(new Long(dClientId.toString()));
        }else{
            dataSourceContextHolder.setCurrentTenantId(null);

        }

        dClientId =0;
//        System.out.println("sessionId: "+httpRequest.getHeader("Cookie"));
        AuditContext.setAuditInfo(new AuditInfo(dClientId, dOrgId,createBy, updateBy,dUserId,acceptLanguage,mainTenantId));

//        // log
//        AuditInfo info = AuditContext.getAuditInfo();
//        if (info == null) {
//            log.info("AuditInfo is not set.");
//        } else {
//            log.info("tenantId={},\n orgId={},\n userId={},\n createBy={},\n updateBy={},\n mainTenantId={}\n", info.getTenantId(), info.getOrgId(), info.getUserId(), info.getCreateBy(), info.getUpdateBy(), info.getMainTenantId());
//        }

        try {
            chain.doFilter(request, response);
        }catch (Exception e) {
            log.info("Error caught from doing filter chain: ", e);
            e.printStackTrace();
        }

//        log.info("AuditFilter - end");

    }

    public int getTenantNumbers(){
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @Override
    public void destroy() {
        // Destroy method if needed
    }


    public void setLocale(String lang) {
        Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
        LocaleContextHolder.setLocale(locale);
    }
}

//////////////////////////////////

//package com.dbiz.app.tenantservice.config.filterHeader;
//
//import com.dbiz.app.tenantservice.domain.AuditContext;
//import com.dbiz.app.tenantservice.domain.AuditInfo;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuditFilter implements Filter {
//
//    private final DataSourceContextHolder dataSourceContextHolder;
//    private final DataSourceConfigService dataSourceConfigService;
//    private final DataSourceRoutingService dataSourceRoutingService;
//
//    @Bean
//    public LocaleResolver localeResolver() {
//        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
//        localeResolver.setDefaultLocale(Locale.ENGLISH);
//        return localeResolver;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        Integer dOrgId = Optional.ofNullable(httpRequest.getHeader("orgId")).map(Integer::valueOf).orElse(0);
//        Integer dClientId = Optional.ofNullable(httpRequest.getHeader("tenantId")).map(Integer::valueOf).orElse(0);
//        String createBy = Optional.ofNullable(httpRequest.getHeader("createBy")).orElse("unknown");
//        String updateBy = Optional.ofNullable(httpRequest.getHeader("updateBy")).orElse("unknown");
//        Integer dUserId = Optional.ofNullable(httpRequest.getHeader("userId")).map(Integer::valueOf).orElse(0);
//        String acceptLanguage = Optional.ofNullable(httpRequest.getHeader("Accept-Language")).orElse("en");
//
//        log.debug("AuditFilter: tenantId={}, orgId={}, userId={}, createBy={}, updateBy={}, language={}",
//                dClientId, dOrgId, dUserId, createBy, updateBy, acceptLanguage);
//
//        if (dClientId == 0 || dOrgId == 0 || dUserId == 0) {
//            log.warn("Missing required audit headers: tenantId={}, orgId={}, userId={}", dClientId, dOrgId, dUserId);
//        }
//
//        try {
//            if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
//                setLocale(acceptLanguage);
//            }
//        } catch (IllegalArgumentException e) {
//            log.error("Invalid Accept-Language header: {}", acceptLanguage, e);
//            setLocale("en");
//        }
//
//        Integer mainTenantId = dClientId;
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService.configureDataSources();
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
//        } catch (Exception e) {
//            log.error("Error updating data sources: {}", e.getMessage(), e);
//        }
//
//        if (dClientId != 0) {
//            dataSourceContextHolder.setCurrentTenantId(Long.valueOf(dClientId));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//        }
//
//        AuditContext.setAuditInfo(AuditInfo.builder()
//                .tenantId(dClientId)
//                .orgId(dOrgId)
//                .createBy(createBy)
//                .updateBy(updateBy)
//                .userId(dUserId)
//                .language(acceptLanguage)
//                .mainTenantId(mainTenantId)
//                .build());
//
//        try {
//            chain.doFilter(request, response);
//        } finally {
//            AuditContext.clear();
//            dataSourceContextHolder.clear(); // Đã thêm phương thức clear()
//        }
//    }
//
//    public int getTenantNumbers() {
//        return dataSourceConfigService.getTenantNumbersRedis();
//    }
//
//    public void setLocale(String lang) {
//        Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
//        LocaleContextHolder.setLocale(locale);
//    }
//}
