package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.util.List;

public interface CommonIntegrationService {

    public TokenIdempiereRespDto getTokenIdempiere();

    public String getEndPointINT(String nameEnd);

    public String castIntDate(SyncIntegrationCredential credential);

    public String castDateYYYYmmDD(SyncIntegrationCredential credential);

    public String castDateYYYYDDMM(SyncIntegrationCredential credential);

    public TokenERPNextRespDto getTokenERPNext();

    public <T, R> R sendPostRequest(String url, T requestBody, Class<R> responseType);

    String getCookie(String url, String username, String password);
}
