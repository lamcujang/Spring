package com.dbiz.app.proxyclient.business.user.model.response;

import com.dbiz.app.proxyclient.business.user.model.TenantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DTenantServiceCollectionDtoResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Collection<TenantDto> collection;


	
}
