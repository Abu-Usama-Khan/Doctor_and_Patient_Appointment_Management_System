package com.practice.doctor_patient_app.controller;

import com.practice.doctor_patient_app.dto.AppointmentSlotRequest;
import com.practice.doctor_patient_app.model.Appointment;
import com.practice.doctor_patient_app.model.User;
import com.practice.doctor_patient_app.service.AppointmentService;
import com.practice.doctor_patient_app.service.DoctorService;
import com.practice.doctor_patient_app.service.PatientService;
import com.practice.doctor_patient_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentController(AppointmentService appointmentService,
                                 UserService userService,
                                 DoctorService doctorService,
                                 PatientService patientService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/me/slots")
    public ResponseEntity<?> createMySlot(@Valid @RequestBody AppointmentSlotRequest req, Authentication auth) {
        String email = auth.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        var dto = new AppointmentSlotRequest(req.getStartTime(), req.getEndTime());
        Appointment saved = appointmentService.createSlotForDoctor(userId, dto);
        return ResponseEntity.ok(saved.getId());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/{appointmentId}/book")
    public ResponseEntity<?> book(@PathVariable Long appointmentId, Authentication auth) {
        String email = auth.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        Appointment saved = appointmentService.bookAppointment(appointmentId, userId);
        return ResponseEntity.ok(saved.getId());
    }
}
