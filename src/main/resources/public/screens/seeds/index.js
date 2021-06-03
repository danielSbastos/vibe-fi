const params = new URLSearchParams(window.location.search)
const vibeId = params.get("vibeId")
const vibeName = params.get("vibeName")

window.onload = () => {
    $("#title").text($("#title").text() + ' ' + vibeName)
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function search(input, type) {
  $.ajax({
    url: `https://api.spotify.com/v1/search?q=${input}&type=${type}&limit=6`,
    type: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + getCookie("access_token"),
    },
  })
    .done(function (data) {
      if (type == "artist") {
        genArtists(data, type);
      } else {
        genTrack(data, type);
      }
    })
    .fail(() => {
      console.log("fail");
    });
}

function removeItem(obj, type) {
  let fullId = obj.innerText.trim() + ";" + obj.id + ";" + type;
  let element = document.getElementById(fullId);
  if (element) {
    element.firstElementChild.classList.toggle("cardActive");
  }
  $(`#${obj.id}`).remove();
  let count = $(`#artistList`).attr(type);
  $(`#artistList`).attr(`${type}`, parseInt(count) - 1);
}

function toggleActive(object) {
  let info = object.id.split(";");
  let count = $(`#artistList`).attr(info[2]);
  let icon = info[2] == "track" ? "fas fa-music" : "fas fa-user-alt";

  if ($(`#${info[1]}`).length) {
    $(`#${info[1]}`).remove();
    $(`#artistList`).attr(`${info[2]}`, parseInt(count) - 1);
    object.firstElementChild.classList.toggle("cardActive");
  } else {
    if (count >= 5) {
      alert(`O máximo de ${info[2]} foi atingido`);
    } else {
      $("#artistList").append(
        `<li class="list-group-item bg-transparent text-light border-0" id="${info[1]}" seedType="${info[2]}">
            <i class="${icon}"></i>
            ${info[0]} 
            <i class="fas fa-trash float-end" onclick="removeItem(this.parentNode, '${info[2]}')"></i>
        </li>`
      );
      $(`#artistList`).attr(`${info[2]}`, parseInt(count) + 1);

      object.firstElementChild.classList.toggle("cardActive");
    }
  }
}

function genArtists(data, type) {
  let content = "";
  data.artists.items.forEach((e) => {
    let url = "";
    if (e.images.length) {
      url = e.images[0].url;
    } else {
      url = "../../assets/VibefiFull.png";
    }
    content += `<div class="col-md-4 my-3" onclick="toggleActive(this)" id="${e.name};${e.id};${type}">
            <div class="card bg-dark text-white" style="max-height: 260px; overflow: hidden;">
                <img src="${url}" class="card-img" alt="..." />
                <div class="card-img-overlay">
                    <h5 class="card-title artName float-end">${e.name}</h5>
                </div>
                <div class="clear-fix"></div>
            </div>
        </div>`;
  });
  $("#cardsSelection").html(content);
}

function genTrack(data, type) {
  let content = "";
  data.tracks.items.forEach((e) => {
    let url = "";
    if (e.album.images.length) {
      url = e.album.images[0].url;
    } else {
      url = "../../assets/VibefiFull.png";
    }
    content += `<div class="col-md-4 my-3" onclick="toggleActive(this)" id="${e.name};${e.id};${type}">
              <div class="card bg-dark text-white" style="max-height: 260px; overflow: hidden;">
                  <img src="${url}" class="card-img" alt="..." />
                  <div class="card-img-overlay">
                      <h5 class="card-title artName float-end">${e.name}</h5>
                  </div>
                  <div class="clear-fix"></div>
              </div>
          </div>`;
  });

  $("#cardsSelection").html(content);
}

function sendIDS(){
  let ids = [];
  let children = $('#artistList').children();
  for(var i = 0; i < children.length; i++){
    ids[i] = { vibeId, identifier: children[i].id, type: children[i].getAttribute('seedType') };
  }

  $.ajax({
      url: `${window.location.protocol}//${window.location.host}/vibeseed`,
      type: "POST",
      data: JSON.stringify(ids),
      contentType: "application/json; charset=utf-8"
  }).done(function (data) {
      console.log(data);
      window.location = `${window.location.protocol}//${window.location.host}/screens/vibe/?vibeId=${vibeId}`;
  })
}

function prepSearch() {
  let input = $("#inputSearch").val();
  let option = $("#selectSearch").val() === "Artista" ? "artist" : "track";
  search(input, option);
}
