import requests
from time import sleep
import json
import os
from functools import reduce

TOKEN = os.getenv("TOKEN")
PIDS_CLASSES = [
        { "ids": ["37i9dQZF1DWWKap1fTevjS", "37i9dQZF1DWT0d3wigiTss", "37i9dQZF1DX05Zqxyo07cQ", "37i9dQZF1DX1U93rnCNfyR", "37i9dQZF1DXbYTn1f3GrPL"], "class": "dormir"},
        { "ids": ["37i9dQZF1DX78yU2od6grb", "6qhPB1dNchQ55E5zUZEAwo", "4KxHGUlzWQdYpaP3Ja3YrT", "69N5GrdsCrd9l7iqpcoLc7"], "class": "correr"},
        { "ids": ["5LBUYwZPKzJ3iFznqozeg7", "2pPXbU6wq6K1WZfANJpZcW", "45DU72qlzzLgqStC4oYNY6"], "class": "triste"},
        { "ids": ["37i9dQZF1DWVovHcLREwOK", "37i9dQZF1DWUIDYTCle9M9", "37i9dQZF1DXdPec7aLTmlC" ], "class": "feliz"},
        { "ids": ["37i9dQZF1DX4sWSpwq3LiO", "37i9dQZF1DXatxKKvXOpbl", "37i9dQZF1DX1s9knjP51Oa"], "class": "calmo"},
        { "ids": ["37i9dQZF1DWYYYb1pjoXTj", "37i9dQZF1DWTyiBJ6yEqeu", "37i9dQZF1DWVi45nh2EuPP", "37i9dQZF1DWUyj0bHlj8N3"], "class": "gaming"},
        { "ids": ["37i9dQZF1DX0vHZ8elq0UK", "37i9dQZF1DX35X4JNyBWtb", "37i9dQZF1DWZVAVMhIe3pV"], "class": "energetico"},
        { "ids": ["7xmpNYZ15D6b43BHzYQCLt"], "class": "foco"},
]

headers = lambda token: { "Authorization": f"Bearer {token}" }

url = lambda pid: f"https://api.spotify.com/v1/playlists/{pid}/tracks"
features_url = "https://api.spotify.com/v1/audio-features"

# For each batch of tracks_ids, fetch their features and transduce it
# into a hash with the track id, its features and the class
# returns = <same return from playlists_tracks_ids>
def tracks_attributes(klass, tracks_ids):
    params = lambda ids: { "ids" : f"{','.join(ids)}" }
    response = requests.get(features_url, headers=headers(TOKEN), params=params(tracks_ids))
    content = response.json()

    if content.get("error"):
        sleep(int(response.headers["retry-after"]))
        response = requests.get(features_url, headers=headers(TOKEN), params=params(tracks_ids))
        content = response.json()

    keys = ['id', 'liveness', 'speechiness', 'instrumentalness', 'valence', 'energy', 'tempo', 'danceability', 'acousticness']
    droped_nan = [c for c in content["audio_features"] if c is not None]
    features = list(map(lambda audio: {"class": klass, **{key: audio[key] for key in keys}}, droped_nan))

    return features

# For each playlist track url, fetch the tracks ids features recursively until the "next"
# response content is null, i.e., there are no more tracks to be paginated.
# If there is a rate api limit error, sleep with the specified retry-after amount
# returns = <same return from playlists_tracks_ids>
def playlist_tracks_ids(klass, url):
    params = { "fields" : "limit,offset,total,next,items(track(id))", "market": "BR" }
    response = requests.get(url, headers=headers(TOKEN), params=params)
    content = response.json()

    if content.get("error"):
        sleep(int(response.headers["retry-after"]))

    next_url = content["next"] 
    ids = list(map(lambda track: track["track"]["id"], content["items"])) 
    attributes = tracks_attributes(klass, ids) 

    if next_url is None:
        return attributes
    return attributes + playlist_tracks_ids(klass, next_url)

# For each array of playlist ids, find their respective tracks ids and merge them together
# returns = [
#     { 
#         "class": "dormir",
#         "id": "17dxXweuShPwv4eXAqJMXz",
#         "valence": 0.117,
#         "energy": 0.0273,
#         "tempo": 68.322,
#         "danceability": 0.0999,
#         "acousticness": 0.91  
#     },
#     { 
#         "class": "dormir",
#         "id": "6TYZYNqAoYfE64csvrZyoy",
#         "valence": 0.033,
#         "energy": 0.00274,
#         "tempo": 134.727,
#         "danceability": 0.153,
#         "acousticness": 0.977 
#     },
# ]
def playlists_tracks_ids(klass, ids):
    return list(reduce(lambda acc, cur: acc + playlist_tracks_ids(klass, url(cur)), ids, []))

# For each entry in PIDS_CLASSES, return each of the tracks attributes in a flat list
# returns = [
#    {
#        "class": "dormir",
#        "id": "17dxXweuShPwv4eXAqJMXz",
#        "valence": 0.117,
#        "energy": 0.0273,
#        "tempo": 68.322,
#        "danceability": 0.0999,
#        "acousticness": 0.91
#    }, {
#         "class": "foco",
#         "id": "1RJ82uG8wlF9y7twIWTOTA",
#         "valence": 0.341, 
#         "energy": 0.149, 
#         "tempo": 144.506,
#         "danceability": 0.473,
#         "acousticness": 0.905
#    }
# ]
def all_tracks_ids():
    return list(map(lambda cur: playlists_tracks_ids(cur["class"], cur["ids"]), PIDS_CLASSES))

with open('result.json', 'w') as fp:
    data = all_tracks_ids()
    json.dump(data, fp, indent=4)
