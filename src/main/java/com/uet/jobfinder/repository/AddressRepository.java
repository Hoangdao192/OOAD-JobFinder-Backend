package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findById(int id);
}
