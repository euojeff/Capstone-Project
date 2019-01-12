package br.com.devslab.gametrends.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.devslab.gametrends.BuildConfig;

public class APIClient {

    private static final String IGDB_KEY = BuildConfig.IGDB_API_KEY;
    private static final String IGDB_BASE_URL = "https://api-v3.igdb.com";

    private static String LIST_GAMES_URN = "games";

    public static Map<String, String> getRequestHeaders(){
        Map<String, String>  params = new HashMap<>();
        params.put("user-key", IGDB_KEY);
        return params;
    }

    public static StringRequest getPopularGamesRequest(@NonNull Response.Listener<String> responseListener, @NonNull Response.ErrorListener responseErrorListener, @NonNull final Integer limit, @NonNull final Integer offSet){
        return new StringRequest(Request.Method.POST, "https://api-v3.igdb.com/games", responseListener, responseErrorListener){

            @Override
            public Map<String, String> getHeaders() {
                return APIClient.getRequestHeaders();
            }

            @Override
            public byte[] getBody() {
                String requestBody =
                        "fields name, summary, cover, rating, screenshots, videos, rating, popularity;\n" +
                                "where cover != null & screenshots != null;\n" +
                                "sort popularity :desc;\n" +
                                "limit " + limit + ";\n" +
                                "offset " + offSet + ";";

                return requestBody.getBytes();
            }
        };
    }

    public static StringRequest getCoverGameRequest(@NonNull Response.Listener<String> responseListener, @NonNull Response.ErrorListener responseErrorListener, @NonNull final Integer id){
        return new StringRequest(Request.Method.POST, "https://api-v3.igdb.com/covers", responseListener, responseErrorListener){

            @Override
            public Map<String, String> getHeaders() {
                return APIClient.getRequestHeaders();
            }

            @Override
            public byte[] getBody() {
                String requestBody =
                        "fields *; where id=" + id + ";";

                return requestBody.getBytes();
            }
        };
    }
}
