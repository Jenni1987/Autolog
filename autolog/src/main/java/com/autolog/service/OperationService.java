package com.autolog.service;

import com.autolog.model.Operation;
import com.autolog.model.Vehicle;
import com.autolog.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {

    private final OperationRepository operationRepo;
    private final VehicleService vehicleService;

    public OperationService(OperationRepository operationRepo,
                            VehicleService vehicleService) {
        this.operationRepo = operationRepo;
        this.vehicleService = vehicleService;
    }

    public List<Operation> getOperationsForUser(Integer userId) {
        return operationRepo.findAll().stream()
                .filter(o -> o.getVehicle().getUser().getId().equals(userId))
                .toList();
    }

    public Operation getOperationForUser(Integer operationId, Integer userId) {
        return operationRepo.findById(operationId)
                .filter(o -> o.getVehicle().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Operación no encontrada"));
    }

    public Operation save(Operation operation,
                                 Integer vehicleId,
                                 Integer userId) {

        Vehicle vehicle =
                vehicleService.getVehicleByIdForUser(vehicleId, userId);

        operation.setVehicle(vehicle);

        return operationRepo.save(operation);
    }

    public void delete(Integer operationId, Integer userId) {
        Operation operation =
                getOperationForUser(operationId, userId);

        operationRepo.delete(operation);
    }
}
