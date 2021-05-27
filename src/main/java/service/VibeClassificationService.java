package service;

import dao.VibeDAO;
import dao.VibeSeedDAO;
import dao.VibeTemplateDAO;
import lib.Classifier;
import model.Features;
import model.Vibe;
import model.VibeSeed;
import model.VibeTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

public class VibeClassificationService {
    private final VibeDAO vibeDao;
    private final VibeSeedDAO vibeSeedDao;

    public VibeClassificationService() {
        this.vibeDao = new VibeDAO();
        this.vibeSeedDao = new VibeSeedDAO();
    }

    @SuppressWarnings("unchecked")
    public Object generate(Request request, Response response) {
        SpotifyService spotifyService = new SpotifyService();

        List<String> templateIds = new ArrayList<>();

        String[] parts = request.body().split("=|&");
        for (int i = 1; i < parts.length; i += 2) {
            templateIds.add(parts[i]);
        }

        JSONArray tracks = (JSONArray) spotifyService.getUserTopTracks(request.cookie("access_token")).get("tracks");

        Features[] features = new Features[tracks.size()];
        int i = 0;
        for (Object track : tracks) {
            JSONObject jsonFeatures = new JSONObject((HashMap<String, Object>) ((JSONObject) track).get("features"));

            Features _features = new Features(
                    null,
                    (Double) jsonFeatures.get("tempo"),
                    (Double) jsonFeatures.get("valence"),
                    (Double) jsonFeatures.get("liveness"),
                    (Double) jsonFeatures.get("acousticness"),
                    (Double) jsonFeatures.get("danceability"),
                    (Double) jsonFeatures.get("energy"),
                    (Double) jsonFeatures.get("speechiness"),
                    (Double) jsonFeatures.get("instrumentalness")
            );
            _features.trackId = (String) ((JSONObject) track).get("id");
            features[i] = _features;
            i++;
        }

        Classifier classifier = new Classifier(features);
        List<Map<String, Object>> result = classifier.classify();
        List<Map<String, Object>> filteredResult = result.stream().filter((r) ->  templateIds.contains((String) r.get("class"))).collect(Collectors.toList());

        String userId = request.cookie("user_id");
        VibeTemplateDAO vibeTemplateDAO = new VibeTemplateDAO();

        VibeTemplate vibeTemplate;
        Vibe vibe;
        for (String templateId : templateIds) {
            vibeTemplate = vibeTemplateDAO.getVibeTemplate(templateId);

            if (classifier.foundClasses.contains(vibeTemplate.getId())) {
                vibe = getOrCreateVibe(userId, vibeTemplate);
                createVibeSeeds(vibe, templateId, filteredResult);
            }
        }

        response.redirect("/screens/perfil/");
        return "ok";
    }

    // create vibeseeds to their vibe. The seed identifier will be the track identifier from Spotify
    private void createVibeSeeds(Vibe vibe, String classifiedClass, List<Map<String, Object>> features) {
        VibeSeed vibeSeed;
        // go through all the features and if the current one`s class
        // is equal to the class (templateID) we are currently creating seeds
        // then create a vibeseed using passing in the vibe id, identifier (track id) and type
        for (Map<String, Object> _features : features) {
            if (_features.get("class").equals(classifiedClass)) {
                try {
                    vibeSeed = new VibeSeed(
                            vibe.getId(),
                            ((Features) _features.get("feature")).trackId,
                            "track"
                    );
                    vibeSeedDao.createVibeSeed(vibeSeed);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }

    private Vibe getOrCreateVibe(String userId, VibeTemplate vibeTemplate) {
        Vibe vibe = null;
        Vibe[] vibes = vibeDao.getUserVibes(userId);

        // fetch user vibe from the given class
        // if it is null, then create the vibe based on the template
        // if otherwise, simply return the found vibe to be later
        // used on the vibeseed creation process
        if (vibes != null) {
            List<Vibe> filteredVibes = Arrays
                    .stream(vibes)
                    .filter((v) -> v.getOriginTemplateId() != null && v.getOriginTemplateId().equals(vibeTemplate.getId()))
                    .collect(Collectors.toList());
            if (!filteredVibes.isEmpty()) {
                vibe = filteredVibes.get(0);
            }
        }

        if (vibe == null) {
            vibe = new Vibe(
                    userId, vibeTemplate.getId(), vibeTemplate.getName(),
                    vibeTemplate.getDescription(), vibeTemplate.getMinFeatures(),
                    vibeTemplate.getMinFeatures()
            );
            vibeDao.createVibe(vibe);
        }

        return vibe;
    }
}
