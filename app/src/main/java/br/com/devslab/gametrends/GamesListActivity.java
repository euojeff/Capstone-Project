package br.com.devslab.gametrends;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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


public class GamesListActivity extends AppCompatActivity implements CardGameAdapter.CardGameAdapterOnclickHandler {

    private CardGameAdapter mAdapter;
    private LinearLayoutManager mManager;

    @BindView(R.id.recycler_games)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    RequestQueue mRequestQueue;

    private boolean isLoading = true;

    private static final Integer PAGE_SIZE = 5;

    private Integer offset = 0;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int totalItemCount = mManager.getItemCount();
            int lastVisibleItemPosition = mManager.findLastVisibleItemPosition();

            if (!isLoading) {
                if (totalItemCount - 1 == lastVisibleItemPosition) {
                    queryGames();
                }
            }
        }
    };

    private void updateRefreshing() {
        mSwipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        ButterKnife.bind(this);

        mRequestQueue = Volley.newRequestQueue(this);
        mAdapter = new CardGameAdapter(this, this, mRequestQueue);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        mManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                offset = 0;
                mAdapter.clearGames();
                queryGames();
            }
        });

        queryGames();

    }

    private void queryGames(){

        isLoading = true;
        updateRefreshing();

        StringRequest request = APIClient.getPopularGamesRequest(new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray itens = new JSONArray(response);
                    ArrayList<String> newItens = new ArrayList<>();

                    for(int i = 0; i < itens.length(); i++){
                        newItens.add(itens.getJSONObject(i).toString());
                    }

                    mAdapter.addItens(newItens);
                    isLoading = false;

                    offset = offset + PAGE_SIZE;
                    updateRefreshing();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRO", "ERRO REQUEST");
                error.printStackTrace();
                isLoading = false;
            }
        }, PAGE_SIZE, offset);


        mRequestQueue.add(request);
    }

    @Override
    public void onCardClick(String jsonFilme) {

    }
}
