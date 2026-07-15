package com.shankar.Ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(int status,
                             String message,
                             T data,String token,String expirationTime,String role) {


}