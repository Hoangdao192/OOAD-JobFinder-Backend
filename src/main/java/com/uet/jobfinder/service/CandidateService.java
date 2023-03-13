package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.model.CandidateContext;
import com.uet.jobfinder.model.CandidateModel;
import com.uet.jobfinder.repository.AddressRepository;
import com.uet.jobfinder.repository.CandidateRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    public CandidateContext createCandidate(Long id, CandidateModel candidateModel, AddressModel addressModel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        Address address = Address.builder()
                .province(addressModel.getProvince())
                .district(addressModel.getDistrict())
                .ward(addressModel.getWard())
                .detailAddress(addressModel.getDetailAddress())
                .latitude(addressModel.getLatitude())
                .longitude(addressModel.getLongitude())
                .build();

        Candidate candidate = Candidate.builder()
                .user(user)
                .fullName(candidateModel.getFullName())
                .sex(candidateModel.getSex())
                .address(address)
                .dateOfBirth(candidateModel.getDateOfBirth())
                .contactEmail(candidateModel.getContactEmail())
                .phoneNumber(candidateModel.getPhoneNumber())
                .selfDescription(candidateModel.getSelfDescription())
                .experience(candidateModel.getExperience())
                .education(candidateModel.getEducation())
                .build();

        candidateRepository.save(candidate);

        return CandidateContext.builder()
                .candidateModel(candidateModel)
                .addressModel(addressModel)
                .build();
    }

    public List<CandidateContext> getAllCandidate() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<CandidateContext> candidateContexts = new ArrayList<>();

        for (Candidate candidate : candidates) {
            Address address = addressRepository.findById(candidate.getAddress().getId())
                    .orElseThrow(() -> new InvalidPathException("../api/candidate/id", "Địa chỉ không tồn tại."));
            AddressModel addressModel = AddressModel.builder()
                    .province(address.getProvince())
                    .district(address.getDistrict())
                    .ward(address.getWard())
                    .detailAddress(address.getDetailAddress())
                    .longitude(address.getLongitude())
                    .latitude(address.getLatitude())
                    .build();

            CandidateModel candidateModel = CandidateModel.builder()
                    .fullName(candidate.getFullName())
                    .sex(candidate.getSex())
                    .dateOfBirth(candidate.getDateOfBirth())
                    .contactEmail(candidate.getContactEmail())
                    .phoneNumber(candidate.getPhoneNumber())
                    .selfDescription(candidate.getSelfDescription())
                    .experience(candidate.getExperience())
                    .education(candidate.getEducation())
                    .build();

            CandidateContext candidateContext = CandidateContext.builder()
                    .addressModel(addressModel)
                    .candidateModel(candidateModel)
                    .build();

            candidateContexts.add(candidateContext);
        }
        return candidateContexts;
    }

    public CandidateContext getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/candidate/id", "Ứng viên không tồn tại"));

        Address address = candidate.getAddress();
        AddressModel addressModel = AddressModel.builder()
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .detailAddress(address.getDetailAddress())
                .longitude(address.getLongitude())
                .latitude(address.getLatitude())
                .build();

        CandidateModel candidateModel = CandidateModel.builder()
                .fullName(candidate.getFullName())
                .sex(candidate.getSex())
                .dateOfBirth(candidate.getDateOfBirth())
                .contactEmail(candidate.getContactEmail())
                .phoneNumber(candidate.getPhoneNumber())
                .selfDescription(candidate.getSelfDescription())
                .experience(candidate.getExperience())
                .education(candidate.getEducation())
                .build();

        CandidateContext candidateContext = CandidateContext.builder()
                .addressModel(addressModel)
                .candidateModel(candidateModel)
                .build();

        return candidateContext;

    }

    public CandidateContext putCandidateById(Long id, CandidateContext candidateContext) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/candidate/id", "Ứng viên không tồn tại"));

        CandidateModel candidateModel = candidateContext.getCandidateModel();
        AddressModel addressModel = candidateContext.getAddressModel();

        Address address = candidate.getAddress();

        address.setProvince(addressModel.getProvince());
        address.setDistrict(addressModel.getDistrict());
        address.setWard(addressModel.getWard());
        address.setDetailAddress(addressModel.getDetailAddress());
        address.setLongitude(addressModel.getLongitude());
        address.setLatitude(addressModel.getLatitude());



        candidate.setFullName(candidateModel.getFullName());
        candidate.setSex(candidateModel.getSex());
        candidate.setDateOfBirth(candidateModel.getDateOfBirth());
        candidate.setContactEmail(candidateModel.getContactEmail());
        candidate.setPhoneNumber(candidateModel.getPhoneNumber());
        candidate.setSelfDescription(candidateModel.getSelfDescription());
        candidate.setExperience(candidateModel.getExperience());
        candidate.setEducation(candidateModel.getEducation());

        candidateRepository.save(candidate);

        return candidateContext;
    }

    public String deleteCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new InvalidPathException("../api/candidate/id", "Ứng viên không tồn tại"));

        candidateRepository.delete(candidate);

        return Long.toString(candidateRepository.count());
    }
}
