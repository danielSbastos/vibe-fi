const userId = 'id'

window.onload = () => {
    playlist = []


    $.ajax({
        url: `http://localhost:6789/vibe/user/${userId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            playlist = data;
            loadhome(data);
        
        });

}
function loadhome (data){

 
    cards="" 
    for  (i = 0;i<data.length;i++){
        cards+= `<div id="card" class="col-lg-4">
        <div class="card border-dark w100">
            <img class="card-img-top" src="assets/VibefiFull.png" alt="Imagem de capa do card">
            <div class="card-body">
                <h5 class="card-title">${data[i].name}</h5>
                <p class="card-text">${data[i].description}</p>
                <a href="screens/vibe/?vibeId=${data[i].id}" class="btn btn-primary">Visitar</a>
            </div>
        </div></div>`    
    } 
    document.getElementById("divprinc").innerHTML = cards

}