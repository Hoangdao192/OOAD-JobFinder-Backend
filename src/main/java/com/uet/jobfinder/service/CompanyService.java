package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.AppFile;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CompanyService {

    @Autowired
    private FileService fileService;

    private ModelMapper modelMapper;
    private UserService userService;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private JsonWebTokenProvider jsonWebTokenProvider;

    public Company getCompanyByUserId(Long userId) {
        //  TODO: Replace by find company
        User user = userService.getUserById(userId);
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
    }

    public Company createEmptyCompany(User user) {
        Company company = new Company();
        company.setUser(user);
        return companyRepository.save(company);
    }

//    public Company createCompany(Long id, CompanyModel companyModel, AddressModel addressModel) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));
//
//        Address address = Address.builder()
//                .province(addressModel.getProvince())
//                .district(addressModel.getDistrict())
//                .ward(addressModel.getWard())
//                .detailAddress(addressModel.getDetailAddress())
//                .latitude(addressModel.getLatitude())
//                .longitude(addressModel.getLongitude())
//                .build();
//
//        Company company = Company.builder()
//                .user(user)
//                .companyName(companyModel.getCompanyName())
//                .companyLogo(companyModel.getCompanyLogo())
//                .companyDescription(companyModel.getCompanyDescription())
//                .address(address)
//                .numberOfEmployee(companyModel.getNumberOfEmployee())
//                .build();
//
//        companyRepository.save(company);
//        return company;
//    }

    public CompanyModel updateCompany(CompanyModel companyModel, HttpServletRequest request) throws IOException {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Address address = company.getAddress();
        AddressModel addressModel = companyModel.getAddress();
        if (address == null && addressModel != null) {
            address = Address.builder()
                    .province(addressModel.getProvince())
                    .district(addressModel.getDistrict())
                    .ward(addressModel.getWard())
                    .detailAddress(addressModel.getDetailAddress())
                    .latitude(addressModel.getLatitude())
                    .longitude(addressModel.getLongitude())
                    .build();
            company.setAddress(address);
        } else if (addressModel != null) {
            address.setProvince(addressModel.getProvince());
            address.setDistrict(addressModel.getDistrict());
            address.setWard(addressModel.getWard());
            address.setDetailAddress(addressModel.getDetailAddress());
            address.setLatitude(addressModel.getLatitude());
            address.setLongitude(addressModel.getLongitude());
        }

        if (companyModel.getCompanyName() != null) {
            company.setCompanyName(companyModel.getCompanyName());
        }
        if (companyModel.getCompanyLogoFile() != null) {
            AppFile appFile = fileService.saveFile(
                    companyModel.getCompanyLogoFile().getOriginalFilename(),
                    companyModel.getCompanyLogoFile().getContentType(),
                    companyModel.getCompanyLogoFile().getBytes()
            );
            company.setCompanyLogo(appFile);
        }
        if (companyModel.getCompanyDescription() != null) {
            company.setCompanyDescription(companyModel.getCompanyDescription());
        }
        if (address != null) {
            company.setAddress(address);
        }
        if (companyModel.getNumberOfEmployee() != null) {
            company.setNumberOfEmployee(companyModel.getNumberOfEmployee());
        }

        company = companyRepository.save(company);

        return modelMapper.map(company, CompanyModel.class);
    }

    public List<CompanyModel> getAllCompany() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyModel> companyModels = new ArrayList<>();
        for (Company company : companies) {
            CompanyModel companyModel = CompanyModel.builder()
                    .companyName(company.getCompanyName())
                    .companyDescription(company.getCompanyDescription())
                    .companyLogo(company.getCompanyLogo().getFilePath() )
                    .numberOfEmployee(company.getNumberOfEmployee())
                    .build();

            companyModels.add(companyModel);
        }

        return companyModels;
    }

    public CompanyModel getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        return modelMapper.map(company, CompanyModel.class);
    }

//    public CompanyContext putCompanyById(Long id, CompanyContext companyContext) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new InvalidPathException("../api/company/id", "Công ty không tồn tại"));
//        Address address = addressRepository.findById(company.getAddress().getId())
//                .orElseThrow(() -> new InvalidPathException("../api/company/id", "Công ty không tồn tại"));
//
//        AddressModel addressModel = companyContext.getAddressModel();
//        CompanyModel companyModel = companyContext.getCompanyModel();
//
//        address.setProvince(addressModel.getProvince());
//        address.setDistrict(addressModel.getDistrict());
//        address.setWard(addressModel.getWard());
//        address.setDetailAddress(addressModel.getDetailAddress());
//        address.setLongitude(addressModel.getLongitude());
//        address.setLatitude(addressModel.getLatitude());
//
//        company.setCompanyName(companyModel.getCompanyName());
//        company.setCompanyDescription(companyModel.getCompanyDescription());
//        company.setCompanyLogo(companyModel.getCompanyLogo());
//        company.setNumberOfEmployee(companyModel.getNumberOfEmployee());
//        company.setAddress(address);
//
//        companyRepository.save(company);
//
//        return companyContext;
//    }

    public boolean deleteCompanyById(Long id, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        //  User is only be deleted by admin or by this user itself
        if (company.getId().equals(id) && !userService.isAdmin(id)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        companyRepository.delete(company);
        return true;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setJsonWebTokenProvider(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }
}
