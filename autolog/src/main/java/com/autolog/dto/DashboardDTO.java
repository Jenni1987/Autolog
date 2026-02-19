package com.autolog.dto;

import java.math.BigDecimal;
import java.util.List;
import com.autolog.model.Operation;

public class DashboardDTO {

    private long vehicleCount;
    private long maintenanceCount;
    private long repairCount;
    private long inspectionCount;
    private long documentCount;
    private BigDecimal annualCost;
    private List<Operation> recentOperations;

    public DashboardDTO(long vehicleCount,
                        long maintenanceCount,
                        long repairCount,
                        long inspectionCount,
                        long documentCount,
                        BigDecimal annualCost,
                        List<Operation> recentOperations) {

        this.vehicleCount = vehicleCount;
        this.maintenanceCount = maintenanceCount;
        this.repairCount = repairCount;
        this.inspectionCount = inspectionCount;
        this.documentCount = documentCount;
        this.annualCost = annualCost;
        this.recentOperations = recentOperations;
    }

	public long getVehicleCount() {
		return vehicleCount;
	}

	public long getMaintenanceCount() {
		return maintenanceCount;
	}

	public long getRepairCount() {
		return repairCount;
	}

	public long getInspectionCount() {
		return inspectionCount;
	}

	public long getDocumentCount() {
		return documentCount;
	}

	public BigDecimal getAnnualCost() {
		return annualCost;
	}

	public List<Operation> getRecentOperations() {
		return recentOperations;
	}

    
}
