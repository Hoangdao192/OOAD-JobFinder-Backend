package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
