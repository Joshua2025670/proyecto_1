function listarAsp(){
    $.ajax({
        method: "GET",
        url: "/Dashboard/aspirantes/api/aspirantes",
        data: {},
        success: function (aspirantes) {
            let tabla = $('#example1').DataTable();
            aspirantes.forEach(aspirante => {
                let botones = '<button class="btn-lg btn-success" type="button" data-toggle="modal" data-target="#modal-S" onclick="idenSingular('+aspirante.id+')"><span class="fas fa-mail-bulk"></span></button>';
                botones = botones + ' <button class="btn-lg btn-warning" type="button" data-toggle="modal" data-target="#modal-V" onclick="idenVer('+aspirante.id+')"><span class="fas fa-eye" ></span></button>';
                botones = botones + ' <button class="btn-lg btn-secondary" type="button" data-toggle="modal" data-target="#modal-I" onclick="idenImp('+aspirante.id+')"><span class="fas fa-print" ></span></button>';
                let rowNode = tabla.row
                    .add([aspirante.nombre, aspirante.email, botones])
                    .draw()
                    .node().id = 'renglon_'+aspirante.id;
            })
        }
    })
}

//VALIDAR CON HTML5 QUE ESTE CORRECTO LOS DATOS DEL FORMULARIO
$(document).ready(function () {
    $('#formRegistroAspirante').on('submit', function(e) {
        // evitar RECARGAR pagina
        e.preventDefault();

        // si logra hasta aqui, pues html5 ya lo valido en buena teoria y lo guarda normal
        guardarAsp();
    });
});


function guardarAsp() {
    let nombreAs = document.getElementById('nombreR').value;
    let telAS = document.getElementById('telR').value;
    let emailAs = document.getElementById('emailR').value;
    let carreraAs = document.getElementById('carreraR').value;

    // USAMOS LA FUNCION PARA VERIFICAR QUE EL CORREO EXISTE O NO YA EN NUESTRA BASE DE DATOS
    $.ajax({
        method: "GET",
        url: "/auth/register/api/check-email",
        data: { email: emailAs },
        success: function(correoExiste) {

            if (correoExiste) {
                // SI YA ESTA EL CORREO NO GUARDA NADA Y MANDA UN PEQUENO MENSAJE AL NAVEGADOR
                alert("Atención: El correo " + emailAs + " ya se encuentra registrado. Por favor, utiliza otro.");
                document.getElementById('emailR').value = "";
                document.getElementById('emailR').focus();
            } else {
                // FIN DE LA VERIFICACION, SI EL CORREO NO EXISTE, GUARDA
                $.ajax({
                    method: "POST",
                    url: "/auth/register/api/register",
                    contentType: "application/json",
                    data: JSON.stringify({
                        nombre: nombreAs,
                        tel: telAS,
                        email: emailAs,
                        carrera: {
                            id: parseInt(carreraAs)
                        }
                    }),
                    success: function (aspirante){
                        alert("Aspirante guardado exitosamente.");
                        limpiarFormularioAsp();
                    },
                    error: function() {
                        alert("Ocurrió un error al intentar guardar al aspirante.");
                    }
                });
            }
        },
        error: function() {
            alert("Error al verificar la disponibilidad del correo electrónico.");
        }
    });
}


function idenVer(id){
    $.ajax({
        method: "GET",
        url: "/Dashboard/aspirantes/api/aspirantes/"+id,
        contentType: "application/json",
        data: {},
        success: function(aspirante){
            document.getElementById('id-V').value = aspirante.id;
            document.getElementById('aspirante-V').innerText = aspirante.nombre;
            document.getElementById('nombre').value = aspirante.nombre;
            document.getElementById('tel').value = aspirante.tel;
            document.getElementById('email').value = aspirante.email;
            document.getElementById('carrera').value = aspirante.carrera.nombre;
        }
    })
}
function idenSingular(id){
    $.ajax({
        method: "GET",
        url: "/Dashboard/aspirantes/api/aspirantes/"+id,
        contentType: "application/json",
        data: {},
        success: function(aspirante){
            document.getElementById('id-S').value = aspirante.id;
            document.getElementById('aspirante-S').innerText = aspirante.nombre;
        }
    })
}


function idenImp(id){
    $.ajax({
        method: "GET",
        url: "/Dashboard/aspirantes/api/aspirantes/"+id,
        contentType: "application/json",
        data: {},
        success: function(aspirante){
            document.getElementById('id-I').value = aspirante.id;
            document.getElementById('personaI').innerText = aspirante.nombre;

            let pdfUrl = "/Dashboard/aspirantes/api/aspirantes/" + id + "/pdf";


            document.getElementById('visorPDF').src = pdfUrl;
        }
    });
}

function limpiarFormularioAsp() {
    document.getElementById('nombreR').value = "";
    document.getElementById('telR').value = "";
    document.getElementById('emailR').value = "";

    document.getElementById('carreraR').selectedIndex = 0;

    document.getElementById('nombreR').focus();
}



function enviarCorreoIndividual() {
    let idAspirante = document.getElementById('id-S').value;
    let asunto = document.getElementById('asuntoS').value;
    let mensaje = document.getElementById('mensajeS').value;

    // EVITAR CORREOS VACIOS AQUI VALIDACION AUTENTICACION ETC
    if (!asunto.trim() || !mensaje.trim()) {
        alert("Por favor, ingresa el asunto y el mensaje.");
        return;
    }

    $.ajax({
        method: "POST",
        url: "/Dashboard/aspirantes/api/aspirantes/" + idAspirante + "/email",
        data: {
            asunto: asunto,
            mensaje: mensaje
        },
        success: function(response) {
            alert(response); // mensaje de confirmacion
            $('#modal-S').modal('hide');

            // limpiar campos
            document.getElementById('asuntoS').value = "";
            document.getElementById('mensajeS').value = "";
        },
        error: function() {
            alert("Ocurrió un error al intentar enviar el correo.");
        }
    });
}


function enviarCorreoMasivo() {
    let asunto = document.getElementById('asuntoM').value;
    let mensaje = document.getElementById('mensajeM').value;

    // EVITAR MANDAR CORREOS POR ACCIDENTE
    if (!asunto.trim() || !mensaje.trim()) {
        alert("Por favor, ingresa el asunto y el mensaje.");
        return;
    }

    $.ajax({
        method: "POST",
        url: "/Dashboard/aspirantes/api/aspirantes/email-masivo",
        data: {
            asunto: asunto,
            mensaje: mensaje
        },
        success: function(response) {
            alert(response);
            $('#modal-M').modal('hide');

            // limpiar campos aquiii
            document.getElementById('asuntoM').value = "";
            document.getElementById('mensajeM').value = "";
        },
        error: function() {
            alert("Ocurrió un error al intentar enviar los correos masivos.");
        }
    });
}



$(document).on('hidden.bs.modal', '#modal-I', function () {
    document.getElementById('visorPDF').src = '';
});