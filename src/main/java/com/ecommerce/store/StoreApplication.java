package com.ecommerce.store;

import com.ecommerce.store.entity.Role;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class StoreApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        if (!userService.adminExists()) {
//            User user = new User();
//            user.setEmail("******@****");
//            user.setFirstName("******");
//            user.setLastName("Admin");
//            user.setRole(Role.Admin);
//            user.setPassword(new BCryptPasswordEncoder().encode("******"));
//            userService.addUser(user);
//        }


    }
}
