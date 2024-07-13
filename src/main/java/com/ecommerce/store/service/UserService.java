package com.ecommerce.store.service;

import com.ecommerce.store.dto.EmailUpdateDto;
import com.ecommerce.store.dto.JWTAuthResponse;
import com.ecommerce.store.dto.PasswordUpdateDto;
import com.ecommerce.store.dto.UserInfoDto;
import com.ecommerce.store.entity.Role;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository
                .findByEmail(username)
                .orElse(null);
    }


    public boolean userExists(String email) {
        return repository
                .findByEmail(email)
                .isPresent();
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public boolean adminExists() {
        return repository
                .findByRole(Role.Admin)
                .isPresent();
    }

    public boolean isAdmin(User user) {
        return user.getRole() == Role.Admin;
    }


    public String updateUserInfo(User user, UserInfoDto userInfoDto) {
        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        repository.save(user);
        return "User Info has been successfully updated";
    }

    public String updateEmail(User user, EmailUpdateDto emailUpdateDto) {
        user.setEmail(emailUpdateDto.getEmail());
        repository.save(user);
        return "User Email has been successfully updated";
    }

    public JWTAuthResponse reAuthenticateOnUserEmailChange(String email) {
        if (!userExists(email)) return null;
        else {
            User user = (User) loadUserByUsername(email);
            int id = user.getUserId();
            boolean isAdmin = isAdmin(user);
            String jwt = jwtService.generateToken(user, 1000 * 60 * 60);
            Long jwtExpiration = System.currentTimeMillis() + 1000 * 60 * 60;
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user,
                    1000L * 60 * 60 * 24 * 15);
            Long refreshTokenExpiration = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
            JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(id, user.getFirstName(), user.getLastName(), user.getEmail(), jwt, refreshToken,
                    jwtExpiration, refreshTokenExpiration, isAdmin);
            return jwtAuthResponse;
        }
    }

    public String updatePassword(User user, PasswordUpdateDto passwordUpdateDto) {
        user.setPassword(new BCryptPasswordEncoder().encode(passwordUpdateDto.getNewPassword()));
        repository.save(user);
        return "User Password has been successfully updated";
    }
}
