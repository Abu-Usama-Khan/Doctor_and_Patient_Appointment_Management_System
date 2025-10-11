package com.practice.doctor_patient_app.service;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

}
