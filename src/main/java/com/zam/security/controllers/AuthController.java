package com.zam.security.controllers;

import com.zam.security.payload.request.SignInRequest;
import com.zam.security.payload.request.SignUpRequest;
import com.zam.security.payload.response.SignInResponse;
import com.zam.security.payload.response.SignUpResponse;
import com.zam.security.payload.response.UserLoggedResponse;
import com.zam.security.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.API_PATH)
public class AuthController {

    public static final String API_PATH = "/api/auth";

    public final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        SignInResponse signInResponse = authService.signIn(signInRequest);

        Cookie cookie = new Cookie("auth_token", signInResponse.getToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        response.addCookie(cookie);

        signInResponse.setToken(null);
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        Cookie cookie = new Cookie("auth_token", signUpResponse.getToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        response.addCookie(cookie);

        signUpResponse.setToken(null);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return new ResponseEntity<>(authService.changePassword(oldPassword, newPassword), HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided on cookie: Usuario no esta logueado");
        }
        UserLoggedResponse userLoggedResponse = authService.validateSession(token);
        return new ResponseEntity<>(userLoggedResponse, HttpStatus.OK);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
