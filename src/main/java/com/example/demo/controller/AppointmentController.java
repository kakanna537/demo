package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 提交预约申请
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }

    // 根据房主用户名获取所有预约申请
    @GetMapping("/{username}")
    public List<Appointment> getAppointmentsByUsername(@PathVariable String username) {
        return appointmentService.getAppointmentsByOwnerUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
