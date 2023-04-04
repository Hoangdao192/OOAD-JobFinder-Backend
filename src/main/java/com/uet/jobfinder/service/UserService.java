package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.model.UserModel;
import com.uet.jobfinder.presentation.UserMonthStatisticPresentation;
import com.uet.jobfinder.presentation.UserYearStatisticPresentation;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PageQueryModel<UserModel> searchUser(
        Integer page, Integer pageSize,
        String email, String accountType,
        Boolean isActive, Boolean isLocked
    ) {
        Long roleId = null;
        if (accountType != null) {
            roleId = roleRepository.findByName(accountType)
                .orElseThrow(() -> new CustomIllegalArgumentException(ServerError.INVALID_ROLE))
                .getId();
        }

        Page<User> users = userRepository.searchUser(
                PageRequest.of(page, pageSize),
                email, roleId, isActive, isLocked
        );
        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        users.getPageable().getPageNumber(),
                        users.getPageable().getPageSize(),
                        users.getTotalPages(),
                        users.getTotalElements()
                ),
                users.getContent().stream()
                        .map(user -> modelMapper.map(user, UserModel.class))
                        .collect(Collectors.toList())
        );
    }

    public PageQueryModel<UserModel> getAllUser(Integer page, Integer pageSize) {
        Page<User> users = userRepository.findAll(
                PageRequest.of(page, pageSize)
        );
        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                    users.getPageable().getPageNumber(),
                    users.getPageable().getPageSize(),
                    users.getTotalPages(),
                    users.getTotalElements()
                ),
                users.getContent().stream()
                        .map(user -> modelMapper.map(user, UserModel.class))
                        .collect(Collectors.toList())
        );
    }

    public Map<String, Object> getUserNumberStatistic() {
        Long numberOfCandidate = userRepository.countAllByRole(UserType.CANDIDATE);
        Long numberOfCompany = userRepository.countAllByRole(UserType.COMPANY);
        return Map.of("candidate", numberOfCandidate, "company", numberOfCompany);
    }

    public Map<String, Object> getUserGrowthStatistic(Integer month, Integer year) {
        //  Statistic by year
        if (month == 0) {
            return getUserGrowthYearStatistic(year);
        }
        return getUserGrowthMonthStatistic(month, year);
    }

    public Map<String, Object> getUserGrowthMonthStatistic(Integer month, Integer year) {
        List<UserMonthStatisticPresentation> statistic = userRepository.getUserMonthGrowthStatistic(
                month, year
        );

        List<Object> companyUserStatistic = new ArrayList<>();
        List<Object> candidateUserStatistic = new ArrayList<>();
        for (int i = 1; i <= 31; ++i) {
            int mDay = i;
            companyUserStatistic.add(
                    new Object() {
                        public Integer day = mDay;
                        public Long numberOfUser = 0L;
                    }
            );
            candidateUserStatistic.add(
                    new Object() {
                        public Integer day = mDay;
                        public Long numberOfUser = 0L;
                    }
            );
        }
        for (UserMonthStatisticPresentation statisticPresentation : statistic) {
            if (statisticPresentation.getRole().equals(UserType.COMPANY)) {
                companyUserStatistic.set(
                        statisticPresentation.getDay() - 1,
                        new Object() {
                            public Integer month = statisticPresentation.getDay();
                            public Long numberOfUser = statisticPresentation.getNumberOfUser();
                        }
                );
            } else if (statisticPresentation.getRole().equals(UserType.CANDIDATE)) {
                candidateUserStatistic.set(
                        statisticPresentation.getDay() - 1,
                        new Object() {
                            public Integer month = statisticPresentation.getDay();
                            public Long numberOfUser = statisticPresentation.getNumberOfUser();
                        }
                );
            }
        }
        return Map.of("company", companyUserStatistic, "candidate", candidateUserStatistic);
    }

    public Map<String, Object> getUserGrowthYearStatistic(Integer year) {
        List<UserYearStatisticPresentation> statistic =
                userRepository.getUserYearGrowthStatistic(year);

        List<Object> companyUserStatistic = new ArrayList<>();
        List<Object> candidateUserStatistic = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            int mMonth = i;
            companyUserStatistic.add(
                    new Object() {
                        public Integer month = mMonth;
                        public Long numberOfUser = 0L;
                    }
            );
            candidateUserStatistic.add(
                    new Object() {
                        public Integer month = mMonth;
                        public Long numberOfUser = 0L;
                    }
            );
        }
        for (UserYearStatisticPresentation statisticPresentation : statistic) {
            if (statisticPresentation.getRole().equals(UserType.COMPANY)) {
                companyUserStatistic.set(
                        statisticPresentation.getMonth() - 1,
                        new Object() {
                            public Integer month = statisticPresentation.getMonth();
                            public Long numberOfUser = statisticPresentation.getNumberOfUser();
                        }
                );
            } else if (statisticPresentation.getRole().equals(UserType.CANDIDATE)) {
                candidateUserStatistic.set(
                        statisticPresentation.getMonth() - 1,
                        new Object() {
                            public Integer month = statisticPresentation.getMonth();
                            public Long numberOfUser = statisticPresentation.getNumberOfUser();
                        }
                );
            }
        }
        return Map.of("company", companyUserStatistic, "candidate", candidateUserStatistic);
    }

    public User createUser(RegisterRequestModel registerRequestModel) {
        if (userRepository.findByEmail(registerRequestModel.getEmail())
                .isPresent()) {
            throw new CustomIllegalArgumentException(ServerError.EMAIL_HAS_BEEN_USED);
        }

        User user = new User(
                registerRequestModel.getEmail(),
                passwordEncoder.encode(registerRequestModel.getPassword())
        );

        user.addRole(
                roleRepository.findByName(registerRequestModel.getRole())
                        .orElseThrow(() ->
                                new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE))
        );

        user.setCreateDate(LocalDate.now());

        return userRepository.saveAndFlush(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomIllegalArgumentException(ServerError.EMAIL_NOT_EXISTS));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
    }

    public User enableUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public boolean isAdmin(Long id) {
        User user = getUserById(id);
        return user
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(UserType.ADMIN));
    }

    public boolean isCandidate(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY));
    }

    public boolean isCompany(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY));
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
