package com.dbiz.app.systemservice.specification;

import com.dbiz.app.systemservice.domain.Reference;
import com.dbiz.app.systemservice.domain.Version;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.common.dbiz.request.systemRequest.VersionRequest;
import org.springframework.data.jpa.domain.Specification;

public class VersionSpecification {
    public Specification<Version> hasVersion(String version)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("version"), version);
    }

    public static Specification<Version> hasPlatform(String platform) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("platform"), platform);
    }

    public static Specification<Version> getEntitySpecification(VersionRequest req) {
        Specification<Version> spec = Specification.where(null);

        if(req.getVersion() != null) {
            spec = spec.and(new VersionSpecification().hasVersion(req.getVersion()));
        }
        if(req.getPlatform() != null) {
            spec = spec.and(hasPlatform(req.getPlatform()));
        }
        return spec;
    }
}
