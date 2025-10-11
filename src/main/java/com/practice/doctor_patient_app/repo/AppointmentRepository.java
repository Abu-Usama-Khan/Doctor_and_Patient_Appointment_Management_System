package com.practice.doctor_patient_app.repo;

import com.practice.doctor_patient_app.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, QuerydslPredicateExecutor<Appointment> {
}
