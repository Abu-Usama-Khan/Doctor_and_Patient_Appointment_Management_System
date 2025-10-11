package com.practice.doctor_patient_app.service;

import com.practice.doctor_patient_app.dto.AppointmentSlotRequest;
import com.practice.doctor_patient_app.model.*;
import com.practice.doctor_patient_app.repo.AppointmentRepository;
import com.practice.doctor_patient_app.repo.DoctorRepository;
import com.practice.doctor_patient_app.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;

    public AppointmentService(AppointmentRepository appointmentRepo,
                              DoctorRepository doctorRepo,
                              PatientRepository patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    @Transactional
    public Appointment createSlotForDoctor(Long doctorUserId, AppointmentSlotRequest dto) {
        Doctor doctor = doctorRepo.findById(doctorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        Appointment ap = new Appointment();
        ap.setDoctor(doctor);
        ap.setStartTime(dto.getStartTime());
        ap.setEndTime(dto.getEndTime());
        ap.setStatus(AppointmentStatus.AVAILABLE);
        Appointment saved = appointmentRepo.save(ap);

        doctor.getAppointments().add(saved);
        return saved;
    }

    @Transactional
    public Appointment bookAppointment(Long appointmentId, Long patientUserId) {
        Appointment ap = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (ap.getStatus() != AppointmentStatus.AVAILABLE) {
            throw new IllegalStateException("Appointment not available");
        }

        Patient patient = patientRepo.findById(patientUserId)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));

        ap.setPatient(patient);
        ap.setStatus(AppointmentStatus.BOOKED);
        Appointment savedAppointment = appointmentRepo.save(ap);

        patient.getAppointments().add(savedAppointment);
        return savedAppointment;
    }

}
