package com.upiiz.proyecto_1.Controllers;

import com.upiiz.proyecto_1.Entities.AspiranteEntity;
import com.upiiz.proyecto_1.Entities.CarreraEntity;
import com.upiiz.proyecto_1.Entities.UsuarioEntity;
import com.upiiz.proyecto_1.Services.AspiranteServImp;
import com.upiiz.proyecto_1.Services.CarreraServImp;
import com.upiiz.proyecto_1.Services.EmailService;
import com.upiiz.proyecto_1.Services.UsuarioServImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AspiranteServImp aspiranteServImp;

    @Autowired
    private UsuarioServImp usuarioServImp;

    @Autowired
    private CarreraServImp carreraServImp;

    @Autowired
    private EmailService emailService;


    @GetMapping("/register")
    public String mostrarR(){
        return "register";
    }
    @GetMapping("/login/admin")
    public String mostrarLogin(){
        return "login";
    }

    @GetMapping("/register/admin")
    public String mostrar(){
        return "registro";
    }

    @GetMapping("/register/api/register")
    @ResponseBody
    public ResponseEntity<List<CarreraEntity>> listaCarr(){
        return ResponseEntity.ok(carreraServImp.listarCarr());
    }

    @PostMapping("/register/api/register")
    @ResponseBody
    public ResponseEntity<AspiranteEntity> guardarAsp (@RequestBody AspiranteEntity aspirante){
        AspiranteEntity nuevoAspirante = aspiranteServImp.crearAsp(aspirante);
        String asunto = "Nuevo registro de aspirante: " + nuevoAspirante.getNombre();
        String mensaje = "Se ha registrado un nuevo aspirante exitosamente.\n\n" +
                "Nombre: " + nuevoAspirante.getNombre() + "\n" +
                "Email: " + nuevoAspirante.getEmail() + "\n" +
                "Teléfono: " + nuevoAspirante.getTel();

        // EL CORREO QUE SE PONGA AQUI VA A SER AL QUE LAS ALERTAS LLEGUEN, DEBE SER DEL ADMIN.
        emailService.enviarCorreo("joshuareyesr@gmail.com", asunto, mensaje);

        return ResponseEntity.ok(nuevoAspirante);
    }

    @PostMapping("/register/admin/api/register")
    @ResponseBody
    public ResponseEntity<UsuarioEntity> guardarUsr (@RequestBody UsuarioEntity usuario){
        return ResponseEntity.ok(usuarioServImp.crearUsr(usuario));
    }

    @GetMapping("/login/api/login/{nombre}/{contrasena}")
    @ResponseBody
    public ResponseEntity<UsuarioEntity> comprobarUsr(@PathVariable String nombre, @PathVariable String contrasena, HttpSession session){
        UsuarioEntity usuario = usuarioServImp.comprobarUsr(nombre, contrasena);
        session.setAttribute("usuariologueado", usuario);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/register/api/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> verificarCorreo(@RequestParam String email) {
        boolean existe = aspiranteServImp.existeCorreo(email);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // ESTO ES PARA LO DE DESTRUIR LA SESION QUE PIDE EN EL PDF DEL PROYECTO, GRACIAS
        session.invalidate();
        return "redirect:/auth/login/admin";
    }


}
