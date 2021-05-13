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
            console.log(data);
        });

}

function setStats(userData) {
    $('#pbAcoustic')
        .attr('aria-valuenow', userData.stats.acousticness)
        .width(userData.stats.acousticness * 100 + '%');
    $('#pbPop')
        .attr('aria-valuenow', userData.stats.popularity)
        .width(userData.stats.popularity / 2 + '%');
    $('#pbEnergy')
        .attr('aria-valuenow', userData.stats.energy)
        .width(userData.stats.energy * 100 + '%');
    $('#pbLive')
        .attr('aria-valuenow', userData.stats.liveness)
        .width(userData.stats.liveness * 100 + '%');
    $('#pbSmile')
        .attr('aria-valuenow', userData.stats.valence)
        .width(userData.stats.valence * 100 + '%');
}