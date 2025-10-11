package com.practice.doctor_patient_app.repo;

import com.practice.doctor_patient_app.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PatientRepository extends JpaRepository<Patient, Long>, QuerydslPredicateExecutor<Patient> {
}
