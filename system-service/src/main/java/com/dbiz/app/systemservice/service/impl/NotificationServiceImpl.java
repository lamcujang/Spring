package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Notification;
import com.dbiz.app.systemservice.helper.NotifyHelper;
import com.dbiz.app.systemservice.repository.MessageRepository;
import com.dbiz.app.systemservice.repository.NotificationRepository;
import com.dbiz.app.systemservice.service.NotificationService;
import com.dbiz.app.systemservice.specification.NotificationSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ClearCacheDto;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.json.HTTP;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final NotifyHelper notify;

    private final MessageRepository messageRepository;

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(NotificationQueryRequest request) {
        Specification<Notification> spec = NotificationSpecification.getEntitySpecification(request);
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Page<Notification> resultQuery = notificationRepository.findAll(spec, pageable);
        List<NotificationDto> resultResponse = new ArrayList<>();
        resultQuery.stream().forEach(
                item -> {
                    NotificationDto itemDto = modelMapper.map(item, NotificationDto.class);
                    itemDto.setCreated(DateHelper.fromInstantUTC(item.getCreated()));
                    resultResponse.add(itemDto);
                }
        );

        return GlobalReponsePagination.builder()
                .data(resultResponse)
                .pageSize(resultQuery.getSize())
                .totalPages(resultQuery.getTotalPages())
                .totalItems(resultQuery.getTotalElements())
                .status(HttpStatus.OK.value())
                .currentPage(resultQuery.getNumber())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(NotificationDto entity) {
        Notification notification = notificationRepository.findById(entity.getId()).orElseThrow(() -> new PosException("notification.not.found"));

        modelMapper.map(entity, notification);
        notificationRepository.save(notification);


        return GlobalReponse.builder()
                .message(messageSource.getMessage("notify.update", null, LocaleContextHolder.getLocale()))
                .data(modelMapper.map(notification, NotificationDto.class))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse sendNotify(SendNotification request) {
        log.info("*** System config, service; send notify *");
        log.info("body " + request.toString());
        if (request.getDeviceTokens() != null && !request.getDeviceTokens().isEmpty()) {
            request.getDeviceTokens().forEach(
                    item ->
                    {
                        notify.notifyFirebase(item, request.getStatus(), request.getTitle(), request.getBody(), request.getCode(), 1, request.getRouter(), request.getType(), request.getSpeak());
                    }
            );
        }
        String result = notify.notifyFirebase(request.getDeviceToken(), request.getStatus(), request.getTitle(), request.getBody(), null,1,"router", request.getType(), request.getSpeak());

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .content(request.getBody())
                .notificationType(request.getType())
                .status("UNR")
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .build();
        if(request.getRouterFunction()!= null && request.getRecordId() != null)
        {
            notification.setRouteFunction(request.getRouterFunction());
            notification.setRecordId(request.getRecordId());
        }
        notificationRepository.save(notification);
        return GlobalReponse.builder()
                .data(result).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    
    /**
     * @param key
     * @return
     */
    @Override
    public GlobalReponse getMessage(String key) {
        log.info("*** System config, service; get message *");
        return GlobalReponse.builder()
                .data(modelMapper.map(messageRepository.findByValue(key), NotificationDto.class))
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse clearCacheFE(ClearCacheDto dto) {


//        // These registration tokens come from the client FCM SDKs.
//        List<String> registrationTokens = Arrays.asList(
//                "YOUR_REGISTRATION_TOKEN_1",
//                // ...
//                "YOUR_REGISTRATION_TOKEN_n"
//        );
//
//// Subscribe the devices corresponding to the registration tokens to the
//// topic.
//        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
//                registrationTokens, topic);
//// See the TopicManagementResponse reference documentation
//// for the contents of response.
//        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
//
//        log.info("*** System config, service; clear cache *");
//        notify.notifyFirebaseTopic("UPDATE-FE-VERSION");
//        return GlobalReponse.builder()
//                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
//                .status(HttpStatus.OK.value())
//                .errors("")
//                .build();
        return null;
    }

    /**
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse updateStatusNotification(NotificationDto dto) {
        List<NotificationDto> responseData =new ArrayList<>();
        if (dto.getIsUpdateAll().equals("Y")) {
            this.notificationRepository.updateAllByStatus(dto.getStatus(),"UNR");
        }else{
            this.notificationRepository.updateStatusById(dto.getStatus(),dto.getId());
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully",null,LocaleContextHolder.getLocale()))
                .data(null)
                .errors("").build();
    }
}
