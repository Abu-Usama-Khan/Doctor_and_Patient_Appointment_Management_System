package com.practice.doctor_patient_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
