package br.com.devslab.gametrends.util;

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

    public static StringRequest getPopularGamesRequest(Response.Listener<String> responseListener, Response.ErrorListener responseErrorListener, final Integer limit, final Integer offSet){
        return new StringRequest(Request.Method.POST, "https://api-v3.igdb.com/games", responseListener, responseErrorListener){

            @Override
            public Map<String, String> getHeaders() {
                return APIClient.getRequestHeaders();
            }

            @Override
            public byte[] getBody() {
                String requestBody =
                        "fields name, summary,cover, rating, screenshots, platforms, videos, rating, popularity;\n" +
                                "where cover != null & screenshots != null & platforms = (48, 49);\n" +
                                "sort rating :desc;\n" +
                                "limit " + limit + ";\n" +
                                "offset " + offSet + ";";

                return requestBody.getBytes();
            }
        };
    }
}
