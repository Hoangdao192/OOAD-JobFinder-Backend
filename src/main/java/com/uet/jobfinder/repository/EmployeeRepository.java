package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
