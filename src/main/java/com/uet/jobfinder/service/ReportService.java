package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Report;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.ReportModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.ReportRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private JsonWebTokenProvider jsonWebTokenProvider;

    public ReportModel createReport(ReportModel reportModel) {
        User user = userRepository.findById(reportModel.getUserId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Company company = companyRepository.findById(reportModel.getCompanyId())
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));

        Report report = Report.builder()
                .company(company)
                .user(user)
                .date(new Date())
                .message(reportModel.getMessage())
                .build();

        reportModel.setDate(report.getDate());

        reportRepository.save(report);

        return reportModel;
    }

    public List<ReportModel> getAllReport() {
        List<Report> reportList = reportRepository.findAll();
        List<ReportModel> reportModelList = new ArrayList<ReportModel>();

        for (Report report: reportList) {
            ReportModel reportModel = ReportModel.builder()
                    .companyId(report.getCompany().getId())
                    .userId(report.getUser().getId())
                    .date(report.getDate())
                    .message(report.getMessage())
                    .build();

            reportModelList.add(reportModel);
        }

        return reportModelList;
    }

    public ReportModel getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(
                        () -> new CustomIllegalArgumentException(
                                ServerError.REPORT_NOT_EXISTED
                        )
                );

        ReportModel reportModel = ReportModel.builder()
                .companyId(report.getCompany().getId())
                .userId(report.getUser().getId())
                .date(report.getDate())
                .message(report.getMessage())
                .build();

        return reportModel;
    }

    public Pair<String, ReportModel> deleteReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(
                        () -> new CustomIllegalArgumentException(
                                ServerError.REPORT_NOT_EXISTED
                        )
                );
        ReportModel reportModel = ReportModel.builder()
                .userId(report.getUser().getId())
                .companyId(report.getCompany().getId())
                .date(report.getDate())
                .message(report.getMessage())
                .build();

        return Pair.of("Deleted report successfully",
                reportModel);
    }

    public List<ReportModel> getAllReportByCompanyOrCandidate(HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
        List<Report> reportList = reportRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.REPORT_NOT_EXISTED
                ));

        List<ReportModel> reportModelList = new ArrayList<ReportModel>();

        for (Report report: reportList) {
            ReportModel reportModel = ReportModel.builder()
                    .companyId(report.getCompany().getId())
                    .userId(report.getUser().getId())
                    .date(report.getDate())
                    .message(report.getMessage())
                    .build();

            reportModelList.add(reportModel);
        }

        return reportModelList;
    }

    @Autowired
    public void setReportRepository(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
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
    public void setJsonWebTokenProvider(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }
}
