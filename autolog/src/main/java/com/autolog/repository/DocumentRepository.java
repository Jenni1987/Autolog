package com.autolog.repository;

import com.autolog.model.Document;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("""
           SELECT d FROM Document d
           JOIN FETCH d.operation o
           JOIN FETCH o.vehicle v
           WHERE v.user.id = :userId
           """)
    List<Document> findAllByUserId(@Param("userId") Integer userId);

    @Query("""
           SELECT d FROM Document d
           JOIN FETCH d.operation o
           JOIN FETCH o.vehicle v
           WHERE d.id = :id AND v.user.id = :userId
           """)
    Optional<Document> findByIdAndUserId(@Param("id") Integer id,
                                                @Param("userId") Integer userId);

    @Query("""
           SELECT COUNT(d)
           FROM Document d
           WHERE d.operation.vehicle.user.id = :userId
           """)
    long countByUserId(@Param("userId") Integer userId);
}
