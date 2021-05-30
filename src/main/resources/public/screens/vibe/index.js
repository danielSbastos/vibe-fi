const params = new URLSearchParams(window.location.search)
const vibeId = params.get("vibeId")
const userId = getCookie("user_id")
var playlist = {}

window.onload = () => {
	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/vibe/${vibeId}?`,
		type: "GET",
		dataType: "json"
	}).done(data => {
		playlist = data
		if (data.userId == userId) {
			setSliders(data)
			setDesc(data)
		} else {
			setNoAcess()
		}
	})

	$("#saveChanges").click(() => {
		playlist.name =
			$("#vibeName").val() == "" ? playlist.name : $("#vibeName").val()
		playlist.description =
			$("#vibeDescription").val() == ""
				? playlist.description
				: $("#vibeDescription").val()

		$.ajax({
			url: `${window.location.protocol}//${window.location.host}/vibe/update/${vibeId}`,
			type: "POST",
			data: JSON.stringify(playlist),
			contentType: "application/json; charset=utf-8"
		}).done(function (data) {
			console.log(data)
		})
	})
}

function setSlider(param, data, percentVlaue = true) {
	if (data.features[param] === null) {
		$("#toggle-" + param).addClass("add-feature")
		$("#toggle-" + param + ">i").removeClass("fa-minus-circle")
		$("#toggle-" + param + ">i").addClass("fa-plus-circle")
		$("#" + param + "-range").prop("disabled", true)

		$("#toggle-" + param).click(evt => {
			playlist.features[param] = $("#" + param + "-range").val()
			setSlider(param, playlist, percentVlaue)
		})
	} else {
		$("#" + param + "-range").val(
			data.features[param] !== null ? data.features[param] : 0
		)

		$("#toggle-" + param).removeClass("add-feature")
		$("#toggle-" + param + ">i").removeClass("fa-plus-circle")
		$("#toggle-" + param + ">i").addClass("fa-minus-circle")

		$("#" + param + "-range").prop("disabled", false)

		$("#toggle-" + param).click(evt => {
			playlist.features[param] = null
			setSlider(param, playlist, percentVlaue)
		})

		$(document).on("input", "#" + param + "-range", () => {
			$("#" + param + "-amount").text(
				Math.round(
					percentVlaue
						? $("#" + param + "-range").val() * 100
						: $("#" + param + "-range").val()
				)
			)

			playlist.features[param] = $("#" + param + "-range").val()
		})
		$("#" + param + "-amount").text(
			Math.round(
				percentVlaue
					? $("#" + param + "-range").val() * 100
					: $("#" + param + "-range").val()
			)
		)
	}
}

function setSliders(data) {
	setSlider("popularity", data, false)
	setSlider("tempo", data, false)
	setSlider("valence", data)
	setSlider("liveness", data)
	setSlider("acousticness", data)
	setSlider("danceability", data)
	setSlider("energy", data)
	setSlider("speechiness", data)
	setSlider("instrumentalness", data)
}

function setDesc(data) {
	$("#vibeName").attr("value", data.name)
	$("#vibeDescription").text(data.description)
	$("#playlist-name > h1").text(data.name)
	$("#playlist-name > h5").text(data.description)
}

function getCookie(cname) {
	var name = cname + "="
	var decodedCookie = decodeURIComponent(document.cookie)
	var ca = decodedCookie.split(";")
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i]
		while (c.charAt(0) == " ") {
			c = c.substring(1)
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length)
		}
	}
	return ""
}

function setNoAcess() {
	content = `<div class="display-6 text-center p-5">Parece que você não possui acesso a essa Vibe :(</div>`
	$(".vibe-container").html(content)
}
