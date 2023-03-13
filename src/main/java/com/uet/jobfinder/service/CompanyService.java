package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.model.CompanyContext;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.repository.AddressRepository;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    //Create

    public Company createCompany(Long id, CompanyModel companyModel, AddressModel addressModel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        Address address = Address.builder()
                .province(addressModel.getProvince())
                .district(addressModel.getDistrict())
                .ward(addressModel.getWard())
                .detailAddress(addressModel.getDetailAddress())
                .latitude(addressModel.getLatitude())
                .longitude(addressModel.getLongitude())
                .build();

        Company company = Company.builder()
                .user(user)
                .companyName(companyModel.getCompanyName())
                .companyLogo(companyModel.getCompanyLogo())
                .companyDescription(companyModel.getCompanyDescription())
                .address(address)
                .numberOfEmployee(companyModel.getNumberOfEmployee())
                .build();

        companyRepository.save(company);
        return company;
    }

    public List<CompanyModel> getAllCompany() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyModel> companyModels = new ArrayList<CompanyModel>();
        for (Company company : companies) {
            CompanyModel companyModel = CompanyModel.builder()
                    .companyName(company.getCompanyName())
                    .companyDescription(company.getCompanyDescription())
                    .companyLogo(company.getCompanyLogo())
                    .numberOfEmployee(company.getNumberOfEmployee())
                    .build();

            companyModels.add(companyModel);
        }

        return companyModels;
    }

    public CompanyModel getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/company/id", "Công ty không tồn tại"));
        return CompanyModel.builder()
                .companyName(company.getCompanyName())
                .companyDescription(company.getCompanyDescription())
                .companyLogo(company.getCompanyLogo())
                .numberOfEmployee(company.getNumberOfEmployee())
                .build();
    }

    public CompanyContext putCompanyById(Long id, CompanyContext companyContext) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/company/id", "Công ty không tồn tại"));
        Address address = company.getAddress();

        AddressModel addressModel = companyContext.getAddressModel();
        CompanyModel companyModel = companyContext.getCompanyModel();

        address.setProvince(addressModel.getProvince());
        address.setDistrict(addressModel.getDistrict());
        address.setWard(addressModel.getWard());
        address.setDetailAddress(addressModel.getDetailAddress());
        address.setLongitude(addressModel.getLongitude());
        address.setLatitude(addressModel.getLatitude());

        company.setCompanyName(companyModel.getCompanyName());
        company.setCompanyDescription(companyModel.getCompanyDescription());
        company.setCompanyLogo(companyModel.getCompanyLogo());
        company.setNumberOfEmployee(companyModel.getNumberOfEmployee());
        company.setAddress(address);

        companyRepository.save(company);

        return companyContext;
    }

    public String deleteCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/company/id", "Công ty không tồn tại"));
        companyRepository.delete(company);
        companyRepository.deleteById(id);

//        return CompanyModel.builder()
//                .companyName(company.getCompanyName())
//                .build()
//                .toString();
        return Long.toString(companyRepository.count());
    }
}
