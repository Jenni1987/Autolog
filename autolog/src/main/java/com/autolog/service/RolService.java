package com.autolog.service;

import com.autolog.model.Rol;
import com.autolog.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepo;

    public RolService(RolRepository rolRepo) {
        this.rolRepo = rolRepo;
    }

    public List<Rol> findAllActive() {
        return rolRepo.findAll().stream()
                .filter(rol -> Boolean.TRUE.equals(rol.getActiveRow()))
                .collect(Collectors.toList());
    }

    public Set<Rol> findByIds(Set<Integer> ids) {
        return rolRepo.findAllById(ids).stream().collect(Collectors.toSet());
    }
}
