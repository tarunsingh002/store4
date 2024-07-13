package com.ecommerce.store.service;

import com.ecommerce.store.dto.JWTAuthRequest;
import com.ecommerce.store.dto.JWTAuthResponse;
import com.ecommerce.store.dto.SignInRequest;
import com.ecommerce.store.dto.SignUpRequest;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.Role;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.entity.WishList;
import com.ecommerce.store.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private WishListRepository wishListRepository;

    public JWTAuthResponse signUp(SignUpRequest signUpRequest) {
        if (userService.userExists(signUpRequest.getEmail())) return null;
        else {

            User user = new User();
            user.setEmail(signUpRequest.getEmail());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setRole(Role.User);
            user = userService.addUser(user);
            WishList wishList = new WishList();
            wishList.setUser(user);
            wishList = wishListRepository.save(wishList);
            return signIn(new SignInRequest(signUpRequest.getEmail(), signUpRequest.getPassword()));
        }
    }

    public JWTAuthResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        if (!userService.userExists(signInRequest.getEmail())) return null;
        else {
            User user = (User) userService.loadUserByUsername(signInRequest.getEmail());
            int id = user.getUserId();
            boolean isAdmin = userService.isAdmin(user);
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

    public JWTAuthResponse refreshToken(JWTAuthRequest jwtAuthRequest) {
        String userEmail = jwtService.extractUsername(jwtAuthRequest.getRefreshToken());
        if (userService.userExists(userEmail) && jwtService.isTokenValid(jwtAuthRequest.getRefreshToken(), userService.loadUserByUsername(userEmail))) {
            User user = (User) userService.loadUserByUsername(userEmail);
            String jwt = jwtService.generateToken(userService.loadUserByUsername(userEmail),
                    1000 * 60 * 60);
            Long jwtExpiration = System.currentTimeMillis() + 1000 * 60 * 60;
            Long refreshTokenExpiration = jwtService
                    .extractExpirationDate(jwtAuthRequest.getRefreshToken())
                    .getTime();
            boolean isAdmin = userService.isAdmin(user);
            JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(), jwt,
                    jwtAuthRequest.getRefreshToken(), jwtExpiration, refreshTokenExpiration,
                    isAdmin);
            return jwtAuthResponse;
        }
        return null;
    }
}
