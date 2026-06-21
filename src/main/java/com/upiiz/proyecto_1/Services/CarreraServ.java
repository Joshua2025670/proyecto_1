package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.CarreraEntity;

import java.util.List;
import java.util.Optional;

public interface CarreraServ {
    List<CarreraEntity> listarCarr();
    CarreraEntity crearCarr(CarreraEntity carrera);
    CarreraEntity editarCarr(CarreraEntity carrera, Long id);
    void eliminarCarr(Long id);
    Optional<CarreraEntity> bucarCarr(Long id);
}
