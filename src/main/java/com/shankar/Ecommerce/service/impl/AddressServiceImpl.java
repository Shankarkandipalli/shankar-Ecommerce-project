package com.shankar.Ecommerce.service.impl;

import com.shankar.Ecommerce.dtos.AddressDTO;
import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.entites.Address;
import com.shankar.Ecommerce.entites.User;
import com.shankar.Ecommerce.exception.NotFoundException;
import com.shankar.Ecommerce.repository.AddressRepository;
import com.shankar.Ecommerce.service.AddressService;
import com.shankar.Ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<AddressDTO> saveAndUpdateAddress(AddressDTO addressDTO) {

        log.info("Save/Update address request");

        User user = userService.getLoginUser();

        Address address = user.getAddress();
        boolean isNewAddress = (address == null);

        if (isNewAddress) {
            log.info("Creating new address for user: {}", user.getId());

            address = new Address();
            address.setUser(user);
        } else {
            log.info("Updating existing address for user: {}", user.getId());
        }

        if (addressDTO.getStreet() != null)
            address.setStreet(addressDTO.getStreet());

        if (addressDTO.getCity() != null)
            address.setCity(addressDTO.getCity());

        if (addressDTO.getState() != null)
            address.setState(addressDTO.getState());

        if (addressDTO.getZipcode() != null)
            address.setZipcode(addressDTO.getZipcode());

        if (addressDTO.getCountry() != null)
            address.setCountry(addressDTO.getCountry());

        Address savedAddress = addressRepository.save(address);

        AddressDTO responseDto = modelMapper.map(savedAddress, AddressDTO.class);

        log.info("Address saved successfully. Address Id: {}", savedAddress.getId());

        return ApiResponse.<AddressDTO>builder()
                .status(200)
                .message(isNewAddress
                        ? "Address created successfully"
                        : "Address updated successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse<List<AddressDTO>> getAllAddress() {

        log.info("Fetching all addresses");

        List<Address> addresses = addressRepository.findAll();

        if (addresses.isEmpty()) {
            log.warn("No addresses found");
            throw new NotFoundException("No addresses found");
        }

        List<AddressDTO> addressDTOs = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        log.info("Successfully fetched {} addresses", addressDTOs.size());

        return ApiResponse.<List<AddressDTO>>builder()
                .status(200)
                .message("Addresses retrieved successfully")
                .data(addressDTOs)
                .build();
    }

    @Override
    public ApiResponse<AddressDTO> getAddressById(Long addressId) {

        log.info("Fetching address by id: {}", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId));

        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);

        log.info("Address found successfully | Id: {}", addressId);

        return ApiResponse.<AddressDTO>builder()
                .status(200)
                .message("Address retrieved successfully")
                .data(addressDTO)
                .build();
    }
}
