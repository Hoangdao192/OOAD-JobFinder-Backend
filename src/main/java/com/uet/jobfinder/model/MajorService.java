package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Major;
import com.uet.jobfinder.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    public Major createMajor(Major major) {
        major.setId(null);
        return majorRepository.save(major);
    }

    public List<Major> getAllMajor() {
        return majorRepository.findAll();
    }
}
