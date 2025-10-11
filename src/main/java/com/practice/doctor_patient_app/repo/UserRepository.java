package com.practice.doctor_patient_app.repo;

import com.practice.doctor_patient_app.model.Doctor;
import com.practice.doctor_patient_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
