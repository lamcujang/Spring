package com.dbiz.app.systemservice.service;

import org.common.dbiz.payload.GlobalReponse;

public interface DoctypeService {

    GlobalReponse getDoctypeByCode(String code);
}
