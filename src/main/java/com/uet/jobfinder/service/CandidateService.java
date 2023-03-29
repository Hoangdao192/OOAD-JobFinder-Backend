package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.AppFile;
import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.CandidateModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.repository.AddressRepository;
import com.uet.jobfinder.repository.CandidateRepository;
import com.uet.jobfinder.repository.UserRepository;
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
public class CandidateService {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private JsonWebTokenProvider jsonWebTokenProvider;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    public Candidate createEmptyCandidate(User user) {
        Candidate candidate = new Candidate();
        candidate.setUser(user);
        return candidateRepository.save(candidate);
    }

    public Candidate createCandidate(Candidate candidate) {
        candidate.setId(null);
        return candidateRepository.save(candidate);
    }

    public PageQueryModel<CandidateModel> getAllCandidate(
            Integer page, Integer pageSize
    ) {
        Page<Candidate> candidates = candidateRepository.findAll(
                PageRequest.of(page, pageSize)
        );

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        candidates.getPageable().getPageNumber(),
                        candidates.getPageable().getPageSize(),
                        candidates.getTotalPages()
                ),
                candidates.getContent().stream().map(
                        candidate -> modelMapper.map(candidate, CandidateModel.class)
                ).collect(Collectors.toList())
        );
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
    }

    public CandidateModel getCandidateModelById(Long id) {
        Candidate candidate = getCandidateById(id);
        return modelMapper.map(candidate, CandidateModel.class);
    }

    public CandidateModel updateCandidate(CandidateModel candidateModel, HttpServletRequest request) throws IOException {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
        Candidate candidate = getCandidateByUser(user);

        if (candidateModel.getCandidateAvatarFile() != null) {
            AppFile appFile = fileService.saveFile(
                    candidateModel.getCandidateAvatarFile().getOriginalFilename(),
                    candidateModel.getCandidateAvatarFile().getContentType(),
                    candidateModel.getCandidateAvatarFile().getBytes()
            );
            candidate.setAvatar(appFile);
        }

        candidate.setFullName(candidateModel.getFullName());
        candidate.setSex(candidateModel.getSex());
        candidate.setDateOfBirth(candidateModel.getDateOfBirth());
        candidate.setContactEmail(candidateModel.getContactEmail());
        candidate.setPhoneNumber(candidateModel.getPhoneNumber());
        candidate.setSelfDescription(candidateModel.getSelfDescription());

        candidate = candidateRepository.save(candidate);
        return modelMapper.map(candidate, CandidateModel.class);
    }

    public Candidate getCandidateByUser(User user) {
        return candidateRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
    }

    public boolean deleteCandidateById(Long id, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        Candidate candidate = candidateRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));

        //  User is only be deleted by admin or by this user itself
        if (candidate.getId().equals(id) && !userService.isAdmin(id)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        candidateRepository.delete(candidate);
        return true;
    }
}
