//package com.dbiz.app.tenantservice.config.google;
//
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.UserCredentials;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class UserGoogleCredentialsConfig {
//
//    @Bean
//    public GoogleCredentials getCredentials() throws IOException {
//
//        InputStream credentialsStream = new ClassPathResource("google-key.json").getInputStream();
//        // Load credentials from JSON file
//        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
//                .createScoped("https://www.googleapis.com/auth/gmail.send");
////        // Cast GoogleCredentials to UserCredentials if using User Authentication
////        if (credentials instanceof UserCredentials) {
////            return (UserCredentials) credentials;
////        } else {
////            throw new IOException("Unable to get User Credentials");
////        }
//        return credentials;
//    }
//}
