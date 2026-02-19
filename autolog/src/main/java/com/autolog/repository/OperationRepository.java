package com.autolog.repository;

import com.autolog.model.Operation;
import com.autolog.model.Operation.OperationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    long countByVehicle_User_IdAndType(Integer userId, OperationType type);

    List<Operation> findTop5ByVehicle_User_IdOrderByDateDesc(Integer userId);

    @Query("""
           SELECT COALESCE(SUM(o.cost),0)
           FROM Operation o
           WHERE o.vehicle.user.id = :userId
           AND YEAR(o.date) = :year
           """)
    BigDecimal sumAnnualCost(@Param("userId") Integer userId,
                             @Param("year") int year);
}
