package com.dbiz.app.userservice.specification;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class main {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;


        String hpg = "Hpg@123";
        String vig = "Viglacera@123";


        String hpgEncoded = passwordEncoder.encode(hpg);
        String vigEncoded = passwordEncoder.encode(vig);
        System.out.println("Hpg Encoded: " + hpgEncoded);

        System.out.println("Vig Encoded: " + vigEncoded);
    }
}
