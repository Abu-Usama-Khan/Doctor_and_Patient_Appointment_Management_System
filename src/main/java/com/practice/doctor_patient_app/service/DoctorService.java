package com.practice.doctor_patient_app.service;

import com.practice.doctor_patient_app.model.Doctor;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.repo.DoctorRepository;
import com.practice.doctor_patient_app.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepo, UserRepository userRepo,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Doctor createDoctorProfile(User user, String specialization) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepo.save(user);
        Doctor doctor = new Doctor();
        doctor.setUser(saved);
        doctor.setSpecialization(specialization);
        doctor.setAppointments(new ArrayList<>());
        return doctorRepo.save(doctor);
    }

    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }
}
