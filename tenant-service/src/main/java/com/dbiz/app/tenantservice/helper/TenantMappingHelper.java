package com.dbiz.app.tenantservice.helper;

import com.dbiz.app.tenantservice.domain.Tenant;
import org.common.dbiz.dto.tenantDto.TenantDto;


public interface TenantMappingHelper {
    public static TenantDto map(final Tenant tenant) {
        return TenantDto.builder()
                .code(tenant.getCode())
                .name(tenant.getName())
                .domainUrl(tenant.getDomainUrl())
                .taxCode(tenant.getTaxCode())
                .imageId(tenant.getImageId())
                .id(tenant.getId())
                .industryId(tenant.getIndustryId())
                .industryCode(tenant.getIndustryCode())
                .notificationKitchen(tenant.getNotificationKitchen())
                .shiftMgmt(tenant.getShiftMgmt())
                .inventoryAlter(tenant.getInventoryAlter())
                .productionMgmt(tenant.getProductionMgmt())
                .billMergeItem(tenant.getBillMergeItem())
                .numberOfPayments(tenant.getNumberOfPayments())
                .dishRecallTime(tenant.getDishRecallTime())
                .expiredDate(tenant.getExpiredDate())
                .agentCode(tenant.getAgentCode())
                .build();
    }

    public static Tenant map(final TenantDto tenantDto){
        return Tenant.builder()
                .code(tenantDto.getCode())
                .name(tenantDto.getName())
                .domainUrl(tenantDto.getDomainUrl())
                .taxCode(tenantDto.getTaxCode())
                .imageId(tenantDto.getImageId())
                .id(tenantDto.getId())
                .industryId(tenantDto.getIndustryId())
                .build();
    }

    public static void updateFromDto(TenantDto tenantDto, Tenant tenant) {
        if (tenantDto.getCode() != null) {
            tenant.setCode(tenantDto.getCode());
        }
        if (tenantDto.getName() != null) {
            tenant.setName(tenantDto.getName());
        }
        if (tenantDto.getDomainUrl() != null) {
            tenant.setDomainUrl(tenantDto.getDomainUrl());
        }
        if (tenantDto.getTaxCode() != null) {
            tenant.setTaxCode(tenantDto.getTaxCode());
        }
        if (tenantDto.getImageId() != null) {
            tenant.setImageId(tenantDto.getImageId());
        }
    }
}
