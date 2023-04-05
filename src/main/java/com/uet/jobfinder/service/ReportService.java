package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Report;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.ReportModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.ReportRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CandidateService candidateService;
    private JsonWebTokenProvider jsonWebTokenProvider;
    @Autowired
    private ModelMapper modelMapper;

    public ReportModel createReport(ReportModel reportModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Candidate candidate = candidateService.getCandidateById(reportModel.getCandidateId());
        if (!(candidate.getUser().getId().equals(userId))) {
            throw new CustomIllegalArgumentException(ServerError.ACCESS_DENIED);
        }

        Company company = companyService.getCompanyByUserId(reportModel.getCompanyId());

        Report report = Report.builder()
                .company(company)
                .candidate(candidate)
                .date(new Date())
                .message(reportModel.getMessage())
                .build();
        report = reportRepository.save(report);

        return modelMapper.map(report, ReportModel.class);
    }

    public PageQueryModel<ReportModel> getAllReport(Integer page, Integer pageSize) {
        Page<Report> reports = reportRepository.findAll(
                PageRequest.of(page, pageSize)
        );
        return new PageQueryModel<>(
                new PageQueryModel.PageModel(reports),
                reports.getContent().stream()
                        .map(report -> modelMapper.map(report, ReportModel.class))
                        .collect(Collectors.toList())
        );
    }

    public PageQueryModel<ReportModel> reportPageToPageQueryModel(Page<Report> page) {
        return new PageQueryModel<>(
                new PageQueryModel.PageModel(page),
                page.getContent().stream()
                        .map(report -> modelMapper.map(report, ReportModel.class))
                        .collect(Collectors.toList())
        );
    }

    public PageQueryModel<ReportModel> getAllReportByCompany(
            Integer page, Integer pageSize, Long companyId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
        if (!(company.getId().equals(companyId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return reportPageToPageQueryModel(
                reportRepository.findAllByCompany(
                        PageRequest.of(page, pageSize),
                        company
                )
        );
    }

    public PageQueryModel<ReportModel> getAllReportByCandidate(
            Integer page, Integer pageSize, Long candidateId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Candidate candidate = candidateService.getCandidateById(userId);
        if (!(candidate.getId().equals(candidateId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return reportPageToPageQueryModel(
                reportRepository.findAllByCandidate(
                        PageRequest.of(page, pageSize),
                        candidate
                )
        );
    }

    public ReportModel getReportById(Long id, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Report report = reportRepository.findById(id)
                .orElseThrow(
                        () -> new CustomIllegalArgumentException(
                                ServerError.REPORT_NOT_EXISTED
                        )
                );

        //  Chỉ có admin, người báo cáo và người bị báo cáo mới có quyền xem báo cáo với id này
        if (!(report.getCandidate().getId().equals(userId) || report.getCompany().getId().equals(userId)
                || userService.isAdmin(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return modelMapper.map(report, ReportModel.class);
    }

    public Pair<String, Boolean> deleteReportById(
            Long id, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Report report = reportRepository.findById(id)
                .orElseThrow(
                        () -> new CustomIllegalArgumentException(
                                ServerError.REPORT_NOT_EXISTED
                        )
                );

        //  Chỉ có admin, người báo cáo mới có quyền xóa báo cáo với id này
        if (!(report.getCandidate().getId().equals(userId) || userService.isAdmin(userId))) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return Pair.of("success", true);
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
