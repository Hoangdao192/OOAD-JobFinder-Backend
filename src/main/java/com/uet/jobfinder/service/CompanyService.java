package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.AppFile;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.SearchCompanyModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.EvaluateStarRepository;
import com.uet.jobfinder.repository.JobApplicationRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private EvaluateStarRepository evaluateStarRepository;

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

    public PageQueryModel<CompanyModel> getAllCompany(Integer page, Integer pageSize) {
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
                        .map(company -> modelMapper.map(company, CompanyModel.class))
                        .collect(Collectors.toList())
        );
    }

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

        if (companyModel.getCompanyLogoFile() == null && company.getCompanyLogo() == null) {
            throw new CustomIllegalArgumentException(
                    ServerError.NULL_COMPANY_LOGO
            );
        }

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

    public CompanyModel getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        return modelMapper.map(company, CompanyModel.class);
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

    public PageQueryModel findCompany(SearchCompanyModel searchCompanyModel, Integer page, Integer pageSize) {
        List<Company> companyList = companyRepository.searchAllCompany(searchCompanyModel.getSearch());
        companyList.removeIf(company -> company.getEvaluateStar().getStar() < searchCompanyModel.getStar());

        List<CompanyModel> companyModels = new ArrayList<>();

        // Nếu limit > độ dài kết quả thì limit bằng độ dài của kết quả
        // limit, start để giới hạn lại khi chuyển model
        int limit = (page + 1) * pageSize > companyList.size() ? companyList.size() : (page + 1) * pageSize;


        int start = page * pageSize;
        int totalPage = companyList.size() / pageSize + 1;

        // Nếu cố tình truy cập page không hợp lệ.
        if (page > totalPage)
            throw new CustomIllegalArgumentException(
                    ServerError.INVALID_REQUEST
            );

        for (int i = start; i < limit; i++) {
            Company company = companyList.get(i);

            CompanyModel companyModel = CompanyModel.builder()
                    .companyName(company.getCompanyName())
                    .companyDescription(company.getCompanyDescription())
                    .numberOfEmployee(company.getNumberOfEmployee())
                    .build();

            companyModels.add(companyModel);
        }

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        page,
                        pageSize,
                        totalPage
                ),
                companyModels
        );
    }

    @Autowired
    public void setEvaluateStarRepository(EvaluateStarRepository evaluateStarRepository) {
        this.evaluateStarRepository = evaluateStarRepository;
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
