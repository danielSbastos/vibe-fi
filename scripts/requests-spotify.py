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

PARAMS = { "fields" : "limit,offset,total,next,items(track(id))", "market": "BR" }

url = lambda pid: f"https://api.spotify.com/v1/playlists/{pid}/tracks"
headers = lambda token: { "Authorization": f"Bearer {token}" }

def playlist_tracks_ids(url):
    response = requests.get(url, headers=headers(TOKEN), params=PARAMS)
    content = response.json()

    if content.get("error"):
        sleep(response["error"]["retry-after"])

    next_url = content["next"] 
    ids = list(map(lambda track: track["track"]["id"], content["items"])) 

    if next_url is None:
        return ids
    return ids + playlist_tracks_ids(next_url)

def playlists_tracks_ids(playlists_ids):
    return list(reduce(lambda acc, cur: acc + playlist_tracks_ids(url(cur)), playlists_ids, []))

def all_tracks_ids():
    return list(map(lambda cur: { **cur, "tracks_ids": playlists_tracks_ids(cur["ids"]) }, PIDS_CLASSES))

with open('result.json', 'w') as fp:
    json.dump(all_tracks_ids(), fp, indent=4)
