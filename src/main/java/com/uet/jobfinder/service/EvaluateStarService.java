package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Evaluate;
import com.uet.jobfinder.entity.EvaluateStar;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.EvaluateModel;
import com.uet.jobfinder.model.EvaluateStarModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.EvaluateRepository;
import com.uet.jobfinder.repository.EvaluateStarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluateStarService {
    private EvaluateRepository evaluateRepository;
    private EvaluateStarRepository evaluateStarRepository;
    private CompanyRepository companyRepository;

    public EvaluateStarModel updateEvaluateStar(EvaluateModel evaluateModel) {
        Company company = companyRepository.findById(evaluateModel.getCompanyId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));

        if (evaluateStarRepository.existsById(company.getId())) {

            // If exist evaluate with company which user evaluated

            EvaluateStar evaluateStar = evaluateStarRepository.findByCompany(company)
                    .orElseThrow(() -> new CustomIllegalArgumentException(
                            ServerError.EVALUATE_NOT_EXITS
                    ));
            List<Evaluate> evaluateList = evaluateRepository.findAllByCompany(company);
            long sum = 0;
            for (Evaluate e : evaluateList) {
                sum += e.getStar();
            }

            System.out.println("Sum -----------------------------------: " + sum);

            int numEvaluate = evaluateList.size();

            evaluateStar.setStar(sum * 1.0 / numEvaluate);
            evaluateStar.setNumEvaluate(numEvaluate);

            evaluateStarRepository.save(evaluateStar);

            System.out.println("update evaluate");

            return EvaluateStarModel.builder()
                    .companyId(evaluateModel.getCompanyId())
                    .star(evaluateStar.getStar())
                    .build();
        } else {

            // If not exist evaluate with company which user evaluated

            EvaluateStar evaluateStar = EvaluateStar.builder()
                    .company(company)
                    .star(evaluateModel.getStar().doubleValue())
                    .numEvaluate(1)
                    .build();

            evaluateStarRepository.save(evaluateStar);

            System.out.println("create evaluate");
            return EvaluateStarModel.builder()
                    .companyId(evaluateModel.getCompanyId())
                    .star(evaluateStar.getStar())
                    .build();
        }


    }

    public EvaluateStarModel updateEvaluateStar(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));

        EvaluateStar evaluateStar = evaluateStarRepository.findByCompany(company)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.EVALUATE_NOT_EXITS
                ));
        List<Evaluate> evaluateList = evaluateRepository.findAllByCompany(company);
        long sum = 0;
        for (Evaluate e : evaluateList) {
            sum += e.getStar();
        }

        System.out.println("Sum -----------------------------------: " + sum);

        int numEvaluate = evaluateList.size();

        if (numEvaluate == 0) {
            return EvaluateStarModel.builder().build();
        }

        evaluateStar.setStar(sum * 1.0 / numEvaluate);
        evaluateStar.setNumEvaluate(numEvaluate);

        evaluateStarRepository.save(evaluateStar);

        System.out.println("update evaluate");

        return EvaluateStarModel.builder()
                .companyId(evaluateStar.getCompany().getId())
                .star(evaluateStar.getStar())
                .build();
    }

    @Autowired
    public void setEvaluateRepository(EvaluateRepository evaluateRepository) {
        this.evaluateRepository = evaluateRepository;
    }

    @Autowired
    public void setEvaluateStarRepository(EvaluateStarRepository evaluateStarRepository) {
        this.evaluateStarRepository = evaluateStarRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

   
}
