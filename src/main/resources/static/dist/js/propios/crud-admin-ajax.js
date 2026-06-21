function comprobarnombre(){
    let nombre = document.getElementById('usuario').value;
    let contrasenaU = document.getElementById('contrasena').value;
    $.ajax({
        method: "GET",
        url: "/auth/login/api/login/"+nombre+"/"+contrasenaU,
        data: {},
        success: function (usuario){
            if(usuario.contrasena==contrasenaU && usuario.nombre==nombre) {
                alert("Usuario Encontrado");
                window.location.href="/Dashboard/aspirantes";
            }
            else
                alert("Los datos proporcionados no son correctos")
        }
    })
}
function agregarUsr(){
    let nombre = document.getElementById('usuario-R').value;
    let contrasena = document.getElementById('contrasena-R').value;
    $.ajax({
        method: "POST",
        url: "/auth/register/admin/api/register",
        data: JSON.stringify({
            nombre: nombre,
            contrasena: contrasena
        }),
        contentType: "application/json",
        success: function (){
            alert("Usuario registrado");
        }
    })
}