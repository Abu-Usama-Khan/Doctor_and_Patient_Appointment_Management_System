package com.practice.doctor_patient_app.service;

import com.practice.doctor_patient_app.model.Patient;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.repo.PatientRepository;
import com.practice.doctor_patient_app.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepo, UserRepository userRepo,
                          PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Patient createPatientProfile(User user, String medicalHistory) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setMedicalHistory(medicalHistory);
        patient.setAppointments(new ArrayList<>());
        return patientRepo.save(patient);
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepo.findAll();
    }
}
