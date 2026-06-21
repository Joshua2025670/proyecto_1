package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.AspiranteEntity;

import java.util.List;
import java.util.Optional;

public interface AspiranteServ {
    List<AspiranteEntity> listadoAsp();
    AspiranteEntity crearAsp(AspiranteEntity aspirante);
    AspiranteEntity editarAsp(AspiranteEntity aspirante, Long id);
    void eliminarAsp(Long id);
    Optional<AspiranteEntity> buscarAsp(Long id);
}
