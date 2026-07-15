package com.shankar.Ecommerce.service.impl;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.LoginRequest;
import com.shankar.Ecommerce.dtos.UserDTO;
import com.shankar.Ecommerce.entites.User;
import com.shankar.Ecommerce.enums.UserRole;
import com.shankar.Ecommerce.exception.InvalidCredentialsException;
import com.shankar.Ecommerce.exception.NotFoundException;
import com.shankar.Ecommerce.repository.UserRepository;
import com.shankar.Ecommerce.security.JwtUtils;
import com.shankar.Ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<UserDTO> registerUser(UserDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new NotFoundException("Email already exists");
        }
        log.info("Register User Request | Email: {}", request.getEmail());
        UserRole role = UserRole.USER;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("admin")) {
            log.info("Assigning Role | Email: {} | Role: ADMIN", request.getEmail());
            role = UserRole.ADMIN;
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User Registered Successfully | Id: {} | Email: {} | Role: {}",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole());
        UserDTO savedUserDto = modelMapper.map(savedUser, UserDTO.class);

        return ApiResponse.<UserDTO>builder()
                .status(201)
                .message("User Successfully created ")
                .data(savedUserDto)
                .build();
    }

    @Override
    public ApiResponse<UserDTO> loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User is not Found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return ApiResponse.<UserDTO>builder()
                .message("User Successfully Logged In").status(200)
                .token(token)
                .expirationTime("6 Month")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<User> getAllUser = userRepository.findAll();
        List<UserDTO> getAllUserDto = getAllUser.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
        return ApiResponse.<List<UserDTO>>builder().message("Users Retrieved Successfully").status(200).data(getAllUserDto).build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No authenticated user found");
            throw new UsernameNotFoundException("User not authenticated");
        }

        String email = authentication.getName();

        log.info("Fetching logged-in user | Email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found | Email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public ApiResponse<UserDTO> getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDTO userDto = modelMapper.map(user,UserDTO.class);
        return ApiResponse.<UserDTO>builder().message("Users Retrieved Successfully").status(200).data(userDto).build();
    }

}
