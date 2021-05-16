const params = new URLSearchParams(window.location.search)
const vibeId = params.get('vibeId');
const userId = getCookie("user_id");
var playlist = [];
window.onload = () => {

    $.ajax({
        url: `${window.location.protocol}//${window.location.host}/vibe/${vibeId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            playlist = data;
            if(data.userId == userId){
                setSliders(data)
                setDesc(data)
            }else{
                setNoAcess();
            }
        });

    $('#saveChanges').click(()=>{
        playlist.name = $("#exampleFormControlInput1").val() == ""? playlist.name : $("#exampleFormControlInput1").val()
        playlist.description = $("#exampleFormControlTextarea1").val() == ""? playlist.description : $("#exampleFormControlTextarea1").val()
        
        $.ajax({
            url: `${window.location.protocol}//${window.location.host}/vibe/update/${vibeId}?`,
            type: "GET",
            data: playlist
        })
            .done(function (data) {
                console.log(data)
                window.location = window.location
            });
    })

}

function setSliders(data){
    $(function () {
        $("#popularity-range").slider({
            range: true,
            min: 0,
            max: 1,
            step: 0.01,
            values: [data.minFeatures.popularity/200, data.maxFeatures.popularity/200],
            slide: function (event, ui) {
                $("#pop-amount").val(Math.round(ui.values[0] * 100) + "% - " + Math.round(ui.values[1] * 100) + "%");
                playlist.minFeatures.popularity = Math.round(ui.values[0] * 200);
                playlist.maxFeatures.popularity = Math.round(ui.values[1] * 200);
            }
        });
        $("#pop-amount").val($("#popularity-range").slider("values", 0) * 100 + "% - " + $("#popularity-range").slider("values", 1) * 100 + "%");
    });

    $(function () {
        $("#happy-range").slider({
            range: true,
            min: 0,
            max: 1,
            step: 0.01,
            values: [data.minFeatures.valence, data.maxFeatures.valence],
            slide: function (event, ui) {
                $("#happy-amount").val(Math.round(ui.values[0] * 100) + "% - " + Math.round(ui.values[1] * 100) + "%");
                playlist.minFeatures.valence = ui.values[0];
                playlist.maxFeatures.valence = ui.values[1];
            }
        });
        $("#happy-amount").val($("#happy-range").slider("values", 0) * 100 + "% - " + $("#happy-range").slider("values", 1) * 100 + "%");
    });

    $(function () {
        $("#energy-range").slider({
            range: true,
            min: 0,
            max: 1,
            step: 0.01,
            values: [data.minFeatures.energy, data.maxFeatures.energy],
            slide: function (event, ui) {
                $("#energy-amount").val(Math.round(ui.values[0] * 100) + "% - " + Math.round(ui.values[1] * 100) + "%");
                playlist.minFeatures.energy = ui.values[0];
                playlist.maxFeatures.energy = ui.values[1];
            }
        });
        $("#energy-amount").val($("#energy-range").slider("values", 0) * 100 + "% - " + $("#energy-range").slider("values", 1) * 100 + "%");
    });

    $(function () {
        $("#bpm-range").slider({
            range: true,
            min: 0,
            max: 220,
            step: 1,
            values: [data.minFeatures.tempo, data.maxFeatures.tempo],
            slide: function (event, ui) {
                $("#bpm-amount").val(Math.round(ui.values[0]) + " - " + Math.round(ui.values[1]));
                playlist.minFeatures.tempo = ui.values[0];
                playlist.maxFeatures.tempo = ui.values[1];
            }
        });
        $("#bpm-amount").val($("#bpm-range").slider("values", 0) + " - " + $("#bpm-range").slider("values", 1));
    });

    $(function () {
        $("#danceability-range").slider({
            range: true,
            min: 0,
            max: 1,
            step: 0.01,
            values: [data.minFeatures.danceability, data.maxFeatures.danceability],
            slide: function (event, ui) {
                $("#danceability-amount").val(Math.round(ui.values[0] * 100) + "% - " + Math.round(ui.values[1] * 100) + "%");
                playlist.minFeatures.danceability = ui.values[0];
                playlist.maxFeatures.danceability = ui.values[1];
            }
        });
        $("#danceability-amount").val($("#danceability-range").slider("values", 0) * 100 + "% - " + $("#danceability-range").slider("values", 1) * 100 + "%");
    });

    $(function () {
        $("#acoustic-range").slider({
            range: true,
            min: 0,
            max: 1,
            step: 0.01,
            values: [data.minFeatures.acousticness, data.maxFeatures.acousticness],
            slide: function (event, ui) {
                $("#acoustic-amount").val(Math.round(ui.values[0] * 100) + "% - " + Math.round(ui.values[1] * 100) + "%");
                playlist.minFeatures.acousticness = ui.values[0];
                playlist.maxFeatures.acousticness = ui.values[1];
            }
        });
        $("#acoustic-amount").val($("#acoustic-range").slider("values", 0) * 100 + "% - " + $("#acoustic-range").slider("values", 1) * 100 + "%");
    });
}

function setDesc(data){
    $('#exampleFormControlInput1').attr('placeholder', data.name);
    $('#exampleFormControlTextarea1').attr('placeholder', data.description);
    $('#playlist-name > h1').text(data.name);
    $('#playlist-name > h3').text(data.description);
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

function setNoAcess(){
    content = `<div class="display-6 text-center p-5">Parece que você não possui acesso a essa Vibe :(</div>`;
    $('.vibe-container').html(content);
}
