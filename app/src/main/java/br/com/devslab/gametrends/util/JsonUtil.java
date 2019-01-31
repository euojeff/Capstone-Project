package br.com.devslab.gametrends.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.devslab.gametrends.database.entity.Artwork;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.Screenshot;

public class JsonUtil {


    public static Artwork getArtwork(JSONObject jsonArtwork) throws JSONException {

        Artwork artwork = new Artwork();

        artwork.setApiImageId(jsonArtwork.getString("image_id"));

        return artwork;
    }

    public static List<Artwork> getArtworks(JSONArray jsonArray) throws JSONException {

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
        };

        try {
            game.setRating((int)jsonGame.getDouble("rating"));
        }catch (JSONException e){
            //Do nothing
        };

        game.setId(jsonGame.getInt("id"));
        game.setName(jsonGame.getString("name"));
        game.setSummary(jsonGame.getString("summary"));
        game.setReleaseDate(jsonGame.getLong("first_release_date"));
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
}
