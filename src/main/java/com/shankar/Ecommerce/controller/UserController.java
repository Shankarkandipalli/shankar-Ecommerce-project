package com.shankar.Ecommerce.controller;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.LoginRequest;
import com.shankar.Ecommerce.dtos.UserDTO;
import com.shankar.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>>getAllUser(){
        return  ResponseEntity.ok(userService.getAllUsers());

    }
    @GetMapping("/myinfo")
    public ResponseEntity<ApiResponse<?>>getUserInfoAndOrderHistory(){
        return  ResponseEntity.ok(userService.getUserInfoAndOrderHistory());

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>>login(@RequestBody LoginRequest request){
        return  ResponseEntity.ok(userService.loginUser(request));

    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>>register(@RequestBody UserDTO request){
        return  ResponseEntity.ok(userService.registerUser(request));

    }







}
