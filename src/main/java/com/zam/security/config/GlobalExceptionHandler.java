package com.zam.security.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.FileInputStream;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("zamecommerce-9293a-firebase-adminsdk-gkjen-d71b1c3fb6.json");

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("zamecommerce-9293a.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

}
