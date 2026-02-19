package com.autolog.repository;

import com.autolog.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByUser_Id(Integer userId);

    long countByUser_Id(Integer userId);
}
