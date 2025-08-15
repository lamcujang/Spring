package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.Tenant;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Integer> {

	Page<Tenant> findAll(Pageable pageable);


	Optional<Tenant> findByDomainUrl(@NotNull final String domainUrl);
//	@Query(nativeQuery = true, value = "SELECT * FROM vReport1_1 ORDER BY DATE_CREATED, AMOUNT")
//	List<R11Dto> getR11();

	@Query("select new org.common.dbiz.dto.tenantDto.TenantAndOrgDto(p.code,i.code)  from Tenant p , Industry  i  where p.id = :tenantId   and p.industryId = i.id")
	TenantAndOrgDto findByCodeTenantAndCodeOrg(Integer tenantId);


	Optional<Tenant> findByName(String name);

	@Modifying
	@Query(value = "INSERT INTO pos.d_tenant (d_tenant_id, name,  code,domain_url,d_industry_id,tax_code,is_active,created_by,updated_by) " +
			" VALUES (:id,:name, :code, :domainUrl, :industryId, :taxCode, :isActive,0,0)" , nativeQuery = true)
	void insertWithId(@Param("id") Integer id,
					  @Param("name") String name,
					  @Param("code") String code,
					  @Param("domainUrl") String domainUrl,
					  @Param("industryId") Integer industryId,
					  @Param("taxCode") String taxCode,
					  @Param("isActive") String isActive);

}
