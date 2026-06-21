package com.upiiz.proyecto_1.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "aspirante")
public class AspiranteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tel;
    @Column(unique = true)
    private String email;
    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private CarreraEntity carrera;

    public AspiranteEntity() {
    }

    public AspiranteEntity(String nombre, String tel, String email, CarreraEntity carrera) {
        this.nombre = nombre;
        this.tel = tel;
        this.email = email;
        this.carrera = carrera;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CarreraEntity getCarrera() {
        return carrera;
    }

    public void setCarrera(CarreraEntity carrera) {
        this.carrera = carrera;
    }
}
