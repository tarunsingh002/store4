package com.ecommerce.store.controller;

import com.ecommerce.store.dto.*;
import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.service.AuthService;
import com.ecommerce.store.service.JWTService;
import com.ecommerce.store.service.OrderService;
import com.ecommerce.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/v1/user/createorder")
    public Order createOrder(@RequestBody List<OrderItemDto> orderItemDtos,
                             @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return orderService.createOrder(orderItemDtos, user);
        }
        return null;
    }

    @GetMapping("/api/v1/user/getorders")
    public List<Order> getOrders(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return orderService.getUserOrders(user);
        }
        return null;
    }

    @PutMapping("/api/v1/user/updateuserinfo")
    public ResponseEntity<String> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody UserInfoDto userInfoDto) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (!userService.userExists(userEmail)) {
            return new ResponseEntity<String>("User does not exists", HttpStatus.NOT_ACCEPTABLE);
        }
        User user = (User) userService.loadUserByUsername(userEmail);
        String updateStatus = userService.updateUserInfo(user, userInfoDto);
        if (updateStatus.equals("User Info has been successfully updated")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/api/v1/user/updateemail")
    public ResponseEntity<?> updateEmail(@RequestHeader("Authorization") String token, @RequestBody EmailUpdateDto emailUpdateDto) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (!userService.userExists(userEmail)) {
            return new ResponseEntity<String>("User does not exists", HttpStatus.NOT_ACCEPTABLE);
        }
        if (userService.userExists(emailUpdateDto.getEmail())) {
            return new ResponseEntity<String>("This email belongs to another user", HttpStatus.NOT_ACCEPTABLE);
        }
        User user = (User) userService.loadUserByUsername(userEmail);
        String updateStatus = userService.updateEmail(user, emailUpdateDto);
        if (updateStatus.equals("User Email has been successfully updated")) {
            return new ResponseEntity<>(userService.reAuthenticateOnUserEmailChange(emailUpdateDto.getEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/api/v1/user/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String token, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (!userService.userExists(userEmail)) {
            return new ResponseEntity<String>("User does not exists", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, passwordUpdateDto.getCurrentPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>("Current Password is incorrect",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        User user = (User) userService.loadUserByUsername(userEmail);
        String updateStatus = userService.updatePassword(user, passwordUpdateDto);

        if (updateStatus.equals("User Password has been successfully updated")) {
            return new ResponseEntity<String>(updateStatus, HttpStatus.OK);
        }

        return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
