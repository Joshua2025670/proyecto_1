package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.AspiranteEntity;
import com.upiiz.proyecto_1.Entities.CarreraEntity;
import com.upiiz.proyecto_1.Reposirories.AspiranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AspiranteServImp implements AspiranteServ{
    @Autowired
    private AspiranteRepository aspiranteRepository;
    @Autowired
    private CarreraServImp carreraServImp;

    @Override
    public List<AspiranteEntity> listadoAsp() {
        return aspiranteRepository.findAll();
    }

    @Override
    public AspiranteEntity crearAsp(AspiranteEntity aspirante) {
        Long carreraId = aspirante.getCarrera().getId();
        Optional<CarreraEntity> carrera = carreraServImp.bucarCarr(carreraId);
        aspirante.setCarrera(carrera.get());
        return aspiranteRepository.save(aspirante);
    }

    @Override
    public AspiranteEntity editarAsp(AspiranteEntity aspirante, Long id) {
        aspirante.setId(id);
        return aspiranteRepository.save(aspirante);
    }

    @Override
    public void eliminarAsp(Long id) {
        aspiranteRepository.deleteById(id);
    }

    @Override
    public Optional<AspiranteEntity> buscarAsp(Long id) {
        return aspiranteRepository.findById(id);
    }

    public boolean existeCorreo(String email) {
        return aspiranteRepository.findByEmail(email).isPresent();
    }

}

