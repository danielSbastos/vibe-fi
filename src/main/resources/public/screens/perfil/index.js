const userId = 'id'

window.onload = () => {
    playlist = []
    $.ajax({
        url: `http://localhost:6789/user/${userId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            setStats(data);
        });

    $.ajax({
        url: `http://localhost:6789/vibe/user/${userId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            setCards(data);
        });

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