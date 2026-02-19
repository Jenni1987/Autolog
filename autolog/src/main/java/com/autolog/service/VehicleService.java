package com.autolog.service;

import com.autolog.model.User;
import com.autolog.model.Vehicle;
import com.autolog.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepo;

    public VehicleService(VehicleRepository vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> getUserVehicles(Integer userId) {
        return vehicleRepo.findByUser_Id(userId);
    }

    public Vehicle getVehicleByIdForUser(Integer vehicleId, Integer userId) {
        return vehicleRepo.findById(vehicleId)
                .filter(v -> v.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
    }

    public Vehicle save(Vehicle vehicle, User user) {
        vehicle.setUser(user);
        return vehicleRepo.save(vehicle);
    }

    public void delete(Integer vehicleId, Integer userId) {
        Vehicle vehicle = getVehicleByIdForUser(vehicleId, userId);
        vehicleRepo.delete(vehicle);
    }
}
