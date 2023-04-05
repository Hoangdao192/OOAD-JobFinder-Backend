package com.uet.jobfinder.controller;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/address")
public class AddressController {

    private AddressService addressService;

    @PostMapping
    public ResponseEntity<String> createAddress(
            @RequestBody @Valid AddressModel addressModel
    ) {
        String str = addressService.createAddress(addressModel);
        return ResponseEntity
                .accepted()
                .body(str);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddress() {
        return ResponseEntity.ok().body(addressService.getAllAddress());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable int id) {
        return ResponseEntity.ok().body(addressService.deleteAddressById(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Address> updateAddressById(@PathVariable int id,
                                                 @RequestBody @Valid AddressModel addressModel) {
        Address address = addressService.updateAddressById(id, addressModel);
        return ResponseEntity
                .accepted()
                .body(address);
    }

    @Autowired
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }
}
