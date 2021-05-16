window.onload = () => {
    playlist = []
    const userId = getCookie("user_id");
    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/user/${userId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            setStats(data);
        });

    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/vibe/user/${userId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            setCards(data);
        })
        .fail(()=>{
            setNoCards();
        });

    $('#logout').click(()=>{
        $.ajax({
            url: `${window.location.protocol}//${window.location.host}/logout`,
            type: "GET",
        })
            .done(()=>{
                console.log("Deslogado com sucesso")
                window.location = window.location
            })
    })
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

function setStats(userData) {
    $('#pbAcoustic')
        .attr('aria-valuenow', userData.stats.acousticness)
        .width(userData.stats.acousticness * 100 + '%');
    $('#perAcoustic').text(userData.stats.acousticness * 100);
    if(userData.stats.valence < 0.5){
        $('i.fa-guitar')
        .toggleClass('fa-guitar')
        .toggleClass('fa-hdd');
    }

    $('#pbPop')
        .attr('aria-valuenow', userData.stats.popularity)
        .width(userData.stats.popularity / 2 + '%');
    $('#perPop').text(userData.stats.popularity/2);
    if(userData.stats.popularity < 100){
        $('i.fa-fire-alt')
        .toggleClass('fa-fire-alt')
        .toggleClass('fa-meh-blank');
    }

    $('#pbEnergy')
        .attr('aria-valuenow', userData.stats.energy)
        .width(userData.stats.energy * 100 + '%');
    $('#perEnergy').text(userData.stats.energy * 100);
    if(userData.stats.energy < 0.5){
        $('i.fa-bolt')
        .toggleClass('fa-bolt')
        .toggleClass('fa-battery-empty');
    }

    $('#pbLive')
        .attr('aria-valuenow', userData.stats.liveness)
        .width(userData.stats.liveness * 100 + '%');
    $('#perLive').text(userData.stats.liveness * 100);
    if(userData.stats.liveness < 0.5){
        console.log('aaaaa')
        $('i.fa-volume-up')
        .toggleClass('fa-volume-up')
        .toggleClass('fa-volume-down');
    }

    $('#pbSmile')
        .attr('aria-valuenow', userData.stats.valence)
        .width(userData.stats.valence * 100 + '%');
    $('#perSmile').text(userData.stats.valence * 100);
    if(userData.stats.valence < 0.5){
        $('i.fa-smile-beam')
        .toggleClass('fa-smile-beam')
        .toggleClass('fa-dizzy');
    }
}

function setCards(data){
    cards = "";
    data.forEach((e) => {       
        cards+= `<div class="col-12 col-md-6">
                    <div class="card vibecard">
                        <div class="card-body">
                            <h5 class="card-img-top">
                                <img src="../../assets/VibefiFull.png" alt="">${e.name}
                            </h5>
                            <p class="card-text">${e.description}</p>
                            <a href="../vibe/?vibeId=${e.id}" class="btn btn-lg float-end btn-success">Editar</a>
                        </div>
                    </div>
                </div>`
    });
    $('#divprinc').html(cards);
}

function setNoCards(){
    content = `<div class="display-6 text-center p-5">Parece que você ainda não possui nenhuma playlist :(</div>`
    $('#divprinc').html(content);
}

