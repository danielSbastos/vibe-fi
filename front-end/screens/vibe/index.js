const params = new URLSearchParams(window.location.search)
const vibeId = params.get('vibeId')

window.onload = () => {

    $.ajax({
        url: `http://localhost:6789/vibe/${vibeId}?`,
        type: "GET",
        dataType: "json",
    })
        .done(function (data) {
            setSliders(data)
            setDesc(data)
        });

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
