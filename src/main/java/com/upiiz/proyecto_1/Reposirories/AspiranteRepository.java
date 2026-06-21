package com.upiiz.proyecto_1.Reposirories;

import com.upiiz.proyecto_1.Entities.AspiranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AspiranteRepository extends JpaRepository<AspiranteEntity, Long>{
    Optional<AspiranteEntity> findByEmail(String email);

}
