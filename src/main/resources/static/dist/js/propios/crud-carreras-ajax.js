function listadoCarr(){
    $.ajax({
        method: "GET",
        url: "/Dashboard/carreras/api/carreras",
        data: {},
        success: function(carreras){
            let tabla = $('#example1').DataTable();
            carreras.forEach(carrera=>{
                let botones = '<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#modal-update" onclick="idenUpdt('+carrera.id+')"><span class="fas fa-pencil-alt" aria-hidden="true"></span></button>';
                botones = botones + ' <button class="btn btn-danger" type="button" data-toggle="modal" data-target="#modal-delete" onclick="idenDelete('+carrera.id+')"><span class="fas fa-trash" aria-hidden="true"></span></button>';
                let rowNode = tabla.row
                    .add([carrera.nombre, carrera.semestres, botones])
                    .draw()
                    .node().id = 'renglon_'+carrera.id;
            })
        }
    })
}
function guardarCarr(){
    let nombreA = document.getElementById('nombreC').value;
    let semestresC = document.getElementById('semestres').value;
    let observaciones = document.getElementById('observaciones').value;
    $.ajax({
        method: "POST",
        url: "/Dashboard/carreras/api/carreras",
        data: JSON.stringify({
            nombre: nombreA,
            semestres: semestresC,
            observaciones: observaciones
        }),
        contentType: "application/json",
        success: function(carrera){
            tabla = $('#example1').DataTable();
            let botones = '<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#modal-update" onclick="idenUpdt('+carrera.id+')"><span class="fas fa-pencil-alt" aria-hidden="true"></span></button>';
            botones = botones + ' <button class="btn btn-danger" type="button" data-toggle="modal" data-target="#modal-delete" onclick="idenDelete('+carrera.id+')"><span class="fas fa-trash" aria-hidden="true"></span></button>';
            let rowNode = tabla.row
                .add([carrera.nombre, carrera.semestres, botones])
                .draw()
                .node().id = 'renglon_'+carrera.id;
            alert("Carrera guardada correctamente");
            limpiarFormulario();
        }

    })
}
function limpiarFormulario(){
    document.getElementById('nombreC').value="";
    document.getElementById('semestres').value="";
    document.getElementById('observaciones').value="";
    document.getElementById('nombreC').focus();
}
function limpiartabla(){
    let tabla = $('#example1').DataTable();
    tabla.clear().draw();
}
function idenUpdt(id){
    $.ajax({
        method: "GET",
        url: "/Dashboard/carreras/api/carreras/"+id,
        data: {},
        success: function (carrera){
            document.getElementById('id-update').value = carrera.id;
            document.getElementById('nombreC-update').value = carrera.nombre;
            document.getElementById('semestres-update').value = carrera.semestres;
            document.getElementById('observaciones-update').value = carrera.observaciones;
        }
    })
}
function Updt(){
    let idC = document.getElementById('id-update').value;
    let nombreC = document.getElementById('nombreC-update').value;
    let semestresC = document.getElementById('semestres-update').value;
    let observacionesC = document.getElementById('observaciones-update').value;
    $.ajax({
        method: "PATCH",
        url: "/Dashboard/carreras/api/carreras/"+idC,
        data: JSON.stringify({
            id: idC,
            nombre: nombreC,
            semestres: semestresC,
            observaciones: observacionesC
        }),
        contentType: "application/json",
        success: function (){
            alert("Carrera actualizada");
            limpiartabla();
            listadoCarr();
        }
    })
}
function idenDelete(id){
    $.ajax({
        method: "GET",
        url: "/Dashboard/carreras/api/carreras/"+id,
        data: {},
        success: function (carrera){
            document.getElementById('id-delete').value = carrera.id;
            document.getElementById('advertencia').textContent = '¿Estas seguro de eliminar la carrera de '+carrera.nombre+'?';
        }
    })
}
function Deltete(){
    let idC = document.getElementById('id-delete').value;
    $.ajax({
        method: "DELETE",
        url: "/Dashboard/carreras/api/carreras/"+idC,
        data: {},
        success: function (){
            alert("Carrera eliminada");
            limpiartabla();
            listadoCarr();
        }
    })
}
function listadoOpc(){
    $.ajax({
        method: "GET",
        url: "/auth/register/api/register",
        data: {},
        success: function (carreras) {
            let opciones = document.getElementById('carreraR');
            carreras.forEach(carrera=>{
                let opcion = document.createElement('option');
                opcion.value=carrera.id
                opcion.text = carrera.nombre;
                opciones.appendChild(opcion);
            })
        }
    })
}

