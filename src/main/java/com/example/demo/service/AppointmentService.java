package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Appointment;
import com.example.demo.model.House;
import com.example.demo.model.Owner;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.HouseRepository;
import com.example.demo.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public Appointment saveAppointment(Appointment appointment) {
        House house = houseRepository.findById(appointment.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        Owner owner = ownerRepository.findByUsername(house.getUsername());
        appointment.setOwner(owner);
        appointment.setHouseTitle(house.getTitle());
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByOwnerUsername(String username) {
        return appointmentRepository.findByOwnerUsername(username);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
