package com.shankar.Ecommerce.service;

import com.shankar.Ecommerce.dtos.AddressDTO;
import com.shankar.Ecommerce.dtos.ApiResponse;

import java.util.List;

public interface AddressService {

    ApiResponse<AddressDTO> saveAndUpdateAddress(AddressDTO addressDTO);

    ApiResponse<List<AddressDTO>> getAllAddress();

    ApiResponse<AddressDTO> getAddressById(Long addressId);


}
