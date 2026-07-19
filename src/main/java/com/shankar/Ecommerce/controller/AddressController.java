package com.shankar.Ecommerce.controller;

import com.shankar.Ecommerce.dtos.AddressDTO;
import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AddressDTO>>saveAndUpdateAddres(@RequestBody AddressDTO addressDTO){
        return ResponseEntity.ok(addressService.saveAndUpdateAddress(addressDTO));
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAllAddress() {
        return ResponseEntity.ok(addressService.getAllAddress());
    }
    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressDTO>>getByAddresId(@PathVariable Long addressId){
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

}