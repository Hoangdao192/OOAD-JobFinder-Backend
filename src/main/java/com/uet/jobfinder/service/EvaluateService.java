package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Evaluate;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.model.EvaluateModel;
import com.uet.jobfinder.model.EvaluateStarModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.EvaluateRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluateService {
    private JsonWebTokenProvider jsonWebTokenProvider;

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private EvaluateRepository evaluateRepository;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ModelMapper modelMapper;

    public EvaluateService(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    public EvaluateModel createEvaluate(EvaluateModel evaluateModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Candidate candidate = candidateService.getCandidateById(evaluateModel.getCandidateId());

        if (!(candidate.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Company company = companyService.getCompanyByUserId(evaluateModel.getCompanyId());

        if (evaluateRepository.existsByCandidateAndCompany(candidate, company)) {
            updateEvaluate(evaluateModel, request);
        }

        Evaluate evaluate = Evaluate.builder()
                .candidate(candidate)
                .company(company)
                .star(evaluateModel.getStar())
                .build();

        evaluate = evaluateRepository.save(evaluate);

        return modelMapper.map(evaluate, EvaluateModel.class);
    }

    public EvaluateModel updateEvaluate(EvaluateModel evaluateModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Candidate candidate = candidateService.getCandidateById(evaluateModel.getCandidateId());
        if (!(candidate.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Company company = companyService.getCompanyByUserId(evaluateModel.getCompanyId());

        Evaluate evaluate = evaluateRepository.findByCandidateAndCompany(candidate, company)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.EVALUATE_NOT_EXITS
                ));
        evaluate.setStar(evaluateModel.getStar());
        evaluate = evaluateRepository.save(evaluate);
        return modelMapper.map(evaluate, EvaluateModel.class);
    }

    public Boolean deleteEvaluate(EvaluateModel evaluateModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Candidate candidate = candidateService.getCandidateById(evaluateModel.getCandidateId());
        Company company = companyService.getCompanyByUserId(evaluateModel.getCompanyId());

        if (!(candidate.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Evaluate evaluate = evaluateRepository.findByCandidateAndCompany(candidate, company)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.EVALUATE_NOT_EXITS
                ));

        evaluateRepository.delete(evaluate);
        return true;
    }

    public List<EvaluateModel> getAllEvaluate() {
        List<Evaluate> evaluateList = evaluateRepository.findAll();
        return evaluateList.stream().map(evaluate ->
                modelMapper.map(evaluate, EvaluateModel.class)).collect(Collectors.toList());
    }

    public List<EvaluateModel> getAllEvaluateByCompany(
            Long companyId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(companyId);

        if (!(company.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        List<Evaluate> evaluateList = evaluateRepository.findAllByCompany(company);

        return evaluateList.stream().map(evaluate ->
                modelMapper.map(evaluate, EvaluateModel.class)).collect(Collectors.toList());
    }

    public EvaluateStarModel getCompanyTotalEvaluate(Long companyId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(companyId);

        if (!(company.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Double averageEvaluate = evaluateRepository.getAverageEvaluateByCompanyId(company.getId());
        //  Not have any review yet
        if (averageEvaluate == null) {
            averageEvaluate = 0d;
        }
        return EvaluateStarModel.builder()
                .companyId(companyId)
                .company(modelMapper.map(company, CompanyModel.class))
                .star(averageEvaluate)
                .build();
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
