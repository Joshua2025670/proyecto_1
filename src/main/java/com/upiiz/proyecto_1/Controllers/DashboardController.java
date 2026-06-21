package com.upiiz.proyecto_1.Controllers;

import com.upiiz.proyecto_1.Entities.AspiranteEntity;
import com.upiiz.proyecto_1.Entities.CarreraEntity;
import com.upiiz.proyecto_1.Services.AspiranteServImp;
import com.upiiz.proyecto_1.Services.CarreraServImp;
import com.upiiz.proyecto_1.Services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.Image;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Dashboard")
public class DashboardController {
    @Autowired
    private AspiranteServImp aspiranteServImp;

    @Autowired
    private CarreraServImp carreraServImp;

    @Autowired
    private EmailService emailService;

    private Boolean checksesion(HttpSession session, Model model){
        if(session.getAttribute("usuariologueado") == null)
            return false;
        model.addAttribute("usuario", session.getAttribute("usuariologueado"));
        return true;
    }

    @GetMapping("/aspirantes")
    public String mostarListadoA(HttpSession session, Model model){
        if (!checksesion(session, model))
            return "redirect:/auth/login/admin";
        return "listado-aspirantes";
    }

    @GetMapping("/carreras")
    public String mostrarcarreras(HttpSession session, Model model){
        if (!checksesion(session, model))
            return "redirect:/auth/login/admin";
        return "listado-carreras";
    }

    @GetMapping("/aspirantes/api/aspirantes")
    @ResponseBody
    public ResponseEntity<List<AspiranteEntity>> listado(){
        return ResponseEntity.ok(aspiranteServImp.listadoAsp());
    }

    @GetMapping("/carreras/api/carreras")
    @ResponseBody
    public ResponseEntity<List<CarreraEntity>> listadoCarr(){
        return ResponseEntity.ok(carreraServImp.listarCarr());
    }

    @PostMapping("/carreras/api/carreras")
    @ResponseBody
    public ResponseEntity<CarreraEntity> crearCarr(@RequestBody CarreraEntity carrera){
        return ResponseEntity.ok(carreraServImp.crearCarr(carrera));
    }

    @PatchMapping("/carreras/api/carreras/{id}")
    @ResponseBody
    public ResponseEntity<CarreraEntity> editarCarr(@RequestBody CarreraEntity carrera, @PathVariable Long id){
        return ResponseEntity.ok(carreraServImp.editarCarr(carrera, id));
    }

    @DeleteMapping("/carreras/api/carreras/{id}")
    @ResponseBody
    public void eliminarCarr(@PathVariable Long id){
        carreraServImp.eliminarCarr(id);
    }

    @GetMapping("/aspirantes/api/aspirantes/{id}")
    @ResponseBody
    public ResponseEntity<Optional<AspiranteEntity>> buscarAsp(@PathVariable Long id){
        return ResponseEntity.ok(aspiranteServImp.buscarAsp(id));
    }

    @GetMapping("/carreras/api/carreras/{id}")
    @ResponseBody
    public ResponseEntity<Optional<CarreraEntity>> buscarCarr(@PathVariable Long id){
        return ResponseEntity.ok(carreraServImp.bucarCarr(id));
    }

    //LOGICA PARA EL PDF QUE DOLOR QUE TRISTEZA ESTO FUE BASTANTE TEDIOOOOSOOOOOOOO QUE DOLOOOOORRRR

    @GetMapping("/aspirantes/api/aspirantes/{id}/pdf")
    public ResponseEntity<byte[]> generarConstanciaPdf(@PathVariable Long id) {
        Optional<AspiranteEntity> aspiranteOpt = aspiranteServImp.buscarAsp(id);

        if (!aspiranteOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AspiranteEntity aspirante = aspiranteOpt.get();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Document document = new Document(PageSize.LETTER, 50, 50, 50, 50)) {

            PdfWriter.getInstance(document, baos);
            document.open();

            // INTENTAR AGREGAR LOS LOGOS AL PDF USANDO A OPENPDF Y SUS METODOS MAGICOS, habiendo agregado las imagenes
            // a la carpeta static/dist/img
            try {
                String ipnLogoPath = new ClassPathResource("static/dist/img/logo-ipn.png").getURL().toString();
                Image logoIpn = Image.getInstance(ipnLogoPath);
                // ACOMODAR ESCALA Y PONERLO EN UNA ESQUINITA
                logoIpn.scaleToFit(70, 70);
                logoIpn.setAbsolutePosition(60, 700);
                document.add(logoIpn);
                String upiizLogoPath = new ClassPathResource("static/dist/img/logo-upiiz.png").getURL().toString();
                Image logoUpiiz = Image.getInstance(upiizLogoPath);
                logoUpiiz.scaleToFit(70, 70);
                logoUpiiz.setAbsolutePosition(480, 700);
                document.add(logoUpiiz);

            } catch (Exception e) {
                System.out.println("Aviso: No se encontraron los logos en la ruta especificada.");
                e.printStackTrace();
            }



            // FUENTES USADAS EN EL PDF
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // encabezados je
            Paragraph header1 = new Paragraph("Instituto Politécnico Nacional", fontNegrita);
            header1.setAlignment(Element.ALIGN_CENTER);
            document.add(header1);

            Paragraph header2 = new Paragraph("Unidad de Educación Continua", fontNegrita);
            header2.setAlignment(Element.ALIGN_CENTER);
            document.add(header2);

            Paragraph header3 = new Paragraph("UPIIZ-IPN", fontNegrita);
            header3.setAlignment(Element.ALIGN_CENTER);
            header3.setSpacingAfter(30);
            document.add(header3);

            // titulo
            Paragraph titulo = new Paragraph("CONSTANCIA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_LEFT);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            Paragraph aQuien = new Paragraph("A QUIEN CORRESPONDA:", fontNegrita);
            aQuien.setAlignment(Element.ALIGN_LEFT);
            aQuien.setSpacingAfter(15);
            document.add(aQuien);

            // cuerpo documento
            document.add(new Paragraph("Por medio de la presente se hace constar que:", fontNormal));

            Paragraph nombreAlumno = new Paragraph(aspirante.getNombre().toUpperCase(), fontNegrita);
            nombreAlumno.setAlignment(Element.ALIGN_CENTER);
            nombreAlumno.setSpacingBefore(10);
            nombreAlumno.setSpacingAfter(10);
            document.add(nombreAlumno);

            String matricula = String.format("%06d", aspirante.getId());

            Paragraph parrafo1 = new Paragraph("con número de matrícula " + matricula + ", se encuentra inscrito en el Curso de Preparación para el Ingreso al Nivel Superior del Instituto Politécnico Nacional (IPN).", fontNormal);
            parrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafo1.setSpacingAfter(10);
            document.add(parrafo1);

            Paragraph parrafo2 = new Paragraph("El curso tiene como objetivo brindar a los alumnos las herramientas y conocimientos necesarios para enfrentar con éxito el examen de admisión al nivel superior de nuestra institución.", fontNormal);
            parrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafo2.setSpacingAfter(10);
            document.add(parrafo2);

            Paragraph parrafo3 = new Paragraph("La duración del curso es de 6 meses y se desarrolla en las instalaciones de la Unidad Profesional Interdisciplinaria de Ingeniería Campus Zacatecas.", fontNormal);
            parrafo3.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafo3.setSpacingAfter(10);
            document.add(parrafo3);

            Paragraph parrafo4 = new Paragraph("La fecha de inicio del curso fue el 12 de enero de 2026 y la fecha de finalización prevista es el 12 de julio de 2026.", fontNormal);
            parrafo4.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafo4.setSpacingAfter(10);
            document.add(parrafo4);

            Paragraph parrafo5 = new Paragraph("Para cualquier información adicional, favor de comunicarse a nuestras oficinas al teléfono 492 123 4567.", fontNormal);
            parrafo5.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafo5.setSpacingAfter(20);
            document.add(parrafo5);

            document.add(new Paragraph("Sin más por el momento, quedo a sus órdenes.", fontNormal));

            // parte fianl deespedidas y asi
            Paragraph atentamente = new Paragraph("Atentamente,", fontNormal);
            atentamente.setAlignment(Element.ALIGN_CENTER);
            atentamente.setSpacingBefore(40);
            atentamente.setSpacingAfter(40);
            document.add(atentamente);

            Paragraph director = new Paragraph("Ing. Becerril Cabral Gabriel \nDirector/Coordinador del Curso\nUnidad de Educación Continua\nInstituto Politécnico Nacional", fontNegrita);
            director.setAlignment(Element.ALIGN_CENTER);
            document.add(director);

            // fecha
            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph fechaEmision = new Paragraph("Fecha de Emisión: " + fechaHoy, fontNormal);
            fechaEmision.setAlignment(Element.ALIGN_RIGHT);
            fechaEmision.setSpacingBefore(10);
            document.add(fechaEmision);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"constancia_" + aspirante.getId() + ".pdf\"");

            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/aspirantes/api/aspirantes/{id}/email")
    @ResponseBody
    public ResponseEntity<String> enviarCorreoIndividual(@PathVariable Long id, @RequestParam String asunto, @RequestParam String mensaje) {
        Optional<AspiranteEntity> aspiranteOpt = aspiranteServImp.buscarAsp(id);

        if (aspiranteOpt.isPresent()) {
            AspiranteEntity aspirante = aspiranteOpt.get();
            emailService.enviarCorreo(aspirante.getEmail(), asunto, mensaje);
            return ResponseEntity.ok("Correo enviado exitosamente a " + aspirante.getNombre());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/aspirantes/api/aspirantes/email-masivo")
    @ResponseBody
    public ResponseEntity<String> enviarCorreoMasivo(@RequestParam String asunto, @RequestParam String mensaje) {
        List<AspiranteEntity> aspirantes = aspiranteServImp.listadoAsp();

        for (AspiranteEntity aspirante : aspirantes) {
            if(aspirante.getEmail() != null && !aspirante.getEmail().isEmpty()) {
                emailService.enviarCorreo(aspirante.getEmail(), asunto, mensaje);
            }
        }
        return ResponseEntity.ok("Correos masivos enviados exitosamente.");
    }



}
