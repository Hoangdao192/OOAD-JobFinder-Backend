package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;

    public String createAddress(AddressModel addressModel) {
        Address address = Address.builder()
                .province(addressModel.getProvince())
                .district(addressModel.getDistrict())
                .ward(addressModel.getWard())
                .detailAddress(addressModel.getDetailAddress())
                .latitude(addressModel.getLatitude())
                .longitude(addressModel.getLongitude())
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

    public Address updateAddressById(int id, AddressModel addressModel) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("api/id", "Không thể update địa chỉ không tồn tại"));
        address.setProvince(addressModel.getProvince());
        address.setDistrict(addressModel.getDistrict());
        address.setWard(addressModel.getWard());
        address.setDetailAddress(addressModel.getDetailAddress());
        address.setLongitude(addressModel.getLongitude());
        address.setLatitude(addressModel.getLatitude());

        addressRepository.save(address);

        return address;
    }
}
