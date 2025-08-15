package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.service.EmailService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.kafka.EmailKafkaDto;
import org.common.dbiz.dto.userDto.password.EmailDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final String GROUP_ID = "gr-sync-order";
    private final String TOPIC_SEND_HTML_MAIL  = "send-html-mail";
    private final JavaMailSender mailSender;

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_SEND_HTML_MAIL, containerFactory = "kafkaListenerContainerFactory")
    public void sendHtmlEmail(ConsumerRecord<String, EmailKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        try {
            EmailKafkaDto dto = consumerRecord.value();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(dto.getFrom());
            helper.setTo(dto.getTo());
            helper.setSubject(dto.getSubject());
            helper.setText(dto.getMessage(), true);  // `true` enables HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }
}
