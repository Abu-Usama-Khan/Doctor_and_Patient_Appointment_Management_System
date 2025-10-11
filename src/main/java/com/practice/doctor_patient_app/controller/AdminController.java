package com.practice.doctor_patient_app.controller;

import com.practice.doctor_patient_app.model.QDoctor;
import com.practice.doctor_patient_app.model.QUser;
import com.practice.doctor_patient_app.model.Role;
import com.practice.doctor_patient_app.model.Doctor;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.repo.DoctorRepository;
import com.practice.doctor_patient_app.repo.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;
    private final DoctorRepository doctorRepo;

    public AdminController(UserRepository userRepo, DoctorRepository doctorRepo) {
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        QUser u = QUser.user;
        BooleanBuilder where = new BooleanBuilder();

        if (role != null && !role.isBlank()) {
                where.and(u.role.eq(Role.valueOf(role.toUpperCase())));
        }
        if (q != null && !q.isBlank()) {
            where.and(u.name.containsIgnoreCase(q).or(u.email.containsIgnoreCase(q)));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> result = userRepo.findAll(where, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/doctors")
    public ResponseEntity<Page<Doctor>> searchDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        QDoctor d = QDoctor.doctor;
        BooleanBuilder where = new BooleanBuilder();

        if (specialization != null && !specialization.isBlank()) {
            where.and(d.specialization.containsIgnoreCase(specialization));
        }
        if (q != null && !q.isBlank()) {
            where.and(d.user().name.containsIgnoreCase(q).or(d.user().email.containsIgnoreCase(q)));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("user.createdAt").descending());
        Page<Doctor> result = doctorRepo.findAll(where, pageable);
        return ResponseEntity.ok(result);
    }
}
