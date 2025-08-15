package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.paymentservice.domain.Bank;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.BankAccount;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;

public class BankAccountSpecification {

    
    public static Specification<BankAccount> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<BankAccount>equalIds(Integer[] ids)
    {
        return (Root<BankAccount> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            //
            if (ids == null || ids.length == 0) {
                return criteriaBuilder.conjunction(); //
            }
            return root.get("id").in(Arrays.asList(ids));
        };
    }

    public static Specification<BankAccount> likeName(String keyword) {
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );}

    public static Specification<BankAccount> likeAccountNo(String keyword) {
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("accountNo")),
                "%" + keyword.toLowerCase() + "%"
        );}
    private static Specification<BankAccount> equalBankId(Integer bankId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bankId"), bankId);
    }

    private static Specification<BankAccount> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<BankAccount> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<BankAccount> byIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<BankAccount> equalCash(String isCash)
    {
        if(isCash == null || isCash.equals("N"))
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return (root, query, criteriaBuilder) -> {
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Bank> bankRoot = subquery.from(Bank.class);
            subquery.select(bankRoot.get("id")).where(criteriaBuilder.equal(bankRoot.get("swiftCode"), "QTM"));

            return criteriaBuilder.not(root.get("bankId").in(subquery));
        };
    }

    public static Specification<BankAccount> equalDefault(String isDefault)
    {
        return (root, query, criteriaBuilder) -> isDefault == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isDefault"), isDefault);
    }

    public static Specification<BankAccount> getSpecification(BankAccountQueryRequest request) {
        Specification<BankAccount> spec = Specification.where(null);
        if(request.getKeyword() != null && !request.getKeyword().isEmpty())
        {
            spec = spec.or(likeName(request
                    .getKeyword()));
            spec = spec.or(likeAccountNo(request
                    .getKeyword()));
        }
        if(request.getBankId()!=null)
        {
            spec = spec.and(equalBankId(request
                    .getBankId()));
        }
        if(request.getId()!= null)
            spec = spec.and(byId(request.getId()));
        spec = spec.and(tenantId(AuditContext.getAuditInfo().getTenantId()));
        spec= spec.and(equalCash(request.getIsCash()));
        spec = spec.and(byIsActive(request.getIsActive()));
        spec = spec.and(equalDefault(request.getIsDefault()));
        return spec;
    }
}