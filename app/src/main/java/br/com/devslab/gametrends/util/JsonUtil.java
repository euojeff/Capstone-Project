package br.com.devslab.gametrends.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.devslab.gametrends.data.Game;

public class JsonUtil {

    public static Game getGame(JSONObject jsonGame) throws JSONException {

        String idCover;
        Game game = new Game();

        Log.i("COVER", jsonGame.toString());

        try {
            idCover = jsonGame.getJSONObject("cover").getString("image_id");
        }catch (JSONException e){
            //Sometime the api get an Integer in this. We will set null.
            idCover = null;
        };

        game.setCoverId(idCover);

        return game;
    }


    public static List<Game> getGames(String json) throws JSONException {

        JSONArray itens = new JSONArray(json);
        ArrayList<Game> newItens = new ArrayList<>();

        for(int i = 0; i < itens.length(); i++){
            newItens.add(getGame(itens.getJSONObject(i)));
        }


        return newItens;
    }
}
