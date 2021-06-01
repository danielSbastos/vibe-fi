window.onload = () => {
    configureLoginUrl();
    alterLoginText();

    playlist = [];
    const userId = getCookie("user_id");

    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/vibe/user/${userId}`,
        type: "GET",
        dataType: "json",
    })
    .done(function (data) {
         playlist = data;
         loadhome(data);
    })
    .fail(function (xhr, status, error) {
	       console.log(error);
           setNoCards();
    });
}

function loadhome (data){
    cards="";
    for  (i = 0;i<data.length;i++){
        cards += `<div id="card" class="col-lg-4">
        <div class="card border-dark w100 vibecard">
            <img class="card-img-top" src="${data[i].imgUrl}" alt="Imagem de capa do card">
            <div class="card-body">
                <h5 class="card-title">${data[i].name}</h5>
                <p class="card-text">${data[i].description}</p>
                <a href="screens/vibe/?vibeId=${data[i].id}" class="btn btn-primary">Visitar</a>
            </div>
        </div></div>`;
    }
    document.getElementById("divprinc").innerHTML = cards;

}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function alterLoginText() {
    if (getCookie("user_id") !== "") {
        document.getElementById("login").innerText = "Visitar perfil";
        document.getElementById("login").href = `http://${window.location.host}/screens/perfil/`;
    }
}

function configureLoginUrl() {
    document.getElementById("login").href = `${window.location.protocol}//${window.location.host}/login`;
}

function setNoCards(){
    content = `<div class="display-6 text-center p-5">Parece que você ainda não possui nenhuma playlist :(</div>`
    $('#divprinc').html(content);
}
