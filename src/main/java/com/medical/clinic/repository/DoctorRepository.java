package com.medical.clinic.repository;

import com.medical.clinic.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findByUserId(String userId);

    Boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findByDepartmentId(String departmentId);

    List<Doctor> findByActiveTrue();
}
