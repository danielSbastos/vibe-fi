const params = new URLSearchParams(window.location.search)
const vibeId = params.get("vibeId")
const userId = getCookie("user_id")
let vibe = {}

window.onload = () => {
	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/vibeseed/${vibeId}`,
		type: "GET",
		headers: {
			"Content-Type": "application/json",
			"Authorization": "Bearer " + getCookie("access_token")
		}
	}).done((data) => {
		if (!data.vibeUser) data = JSON.parse(data)
		if (data.vibeUser == userId) {
			vibe = JSON.parse(JSON.stringify(data))
			$("#title").text($("#title").text() + " " + data.vibeName)
			vibe.vibeseeds = []
			if (data.vibeseeds) data.vibeseeds.forEach(addSeed)
		} else {
			setNoAcess()
		}
	})
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

function search(input, type) {
	$.ajax({
		url: `https://api.spotify.com/v1/search?q=${input}&type=${type}&limit=6`,
		type: "GET",
		headers: {
			"Content-Type": "application/json",
			"Authorization": "Bearer " + getCookie("access_token")
		}
	})
		.done(function (data) {
			if (type == "artist") {
				genArtists(data)
			} else if (type == "track") {
				genTrack(data)
			}
		})
		.fail(() => {
			console.log("fail")
		})
}

function removeItem(seedId, type) {
	let fullId = type + ":" + seedId
	let element = document.getElementById(fullId)
	if (element) {
		element.firstElementChild.classList.remove("cardActive")
	}
	$(`#${seedId}`).remove()

	let removeIndex = vibe.vibeseeds.findIndex(
		(seed) => seed.identifier == seedId
	)

	if (removeIndex > -1) {
		vibe.vibeseeds.splice(removeIndex, 1)
	}
}

function addSeed(seed) {
	if (vibe.vibeseeds.length >= 5) {
		alert(`O máximo itens selecionados foi atingido`)
		return
	}

	vibe.vibeseeds.push(seed)
	let icon = seed.type == "track" ? "fas fa-music" : "fas fa-user-alt"

	$("#seedList").append(
		`<li class="list-group-item bg-transparent text-light border-0" id="${seed.identifier}" seedType="${seed.type}">
		<i class="${icon}"></i>
		${seed.description} 
		<i class="fas fa-trash float-end" onclick="removeItem(this.parentNode.id, this.parentNode.seedType)"></i>
        </li>`
	)

	let fullId = seed.type + ":" + seed.identifier
	let element = document.getElementById(fullId)
	if (element) {
		element.firstElementChild.classList.add("cardActive")
	}
}

function toggleActive(object) {
	let info = object.id.split(":")
	seed = {
		identifier: info[1],
		description: object.getElementsByClassName("artName")[0].textContent,
		type: info[0],
		vibeId: vibeId
	}

	if ($(`#${seed.identifier}`).length) {
		removeItem(seed.identifier, seed.type)
	} else {
		addSeed(seed)
	}
}

function addCard(item, type, imageUrl) {
	$("#cardsSelection")
		.append(`<div class="col-md-4 my-3" onclick="toggleActive(this)" id="${type}:${item.id}">
            <div class="card bg-dark text-white" style="max-height: 260px; overflow: hidden;">
                <img src="${imageUrl}" class="card-img" alt="..." />
                <div class="card-img-overlay">
                    <h5 class="card-title artName float-end">${item.name}</h5>
                </div>
                <div class="clear-fix"></div>
            </div>
        </div>`)
}

function genArtists(data) {
	$("#cardsSelection").html("")

	data.artists.items.forEach((artist) => {
		let url = ""
		if (artist.images.length) {
			url = artist.images[0].url
		} else {
			url = "../../assets/VibefiFull.png"
		}
		addCard(artist, "artist", url)
	})
}

function genTrack(data) {
	$("#cardsSelection").html("")

	data.tracks.items.forEach((track) => {
		let url = ""
		if (track.album.images.length) {
			url = track.album.images[0].url
		} else {
			url = "../../assets/VibefiFull.png"
		}
		addCard(track, "track", url)
	})
}

function sendIDS() {
	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/vibeseed/allseeds/${vibeId}`,
		type: "POST",
		data: JSON.stringify(vibe.vibeseeds),
		contentType: "application/json; charset=utf-8"
	}).done(function (data) {
		window.location = `${window.location.protocol}//${window.location.host}/screens/vibe/?vibeId=${vibeId}`
	})
}

function prepSearch() {
	let input = $("#inputSearch").val()
	let option = $("#selectSearch").val() === "Artista" ? "artist" : "track"
	search(input, option)
}

function setNoAcess() {
	content = `<div class="display-6 text-center p-5">Parece que você não possui acesso a essa Vibe :(</div>`
	$("#mainContainer").html(content)
}
