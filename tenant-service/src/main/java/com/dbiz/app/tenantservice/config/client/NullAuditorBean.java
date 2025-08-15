package com.dbiz.app.tenantservice.config.client;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class NullAuditorBean  implements AuditorAware<Object> {
    @Override
    public Optional<Object> getCurrentAuditor() {
        return Optional.empty(); // Trả về Optional rỗng
    }

}
