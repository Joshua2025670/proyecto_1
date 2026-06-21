package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.CarreraEntity;
import com.upiiz.proyecto_1.Reposirories.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraServImp implements CarreraServ{
    @Autowired
    private CarreraRepository carreraRepository;

    @Override
    public List<CarreraEntity> listarCarr() {
        return carreraRepository.findAll();
    }

    @Override
    public CarreraEntity crearCarr(CarreraEntity carrera) {
        return carreraRepository.save(carrera);
    }

    @Override
    public CarreraEntity editarCarr(CarreraEntity carrera, Long id) {
        carrera.setId(id);
        return carreraRepository.save(carrera);
    }

    @Override
    public void eliminarCarr(Long id) {
        carreraRepository.deleteById(id);
    }

    @Override
    public Optional<CarreraEntity> bucarCarr(Long id) {
        return carreraRepository.findById(id);
    }
}
