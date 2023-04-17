package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.AppFile;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.dto.AddressDTO;
import com.uet.jobfinder.dto.CompanyDTO;
import com.uet.jobfinder.dto.PageQueryModel;
import com.uet.jobfinder.repository.*;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;


@Service
public class CompanyService {

    private FileService fileService;

    private ModelMapper modelMapper;
    private UserService userService;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private JsonWebTokenProvider jsonWebTokenProvider;
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private EvaluateRepository evaluateRepository;

    public Company getCompanyByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
    }

    public Company createEmptyCompany(User user) {
        Company company = new Company();
        company.setUser(user);
        return companyRepository.saveAndFlush(company);
    }

    public Company createCompany(Company company) {
        company.setId(null);
        return companyRepository.save(company);
    }

    public PageQueryModel<CompanyDTO> getAllCompany(Integer page, Integer pageSize) {
        Page<Company> companyPage = companyRepository.findAll(
                PageRequest.of(page, pageSize)
        );
        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        companyPage.getPageable().getPageNumber(),
                        companyPage.getPageable().getPageSize(),
                        companyPage.getTotalPages()
                ),
                companyPage.getContent()
                        .stream()
                        .map(company -> modelMapper.map(company, CompanyDTO.class))
                        .collect(Collectors.toList())
        );
    }

    public CompanyDTO updateCompany(CompanyDTO companyDTO, HttpServletRequest request) throws IOException {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        if (companyDTO.getCompanyLogoFile() == null && company.getCompanyLogo() == null) {
            throw new CustomIllegalArgumentException(
                    ServerError.NULL_COMPANY_LOGO
            );
        }

        Address address = company.getAddress();
        AddressDTO addressDTO = companyDTO.getAddress();
        if (address == null && addressDTO != null) {
            address = Address.builder()
                    .province(addressDTO.getProvince())
                    .district(addressDTO.getDistrict())
                    .ward(addressDTO.getWard())
                    .detailAddress(addressDTO.getDetailAddress())
                    .latitude(addressDTO.getLatitude())
                    .longitude(addressDTO.getLongitude())
                    .build();
            company.setAddress(address);
        } else if (addressDTO != null) {
            address.setProvince(addressDTO.getProvince());
            address.setDistrict(addressDTO.getDistrict());
            address.setWard(addressDTO.getWard());
            address.setDetailAddress(addressDTO.getDetailAddress());
            address.setLatitude(addressDTO.getLatitude());
            address.setLongitude(addressDTO.getLongitude());
        }

        if (companyDTO.getCompanyName() != null) {
            company.setCompanyName(companyDTO.getCompanyName());
        }
        if (companyDTO.getCompanyLogoFile() != null) {
            AppFile appFile = fileService.saveFile(
                    companyDTO.getCompanyLogoFile().getOriginalFilename(),
                    companyDTO.getCompanyLogoFile().getContentType(),
                    companyDTO.getCompanyLogoFile().getBytes()
            );
            company.setCompanyLogo(appFile);
        }
        if (companyDTO.getCompanyDescription() != null) {
            company.setCompanyDescription(companyDTO.getCompanyDescription());
        }
        if (address != null) {
            company.setAddress(address);
        }
        if (companyDTO.getNumberOfEmployee() != null) {
            company.setNumberOfEmployee(companyDTO.getNumberOfEmployee());
        }

        company = companyRepository.save(company);

        return modelMapper.map(company, CompanyDTO.class);
    }

    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        return modelMapper.map(company, CompanyDTO.class);
    }

    public Company getCompanyByUser(User user) {
       return companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
    }

    public Long countComingApplication(Long companyId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        if (!companyId.equals(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Company company = getCompanyByUserId(companyId);

        return jobApplicationRepository.countCompanyComingApplication(company.getId());
    }

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

    public PageQueryModel<CompanyDTO> findCompany(String searchKey, Integer page, Integer pageSize) {
        Page<Company> companies = companyRepository.searchAllCompany(
                PageRequest.of(page, pageSize), searchKey);

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        companies.getPageable().getPageNumber(),
                        companies.getPageable().getPageSize(),
                        companies.getTotalPages()
                ),
                companies.getContent().stream().map(company -> modelMapper.map(company, CompanyDTO.class))
                        .collect(Collectors.toList())
        );
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
    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    @Autowired
    public void setJobApplicationRepository(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }
}
