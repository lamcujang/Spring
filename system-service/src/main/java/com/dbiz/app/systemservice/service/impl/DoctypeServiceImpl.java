package com.dbiz.app.systemservice.service.impl;


import com.dbiz.app.systemservice.domain.Doctype;
import com.dbiz.app.systemservice.repository.DoctypeRepository;
import com.dbiz.app.systemservice.service.DoctypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DoctypeServiceImpl implements DoctypeService {


    private final DoctypeRepository doctypeRepository;

    @Override
    public GlobalReponse getDoctypeByCode(String code) {


        Optional<Doctype> doctype = Optional.ofNullable(doctypeRepository.findByCode(code));

        if(!doctype.isPresent()) throw new PosException("Doctype not found");

        GlobalReponse response = new GlobalReponse();
        response.setData(doctype.get().getId());
        response.setMessage("Doctype fetched successfully");
        response.setStatus(HttpStatus.OK.value());

        return response;
    }
}
