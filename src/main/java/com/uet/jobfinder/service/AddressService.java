package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.dto.AddressDTO;
import com.uet.jobfinder.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;

    public String createAddress(AddressDTO addressDTO) {
        Address address = Address.builder()
                .province(addressDTO.getProvince())
                .district(addressDTO.getDistrict())
                .ward(addressDTO.getWard())
                .detailAddress(addressDTO.getDetailAddress())
                .latitude(addressDTO.getLatitude())
                .longitude(addressDTO.getLongitude())
                .build();

        addressRepository.save(address);

        return address.toString();
    }

    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public Address deleteAddressById(int id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("api/id", "Không thể xóa địa chỉ không tồn tại"));
        addressRepository.delete(address);
        return address;
    }

    public Address updateAddressById(int id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("api/id", "Không thể update địa chỉ không tồn tại"));
        address.setProvince(addressDTO.getProvince());
        address.setDistrict(addressDTO.getDistrict());
        address.setWard(addressDTO.getWard());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setLongitude(addressDTO.getLongitude());
        address.setLatitude(addressDTO.getLatitude());

        addressRepository.save(address);

        return address;
    }
}
