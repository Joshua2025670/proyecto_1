package com.upiiz.proyecto_1.Reposirories;

import com.upiiz.proyecto_1.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByNombre(String nombre);
}
