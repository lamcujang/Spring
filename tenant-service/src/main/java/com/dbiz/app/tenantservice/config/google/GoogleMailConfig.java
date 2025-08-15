//package com.dbiz.app.tenantservice.config.google;
//
//import com.google.auth.oauth2.AccessToken;
//import com.google.auth.oauth2.GoogleCredentials;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import javax.mail.Authenticator;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class GoogleMailConfig {
//
//    @Value("${google.username}")
//    private String username;
//
//    @Value("${google.password}")
//    private String password;
//
//    @Bean
//    public JavaMailSender getJavaMailSender(GoogleCredentials googleCredentials) throws IOException {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        // Configure Gmail SMTP settings
//        mailSender.setHost("smtp.gmail.com");
////        mailSender.setPort(25);
//        mailSender.setProtocol("smtp");
//        mailSender.setUsername(username);
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "false");
//        props.put("mail.smtp.port", "25");
////        props.put("mail.transport.protocol", "smtp");
////        props.put("mail.from.email", username);
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
//        // Obtain an access token from GoogleCredentials
//        AccessToken accessToken = googleCredentials.refreshAccessToken();
//        props.put("mail.smtp.auth.oauth2.token", accessToken.getTokenValue());
//        // Set the session with an Authenticator that uses the OAuth2 access token
////        Session session = Session.getInstance(props, new Authenticator() {
////            @Override
////            protected PasswordAuthentication getPasswordAuthentication() {
////                return new PasswordAuthentication(username, accessToken.getTokenValue());
////            }
////        });
////        Session session = Session.getInstance(props);
////
////        mailSender.setSession(session);
//
//        mailSender.setJavaMailProperties(props);
//
//        return mailSender;
//    }
//}
