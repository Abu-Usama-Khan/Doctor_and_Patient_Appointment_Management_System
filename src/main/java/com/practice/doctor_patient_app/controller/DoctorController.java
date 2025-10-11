package com.practice.doctor_patient_app.controller;

import com.practice.doctor_patient_app.dto.CreateDoctorRequest;
import com.practice.doctor_patient_app.model.Appointment;
import com.practice.doctor_patient_app.model.Doctor;
import com.practice.doctor_patient_app.model.Role;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.service.AppointmentService;
import com.practice.doctor_patient_app.service.DoctorService;
import com.practice.doctor_patient_app.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    ModelMapper modelMapper;

    public DoctorController(DoctorService doctorService,
                            AppointmentService appointmentService,
                            UserService userService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createDoctorProfile(@Valid @RequestBody CreateDoctorRequest req) {
        User u = modelMapper.map(req, User.class);
        u.setRole(Role.DOCTOR);
        Doctor doctor = doctorService.createDoctorProfile(u, req.getSpecialization());
        return ResponseEntity.ok(doctor.getId());
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/me/appointments")
    public ResponseEntity<List<Appointment>> myAppointments(Authentication auth) {
        String email = auth.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();
        Doctor doc = doctorService.findAll().stream()
                .filter(d -> d.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));
        return ResponseEntity.ok(doc.getAppointments());
    }
}
