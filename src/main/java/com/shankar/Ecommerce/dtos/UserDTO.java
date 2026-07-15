package com.shankar.Ecommerce.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

        private Long id;
        private String name;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;
        private String email;
        private String phoneNumber;
        private String role;
        private LocalDateTime createdAt;
        private List<OrderItemDTO> orderItems;
        private AddressDTO address;





}
