package com.shankar.Ecommerce.service;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.LoginRequest;
import com.shankar.Ecommerce.dtos.UserDTO;
import com.shankar.Ecommerce.entites.User;


import java.util.List;

public interface UserService {

    ApiResponse<UserDTO> registerUser(UserDTO request);

    ApiResponse<UserDTO> loginUser(LoginRequest request);

    ApiResponse<List<UserDTO>> getAllUsers();

    User getLoginUser();

    ApiResponse<UserDTO> getUserInfoAndOrderHistory();


}
