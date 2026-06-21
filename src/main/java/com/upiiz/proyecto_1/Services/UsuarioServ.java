package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.UsuarioEntity;

public interface UsuarioServ {
    UsuarioEntity comprobarUsr(String nombre, String contrasena);
    UsuarioEntity crearUsr(UsuarioEntity usuario);

}
