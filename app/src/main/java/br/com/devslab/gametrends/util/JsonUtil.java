package br.com.devslab.gametrends.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devslab.gametrends.database.entity.Artwork;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.database.entity.Screenshot;

public class JsonUtil {


    public static Artwork getArtwork(JSONObject jsonArtwork) throws JSONException {

        Artwork artwork = new Artwork();

        artwork.setApiImageId(jsonArtwork.getString("image_id"));

        return artwork;
    }

    public static List<Artwork> getArtworks(JSONArray jsonArray) {

        List<Artwork> newItens = new ArrayList<>();

        try{
            for(int i = 0; i < jsonArray.length(); i++){
                newItens.add(getArtwork(jsonArray.getJSONObject(i)));
            }
        }catch (JSONException e){

        }

        return newItens;
    }


    public static Screenshot getScreenshot(JSONObject json) throws JSONException {

        Screenshot screenshot = new Screenshot();

        screenshot.setApiImageId(json.getString("image_id"));

        return screenshot;
    }

    public static List<Screenshot> getScreenshots(JSONArray jsonArray) throws JSONException {

        List<Screenshot> newItens = new ArrayList<>();

        try {
            for(int i = 0; i < jsonArray.length(); i++){
                newItens.add(getScreenshot(jsonArray.getJSONObject(i)));
            }
        }catch (JSONException e){

        }

        return newItens;
    }

    public static Game getGame(JSONObject jsonGame) throws JSONException {

        String idCover;
        Game game = new Game();

        Log.d("GAME", jsonGame.toString());

        try {
            idCover = jsonGame.getJSONObject("cover").getString("image_id");
        }catch (JSONException e){
            //Sometime the api get an Integer in this. We will set null.
            idCover = null;
        }

        try {
            game.setRating((int)jsonGame.getDouble("rating"));
        }catch (JSONException e){
            //Do nothing
        }

        game.setId(jsonGame.getInt("id"));
        game.setName(jsonGame.getString("name"));
        game.setSummary(jsonGame.getString("summary"));
        game.setReleaseDate(Util.unixTimeToTimeMilis(jsonGame.getLong("first_release_date")));
        game.setCoverId(idCover);
        game.setArtworksList(getArtworks(jsonGame.getJSONArray("artworks")));
        game.setScreenshotsList(getScreenshots(jsonGame.getJSONArray("screenshots")));

        return game;
    }

    public static List<Game> getGames(String json) throws JSONException {

        JSONArray itens = new JSONArray(json);
        ArrayList<Game> newItens = new ArrayList<>();

        for(int i = 0; i < itens.length(); i++){
            Game game = getGame(itens.getJSONObject(i));

            if(!Util.isEmptyOrNull(game.getCoverId())
                    && !Util.isEmptyOrNull(game.getArtworksList())
                    && !Util.isEmptyOrNull(game.getScreenshotsList())){
                //discard without imgs
                newItens.add(game);
            }

            Log.d("GAME INDEX", i + "");
        }


        return newItens;
    }

    public static PulseArticle getPulseArticle(JSONObject json) {

        PulseArticle article = new PulseArticle();

        try{

            String uid = json.getString("uid");
            String imgUrl = json.getString("image");
            String title = json.getString("title");
            String author = json.getString("author");
            String summary = json.getString("summary");
            String articleUrl = json.getJSONObject("website").getString("url");
            Long publishedAt = Util.unixTimeToTimeMilis(json.getLong("published_at"));

            article.setUniqueId(uid);
            article.setTitle(title);
            article.setAuthor(author);
            article.setImgUrl(imgUrl);
            article.setSummary(summary);
            article.setArticleUrl(articleUrl);
            article.setPublishedDate(publishedAt);

        }catch (JSONException e){
            article = null;
        }


        return article;
    }

    public static List<PulseArticle> getPulseArticles(String json) throws JSONException {

        JSONArray pulseGroups = new JSONArray(json);
        ArrayList<PulseArticle> newItens = new ArrayList<>();

        HashMap<String, JSONObject> distinctPulse = new HashMap<>();

        for(int i = 0; i < pulseGroups.length(); i++){

            JSONArray pulses = pulseGroups.getJSONObject(i).getJSONArray("pulses");

            for(int j = 0; j < pulses.length(); j++){

                try{
                    JSONObject pulse = pulses.getJSONObject(j);
                    String uid = pulse.getString("uid");

                    if(!distinctPulse.containsKey(uid)){
                        distinctPulse.put(uid, pulse);
                    }
                }catch (JSONException e){
                    //Sometime the api gets an Integer in this. We will go forward... And exclude this element.
                    continue;
                }
            }
        }

        for(Map.Entry<String, JSONObject> item: distinctPulse.entrySet()){

            PulseArticle pulse = getPulseArticle(item.getValue());

            if(pulse != null){
                newItens.add(pulse);
            }
        }

        Collections.sort(newItens, new Comparator<PulseArticle>() {
            @Override
            public int compare(PulseArticle o1, PulseArticle o2) {
                return o1.getPublishedDate() > o2.getPublishedDate() ? -1 : 1;
            }
        });

        return newItens;
    }
}
