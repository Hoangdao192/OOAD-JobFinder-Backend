package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.SavedJob;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.SavedJobModel;
import com.uet.jobfinder.repository.SavedJobRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Service
public class SavedJobService {

    @Autowired
    private SavedJobRepository savedJobRepository;
    @Autowired
    private JobService jobService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JsonWebTokenProvider jsonWebTokenProvider;

    public SavedJobModel candidateSaveJob(SavedJobModel savedJobModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        if (!savedJobModel.getCandidateId().equals(userId)) {
            throw new CustomIllegalArgumentException(ServerError.ACCESS_DENIED);
        }

        Job job = jobService.getJobById(savedJobModel.getJobId());
        Candidate candidate = candidateService.getCandidateById(savedJobModel.getCandidateId());
        if (savedJobRepository.existsByJobAndCandidate(job, candidate)) {
            throw  new CustomIllegalArgumentException(
                    ServerError.JOB_ALREADY_SAVED
            );
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setCandidate(candidate);
        savedJob.setJob(job);
        savedJob = savedJobRepository.save(savedJob);
        return modelMapper.map(savedJob, SavedJobModel.class);
    }

    public boolean deleteSavedJob(Long savedJobId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        SavedJob savedJob = getSavedJobById(savedJobId);
        if (!savedJob.getCandidate().getId().equals(userId)) {
            throw new CustomIllegalArgumentException(ServerError.ACCESS_DENIED);
        }

        savedJobRepository.delete(savedJob);
        return true;
    }

    public PageQueryModel<SavedJobModel> getAllSavedJob(
            Integer page, Integer pageSize,
            Long candidateId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        if (!userId.equals(candidateId)) {
            throw new CustomIllegalArgumentException(ServerError.ACCESS_DENIED);
        }

        Candidate candidate = candidateService.getCandidateById(candidateId);
        Page<SavedJob> savedJobs = savedJobRepository.findAllByCandidate(
                PageRequest.of(page, pageSize),
                candidate
        );

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        savedJobs.getPageable().getPageNumber(),
                        savedJobs.getPageable().getPageSize(),
                        savedJobs.getTotalPages()
                ),
                savedJobs.getContent()
                        .stream()
                        .map(savedJob ->
                                modelMapper.map(savedJob, SavedJobModel.class)).collect(Collectors.toList())
        );
    }

    public SavedJob getSavedJobById(Long savedJobId) {
        return savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.SAVED_JOB_NOT_EXISTS
                ));
    }

}
