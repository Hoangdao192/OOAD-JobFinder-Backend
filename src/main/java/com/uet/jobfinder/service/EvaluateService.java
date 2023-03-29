package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Evaluate;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.model.EvaluateModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.EvaluateRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Service
public class EvaluateService {
    private JsonWebTokenProvider jsonWebTokenProvider;

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private EvaluateRepository evaluateRepository;

    public EvaluateService(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    public EvaluateModel createEvaluate(EvaluateModel evaluateModel) {
        Long userId = evaluateModel.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Company company = companyRepository.findById(evaluateModel.getCompanyId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));

        if (evaluateRepository.existsByCompanyAndUser(company, user)) {
            throw new CustomIllegalArgumentException(
                    ServerError.EVALUATE_EXITS
            );
        }

        Evaluate evaluate = Evaluate.builder()
                .user(user)
                .company(company)
                .star(evaluateModel.getStar())
                .build();

        evaluateRepository.save(evaluate);

        return evaluateModel;
    }

    public EvaluateModel updateEvaluate(EvaluateModel evaluateModel, HttpServletRequest httpServletRequest) {
        Company company = companyRepository.findById(evaluateModel.getCompanyId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        User user = userRepository.findById(evaluateModel.getUserId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));


        if (evaluateRepository.existsByCompanyAndUser(company, user)) {
            // If evaluate exits then update
            Evaluate evaluate = evaluateRepository.findByCompanyAndUser(company, user)
                    .orElseThrow(() -> new CustomIllegalArgumentException(
                            ServerError.EVALUATE_NOT_EXITS
                    ));

            evaluate.setStar(evaluateModel.getStar());
            evaluateRepository.save(evaluate);

            return evaluateModel;
        } else {
            throw new CustomIllegalArgumentException(
                    ServerError.EVALUATE_NOT_EXITS
            );
        }

    }

    public EvaluateModel deleteEvaluate(EvaluateModel evaluateModel, HttpServletRequest httpServletRequest) {
        Company company = companyRepository.findById(evaluateModel.getCompanyId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        User user = userRepository.findById(evaluateModel.getUserId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        Evaluate evaluate = evaluateRepository.findByCompanyAndUser(company, user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.EVALUATE_NOT_EXITS
                ));
        evaluateRepository.delete(evaluate);

        return evaluateModel;
    }

    public List<EvaluateModel> getAllEvaluate() {
        List<Evaluate> evaluateList = evaluateRepository.findAll();
        List<EvaluateModel> evaluateModels = new LinkedList<>();

        for (Evaluate evaluate: evaluateList) {
            EvaluateModel evaluateModel = EvaluateModel.builder()
                    .companyId(evaluate.getCompany().getId())
                    .userId(evaluate.getUser().getId())
                    .star(evaluate.getStar())
                    .build();
            evaluateModels.add(evaluateModel);
        }

        return evaluateModels;
    }

    public List<EvaluateModel> getAllEvaluateByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));

        List<Evaluate> evaluateList = evaluateRepository.findAllByCompany(company);
        List<EvaluateModel> evaluateModels = new LinkedList<>();

        for (Evaluate evaluate: evaluateList) {
            EvaluateModel evaluateModel = EvaluateModel.builder()
                    .companyId(evaluate.getCompany().getId())
                    .userId(evaluate.getUser().getId())
                    .star(evaluate.getStar())
                    .build();
            evaluateModels.add(evaluateModel);
        }

        return evaluateModels;
    }

    @Autowired
    public void setJsonWebTokenProvider(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    public void setEvaluateRepository(EvaluateRepository evaluateRepository) {
        this.evaluateRepository = evaluateRepository;
    }

}
