package com.upiiz.proyecto_1.Services;

import com.upiiz.proyecto_1.Entities.UsuarioEntity;
import com.upiiz.proyecto_1.Reposirories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServImp implements UsuarioServ{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioEntity comprobarUsr(String usuario, String contrasena) {
        Optional<UsuarioEntity> Usr = usuarioRepository.findByNombre(usuario);
        if(Usr.isPresent() && Usr.get().getContrasena().equals(contrasena))
            return Usr.get();
        return null;
    }

    @Override
    public UsuarioEntity crearUsr(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }
}
