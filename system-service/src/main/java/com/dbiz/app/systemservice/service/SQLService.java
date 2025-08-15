package com.dbiz.app.systemservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.multipart.MultipartFile;

public interface SQLService {

    GlobalReponse modifyAll(MultipartFile file);

    GlobalReponse grantAccessAll(MultipartFile file);


    GlobalReponse clearTrialData();

}
