package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.userservice.domain.Role;
import com.dbiz.app.userservice.domain.RolePermission;
import com.dbiz.app.userservice.repository.RolePermissionRepository;
import com.dbiz.app.userservice.repository.RoleRepository;
import com.dbiz.app.userservice.service.UserRolePermissionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.RolePermissionVDto;
import org.common.dbiz.payload.GlobalReponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserRolePermissionServiceImpl implements UserRolePermissionService {
    private final MessageSource messageSource;

    private final EntityManager entityManager;

    private final RolePermissionRepository rolePermissionRepository;

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;
    private StringBuilder sql;
    private final RoleRepository roleRepository;



    /**
     * @param roleId
     * @return
     */
    @Override
    public GlobalReponse getRolePermission(Integer roleId) {
        sql = new StringBuilder("SELECT d_role_id, name, code, d_tenant_id, json_data \n" +
                "FROM d_role_permission_v \n" +
                "WHERE d_tenant_id =:tenantId \n");
        if (roleId != 0)
            sql.append("AND d_role_id = :roleId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        if (roleId != 0)
            query.setParameter("roleId", roleId);
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("d_role_id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("code", StandardBasicTypes.STRING)
                .addScalar("json_data", StandardBasicTypes.STRING)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> results = query.getResultList();
//        List<RolePermissionVDto> listResults = new ArrayList<>();
        RolePermissionVDto resultDto = new RolePermissionVDto();

        for (Map<String, Object> row : results) {
            String jsonData = ParseHelper.STRING.parse(row.get("json_data"));
            JsonNode jsonDataNode = null;
            try {
                jsonDataNode = objectMapper.readTree(jsonData).get("permissions");
            } catch (Exception e) {
                log.error("Error parse json: {}", e.getMessage());
            }
            Role role = roleRepository.findById(ParseHelper.INT.parse(row.get("d_role_id"))).get();
             resultDto = RolePermissionVDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_role_id")))
                    .name(ParseHelper.STRING.parse(row.get("name")))
                    .code(ParseHelper.STRING.parse(row.get("code")))
                     .isActive(role.getIsActive())
                    .permissions(jsonDataNode)

                    .build();
        }

        return GlobalReponse.builder()
                .data(resultDto)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("").build();
    }

    /**
     * @param rolePermissionVDtos
     * @return
     */
    @Override
    public GlobalReponse save( RolePermissionVDto rolePermissionVDtos) {


            RolePermission role = rolePermissionRepository.findByRoleIdAndTenantId(rolePermissionVDtos.getRoleId(), AuditContext.getAuditInfo().getTenantId());
            Role roleUpdate = roleRepository.findById(rolePermissionVDtos.getRoleId()).get();
            roleUpdate.setIsActive(rolePermissionVDtos.getIsActive());
            this.roleRepository.save(roleUpdate);
            log.info("RolePermission: {}", role);
            try {
                this.updateRolePermission(rolePermissionVDtos);

            } catch (Exception e) {
                log.error("Error parse json: {}", e.getMessage());
            }

        GlobalReponse res = GlobalReponse.builder()
                .errors("")
                .data(rolePermissionVDtos).
                message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
        return res;
    }

    @Transactional
    public void updateRolePermission(RolePermissionVDto rolePermissionVDto) {
        StringBuilder sql = new StringBuilder("UPDATE pos.d_role_permission SET json_data = :jsonData WHERE d_role_id = :roleId AND d_tenant_id = :tenantId");
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("permissions", rolePermissionVDto.getPermissions());
            entityManager.createNativeQuery(
                            "UPDATE pos.d_role_permission SET json_data = :jsonData, updated = now() , updated_by=:userId  WHERE d_role_id = :roleId AND d_tenant_id = :tenantId")
                    .unwrap(NativeQuery.class)
                    .setParameter("jsonData",  node, JsonBinaryType.INSTANCE)
                    .setParameter("roleId", rolePermissionVDto.getRoleId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("userId", AuditContext.getAuditInfo().getUserId())
                    .executeUpdate();

            Role roleUpdate = roleRepository.findById(rolePermissionVDto.getRoleId()).get();
            roleUpdate.setDescription(rolePermissionVDto.getDescription());
            roleUpdate.setName(rolePermissionVDto.getName());
            roleRepository.save(roleUpdate);

//            Query query = entityManager.createNativeQuery(sql.toString());
//            query.setParameter("jsonData", rolePermissionVDto.getJsonData().toString());
//            query.setParameter("roleId", rolePermissionVDto.getRoleId());
//            query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
//
//            int result = query.executeUpdate();
//            if (result == 0) {
//                throw new RuntimeException("No records updated. Check if roleId and tenantId exist.");
//            }

        } catch (Exception e) {
            log.error("Error during updateRolePermission: {}", e.getMessage());
            throw e;  // Để Spring quản lý rollback
        }
    }

}
