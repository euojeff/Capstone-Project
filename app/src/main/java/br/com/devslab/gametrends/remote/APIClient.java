package br.com.devslab.gametrends.remote;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devslab.gametrends.BuildConfig;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.util.JsonUtil;

public class APIClient {

    public interface ApiClientResponse<T>{

        void onResponse(T parsedContent, String originalJson);

        void onErro();

    }

    private static final String IGDB_KEY = BuildConfig.IGDB_API_KEY;
    private static final String IGDB_BASE_URL = "https://api-v3.igdb.com/";

    private static String LIST_GAMES_URN = "games";

    private static String QUERY_GAMES = IGDB_BASE_URL + LIST_GAMES_URN;

    public static Map<String, String> getRequestHeaders(){
        Map<String, String>  params = new HashMap<>();
        params.put("user-key", IGDB_KEY);
        return params;
    }

    public static void getPopularGamesRequest(@NonNull RequestQueue requestQueue,
                                              @NonNull final ApiClientResponse<List<Game>> listener,
                                              @NonNull final Integer limit,
                                              @NonNull final Integer offSet){

        final Response.Listener<String> response =
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            listener.onResponse(JsonUtil.getGames(response), response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onErro();
                        }
                    }
                };

        final Response.ErrorListener responseErro =
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       listener.onErro();
                    }
                };


        StringRequest request = new StringRequest(Request.Method.POST, QUERY_GAMES, response, responseErro){

            @Override
            public Map<String, String> getHeaders() {
                return APIClient.getRequestHeaders();
            }

            @Override
            public byte[] getBody() {
                String requestBody =
                        "fields name, summary, cover.*, screenshots.*, artworks.*, rating, rating, popularity, first_release_date;\n" +
                                "where platforms = (48) & cover != null & screenshots != null & artworks != null & summary != null &  first_release_date != null;\n" +
                                "sort popularity :desc;\n" +
                                "limit " + limit + ";\n" +
                                "offset " + offSet + ";";

                return requestBody.getBytes();
            }
        };

        requestQueue.add(request);
    }


    public static void getCommingGamesRequest(@NonNull RequestQueue requestQueue,
                                              @NonNull final ApiClientResponse<List<Game>> listener,
                                              @NonNull final Integer limit,
                                              @NonNull final Integer offSet){

        final long unixTimeNow = System.currentTimeMillis() / 1000L;

        final Response.Listener<String> response =
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            listener.onResponse(JsonUtil.getGames(response), response);


                        } catch (JSONException e) {
                            listener.onErro();
                        }
                    }
                };

        final Response.ErrorListener responseErro =
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErro();
                    }
                };

        StringRequest request = new StringRequest(Request.Method.POST, QUERY_GAMES, response, responseErro){

            @Override
            public Map<String, String> getHeaders() {
                return APIClient.getRequestHeaders();
            }

            @Override
            public byte[] getBody() {
                String requestBody =
                        "fields name, summary, cover.*, screenshots.*, artworks.*, rating, first_release_date;\n" +
                                "where platforms = (48) & cover != null & screenshots != null & artworks != null & summary != null & first_release_date != null & first_release_date > " + unixTimeNow + ";\n" +
                                "sort first_release_date :asc;\n" +
                                "limit " + limit + ";\n" +
                                "offset " + offSet + ";";

//                fields first_release_date, name, summary, storyline, cover.*, artworks.*, pulse_count, rating, screenshots.*, videos.*;
//                exclude cover.alpha_channel, cover.animated, cover.height, cover.url, cover.width;
//                exclude screenshots.alpha_channel, screenshots.animated, screenshots.height, screenshots.width, screenshots.url;
//                exclude artworks.alpha_channel, artworks.animated, artworks.height, artworks.width, artworks.url;
//                where cover != null & screenshots != null & artworks != null & platforms = (48) & first_release_date > 1538129354 & artworks.animated = false & artworks.alpha_channel = false &
//                        cover.animated = false & cover.alpha_channel = false &
//                        screenshots.alpha_channel = false & screenshots.animated = false;
//                sort popularity desc;
//                offset 40; limit 10;

                return requestBody.getBytes();
            }
        };

        requestQueue.add(request);
    }

}
