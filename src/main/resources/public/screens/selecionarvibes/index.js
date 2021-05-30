window.onload = () =>{
    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/vibetemplate/all`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            console.log(data);
            createCard(data);
        });
}

function createCard(data){
    cards = "";
    data.templates.forEach((e) => {
        cards += `<div class="col-md-4 p-4" onclick="toggleActive(this)">
                    <div class="card select-card cardActive" id="${e.id}">
                        <div class="card-img-block">
                            <img class="card-img-top" src="${e.imgUrl}"
                                alt="Card image cap">
                        </div>
                        <div class="card-body pt-0">
                            <h5 class="card-title">${e.name}</h5>
                            <p class="card-text">${e.desc}.</p>
                        </div>
                    </div>
                </div>`
        console.log(e)
    });
    $('#selectVibes').html(cards);
}

function toggleActive(div){
    div.firstElementChild.classList.toggle('cardActive');
    div.firstElementChild.firstElementChild.classList.toggle('fotoActive');
}

function genVibes(){
    let selected = []
    let ids = []
    selected = (document.getElementsByClassName("cardActive"));
    for (let i = 0; i < selected.length; i++) {
        ids[i] = selected[i].id;
    }

    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/vibe/generate`,
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        timeout: 11000, // sets timeout to 3 seconds
        data: {
           templateIds: ids,
        },
    }).done(function(data)  {
         console.log("OK");
         window.location = `${window.location.protocol}//${window.location.host}/screens/perfil/`;
    }).fail(function()  {
         alert("Sorry. Server unavailable. ");
    });
}
