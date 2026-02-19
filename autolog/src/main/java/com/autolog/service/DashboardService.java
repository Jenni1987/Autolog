package com.autolog.service;

import com.autolog.dto.DashboardDTO;
import com.autolog.model.Operation.OperationType;
import com.autolog.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DashboardService {

    private final VehicleRepository vehicleRepo;
    private final OperationRepository operationRepo;
    private final DocumentRepository documentRepo;

    public DashboardService(VehicleRepository vehicleRepo,
                            OperationRepository operationRepo,
                            DocumentRepository documentRepo) {
        this.vehicleRepo = vehicleRepo;
        this.operationRepo = operationRepo;
        this.documentRepo = documentRepo;
    }

    public DashboardDTO getDashboard(Integer userId) {

        long vehicleCount = vehicleRepo.countByUser_Id(userId);

        long maintenance = operationRepo.countByVehicle_User_IdAndType(userId, OperationType.MANTENIMIENTO);
        long repair = operationRepo.countByVehicle_User_IdAndType(userId, OperationType.REPARACION);
        long inspection = operationRepo.countByVehicle_User_IdAndType(userId, OperationType.INSPECCION);

        BigDecimal annualCost =
                operationRepo.sumAnnualCost(userId, LocalDate.now().getYear());

        long documentCount =
                documentRepo.countByUserId(userId);

        return new DashboardDTO(
                vehicleCount,
                maintenance,
                repair,
                inspection,
                documentCount,
                annualCost,
                operationRepo.findTop5ByVehicle_User_IdOrderByDateDesc(userId)
        );
    }
}
