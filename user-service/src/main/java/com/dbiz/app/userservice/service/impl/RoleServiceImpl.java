package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.Role;
import com.dbiz.app.userservice.domain.RolePermission;
import com.dbiz.app.userservice.helper.DateHelper;
import com.dbiz.app.userservice.repository.RolePermissionRepository;
import com.dbiz.app.userservice.repository.RoleRepository;
import com.dbiz.app.userservice.repository.UserRoleAccessRepository;
import com.dbiz.app.userservice.service.RoleService;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.RoleDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.RoleQueryRequest;
import org.hibernate.query.NativeQuery;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final UserRoleAccessRepository userRoleAccessRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final EntityManager entityManager;

    /**
     * @param roleQueryRequest
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(RoleQueryRequest roleQueryRequest) {
        Specification<Role> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                roleQueryRequest.getName() == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + roleQueryRequest.getName().toLowerCase() + "%"),
                criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId())
        );
        spec=spec.and ( (root, query, criteriaBuilder) -> roleQueryRequest.getIsActive() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isActive"), roleQueryRequest.getIsActive()));
        spec = spec.and((root, query, criteriaBuilder) ->   criteriaBuilder.notEqual(root.get("id"), 0));
        Pageable pageable = requestParamsUtils.getPageRequest(roleQueryRequest);
        Page<Role> pages = roleRepository.findAll(spec, pageable);

        List<RoleDto> resultList = new ArrayList<>();

        pages.getContent().forEach(
                item -> {
                    RoleDto itemDto = modelMapper.map(item, RoleDto.class);
                    itemDto.setCreated(DateHelper.fromInstantDMY(item.getCreated()));
                    itemDto.setUpdated(DateHelper.fromInstantDMY(item.getUpdated()));
                    resultList.add(itemDto);
                }
        );

        return GlobalReponsePagination.builder()
                .data(resultList)
                .totalPages(pages.getTotalPages())
                .totalItems(pages.getTotalElements())
                .currentPage(pages.getNumber())
                .pageSize(pages.getSize())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())
                ).status(HttpStatus.OK.value()).build();
    }

    /**
     * @param roleDto
     * @return
     */
    @Override
    public GlobalReponse save(RoleDto roleDto) {
        Role roleCheck ;

        GlobalReponse response = new GlobalReponse();

        if(roleDto.getId()!= null)
        {
            roleCheck = roleRepository.findByIdAndTenantId(roleDto.getId(),AuditContext.getAuditInfo().getTenantId()).orElseThrow(()->new PosException(messageSource.getMessage("role.notfound",null, LocaleContextHolder.getLocale())));
            modelMapper.map(roleDto,roleCheck);
            roleCheck = roleRepository.save(roleCheck);
            response.setData(modelMapper.map(roleCheck,RoleDto.class));
            response.setMessage(messageSource.getMessage("role.update.success",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        }
        else
        {
            if(roleRepository.existsByName(roleDto.getName()))
                throw new PosException(messageSource.getMessage("role.name.exist",null, LocaleContextHolder.getLocale()));
            roleCheck =modelMapper.map(roleDto,Role.class);
            roleCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(roleCheck.getCode() == null)
            {
                String docNo = "ROL" + (roleRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                roleCheck.setCode(docNo);
            }
            roleCheck = roleRepository.save(roleCheck);
            response.setData(modelMapper.map(roleCheck,RoleDto.class));
            response.setMessage(messageSource.getMessage("role.save.success",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            if(roleDto.getCopyFromRoleId() != null)
            {
                RolePermission rolePermission = rolePermissionRepository.findByRoleIdAndTenantId(roleDto.getCopyFromRoleId(),AuditContext.getAuditInfo().getTenantId());

                entityManager.createNativeQuery(
                                "INSERT INTO pos.d_role_permission (json_data, updated, updated_by, created, created_by, d_role_id, d_tenant_id)\n" +
                                        "VALUES (:jsonData, NOW(), :userId, NOW(), :userId, :roleId, :tenantId)")
                        .unwrap(NativeQuery.class)
                        .setParameter("jsonData",  rolePermission.getJsonData(), JsonBinaryType.INSTANCE)
                        .setParameter("roleId", roleCheck.getId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("userId", AuditContext.getAuditInfo().getUserId())
                        .executeUpdate();
            }
            if(roleDto.getFullRole()!= null && roleDto.getFullRole().equals("Y"))
            {
                RolePermission rolePermission = rolePermissionRepository.findByRoleIdAndTenantId(0,AuditContext.getAuditInfo().getTenantId());

                entityManager.createNativeQuery(
                                "INSERT INTO pos.d_role_permission (json_data, updated, updated_by, created, created_by, d_role_id, d_tenant_id)\n" +
                                        "VALUES (:jsonData, NOW(), :userId, NOW(), :userId, :roleId, :tenantId)")
                        .unwrap(NativeQuery.class)
                        .setParameter("jsonData",  rolePermission.getJsonData(), JsonBinaryType.INSTANCE)
                        .setParameter("roleId", roleCheck.getId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("userId", AuditContext.getAuditInfo().getUserId())  .executeUpdate();
            }
        }



        return response;
    }

    /**
     * @param roleId
     * @param userId
     * @return
     */
    @Override
    public GlobalReponsePagination getRoleAccess(Integer roleId, Integer userId, Integer page, Integer pageSize) {
        GlobalReponsePagination reponse = new GlobalReponsePagination();
        Role role = roleRepository.findByIdAndTenantId(roleId, AuditContext.getAuditInfo().getTenantId()).orElseThrow(() -> new PosException(messageSource.getMessage("role.notfound", null, LocaleContextHolder.getLocale())));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Role> roles;
        if(role.getCode().equals("ADM"))
        {
            roles = roleRepository.findAllByTenantId(AuditContext.getAuditInfo().getTenantId(),pageable);
            List<RoleDto> roleDto = roles.getContent().stream().map(item -> {
                RoleDto roleDto1 = modelMapper.map(item, RoleDto.class);
                roleDto1.setCreated(DateHelper.fromInstantDMY(item.getCreated()));
                roleDto1.setUpdated(DateHelper.fromInstantDMY(item.getUpdated()));
                return roleDto1;
            }).collect(Collectors.toList());
            reponse.setData(roleDto);
        }
        else{
            List<Integer> roleIds = userRoleAccessRepository.findRoleIdByUserId(userId);
              roles = roleRepository.findAllByIdIn(roleIds,pageable);
            List<RoleDto> roleDto = roles.getContent().stream().map(item -> {
                RoleDto roleDto1 = modelMapper.map(item, RoleDto.class);
                roleDto1.setCreated(DateHelper.fromInstantDMY(item.getCreated()));
                roleDto1.setUpdated(DateHelper.fromInstantDMY(item.getUpdated()));
                return roleDto1;
            }).collect(Collectors.toList());
            reponse.setData(roleDto);
        }
        reponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        reponse.setStatus(HttpStatus.OK.value());
        reponse.setTotalPages(roles.getTotalPages());
        reponse.setTotalItems(roles.getTotalElements());
        reponse.setCurrentPage(roles.getNumber());
        reponse.setPageSize(roles.getSize());

        return reponse;
    }
}
