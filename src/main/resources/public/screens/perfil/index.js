window.onload = () => {
	const userId = getCookie("user_id")

	updateContent(userId)
	setAlertMessage()

	$("#incompleteVibeGeneration").bind("hidden.bs.modal", function () {
		$.ajax({
			url: `${window.location.protocol}//${window.location.host}/cookies/deleteMissingClasses`,
			type: "GET"
		}).done(function (data) {
			console.log("deleted")
		})
	})

	playlist = []
	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/user/${userId}?`,
		type: "GET",
		dataType: "json"
	}).done(function (data) {
		setProfile(data)
		setStats(data)
	})

	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/vibe/user/${userId}?`,
		type: "GET",
		dataType: "json"
	})
		.done(function (data) {
			setCards(data)
		})
		.fail(() => {
			setNoCards()
		})

	$("#logout").click(() => {
		$.ajax({
			url: `${window.location.protocol}//${window.location.host}/logout`,
			type: "GET"
		}).done(() => {
			console.log("Deslogado com sucesso")
			window.location = `${window.location.protocol}//${window.location.host}`
		})
	})

    $("#btnReconfigureVibes").click(() => {
        window.location = `${window.location.protocol}//${window.location.host}/screens/selecionarvibes/`
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

function setProfile(userData) {
	console.log(userData)
	$("#displayName").text(userData.user)
	if (userData.imageURL) {
		$("#profilePic").html(`<img src=${userData.imageURL} width=100%>`)
	}
}

function setStats(userData) {
	$("#pbAcoustic")
		.attr("aria-valuenow", userData.stats.acousticness)
		.width(userData.stats.acousticness * 100 + "%")
	$("#perAcoustic").text(Math.round(userData.stats.acousticness * 100))
	if (userData.stats.acousticness < 0.5) {
		$("i.fa-guitar").toggleClass("fa-guitar").toggleClass("fa-hdd")
	}

	$("#pbPop")
		.attr("aria-valuenow", userData.stats.popularity)
		.width(userData.stats.popularity + "%")
	$("#perPop").text(userData.stats.popularity)
	if (userData.stats.popularity < 50) {
		$("i.fa-fire-alt")
			.toggleClass("fa-fire-alt")
			.toggleClass("fa-meh-blank")
	}

	$("#pbEnergy")
		.attr("aria-valuenow", userData.stats.energy)
		.width(userData.stats.energy * 100 + "%")
	$("#perEnergy").text(Math.round(userData.stats.energy * 100))
	if (userData.stats.energy < 0.5) {
		$("i.fa-bolt").toggleClass("fa-bolt").toggleClass("fa-battery-empty")
	}

	$("#pbLive")
		.attr("aria-valuenow", userData.stats.liveness)
		.width(userData.stats.liveness * 100 + "%")
	$("#perLive").text(Math.round(userData.stats.liveness * 100))
	if (userData.stats.liveness < 0.5) {
		$("i.fa-volume-up")
			.toggleClass("fa-volume-up")
			.toggleClass("fa-volume-down")
	}

	$("#pbSmile")
		.attr("aria-valuenow", userData.stats.valence)
		.width(userData.stats.valence * 100 + "%")
	$("#perSmile").text(Math.round(userData.stats.valence * 100))
	if (userData.stats.valence < 0.5) {
		$("i.fa-smile-beam")
			.toggleClass("fa-smile-beam")
			.toggleClass("fa-dizzy")
	}
}

function setCards(data) {
	cards = ""
	data.forEach((e) => {
		let isSeedless = e.seeds.length === 0
		let editUrl = isSeedless
			? `../seeds/?vibeId=${e.id}`
			: `../vibe/?vibeId=${e.id}`

		cards += `<div class="col-12 col-md-6">
                    <div class="card vibecard">
                        <div class="card-body">
                            <h5 class="card-img-top">
                                <img src="${e.imgUrl}" alt="">${e.name}
                                ${
									isSeedless
										? '<span class="badge bg-warning text-dark">sem músicas</span>'
										: ""
								}
                            </h5>
                            <p class="card-text">${e.description}</p>
                            <a href="${editUrl}" class="btn btn-lg float-end btn-success">Editar</a>
                            <button type="button" onclick="setModal('${
								e.id
							}')"  class="btn btn-lg btn-danger float-end me-2"
                                data-bs-toggle="modal" data-bs-target="#deleteVibeModal">
                                <i id="deleteVibe" class="far fa-trash-alt "></i>
                            </button>
                        </div>
                    </div>
                </div>`
	})
	$("#divprinc").html(cards)
}

function setModal(id) {
	document.getElementById("btnConfDel").removeEventListener("click", delVibe)
	$("#btnConfDel").click(() => {
		delVibe(id)
	})
}

function delVibe(id) {
	$.ajax({
		url: `${window.location.protocol}//${window.location.host}/vibe/delete/${id}?`,
		type: "GET",
		dataType: "json"
	}).done(function (data) {
		console.log("Delete successfuly")
	})
	window.location = window.location
}

function setNoCards() {
	content = `<div class="display-6 text-center p-5">Parece que você ainda não possui nenhuma playlist :(</div>`
	$("#divprinc").html(content)
}

function updateContent(userId) {
	if (userId === "") {
		$("#perfil-stats").html("")

		content = `<div class="display-6 text-center p-5">Parece que você não está logado :(</div>`
		$("#perfil-vibes").html(content)
	}
}

function setAlertMessage() {
	let missingClasses = getCookie("missing-classes")
	if (missingClasses !== "") {
		missingClasses = missingClasses.split("&").join(", ")
		$("#incompleteModalBody").html(
			`Não conseguimos encontrar músicas que você escuta para as Vibes ${missingClasses}. Você pode usar esta sua página de perfil para atualizá-las`
		)
		$("#incompleteVibeGeneration").modal("show")
	}
}
