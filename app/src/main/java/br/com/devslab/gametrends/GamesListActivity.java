package br.com.devslab.gametrends;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.com.devslab.gametrends.util.APIClient;
import butterknife.BindView;
import butterknife.ButterKnife;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            GamesFragment gf = new GamesFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.game_list_container, gf)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
