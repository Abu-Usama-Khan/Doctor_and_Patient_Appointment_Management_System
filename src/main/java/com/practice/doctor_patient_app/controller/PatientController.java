package com.practice.doctor_patient_app.controller;

import com.practice.doctor_patient_app.dto.CreatePatientRequest;
import com.practice.doctor_patient_app.model.Appointment;
import com.practice.doctor_patient_app.model.Patient;
import com.practice.doctor_patient_app.model.Role;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.service.AppointmentService;
import com.practice.doctor_patient_app.service.PatientService;
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
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    ModelMapper modelMapper;

    public PatientController(PatientService patientService,
                             AppointmentService appointmentService,
                             UserService userService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody CreatePatientRequest req) {
        User u = modelMapper.map(req, User.class);
        u.setRole(Role.PATIENT);

        Patient p = patientService.createPatientProfile(u, req.getMedicalHistory());
        return ResponseEntity.ok(p.getId());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/appointments")
    public ResponseEntity<List<Appointment>> myAppointments(Authentication auth) {
        String email = auth.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();
        Patient p = patientService.findAll().stream()
                .filter(pt -> pt.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return ResponseEntity.ok(p.getAppointments());
    }
}
