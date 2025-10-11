package com.practice.doctor_patient_app.repo;

import com.practice.doctor_patient_app.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, QuerydslPredicateExecutor<Doctor> {
}
