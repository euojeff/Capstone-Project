package br.com.devslab.gametrends.ui;

import br.com.devslab.gametrends.R;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.GameRelation;
import br.com.devslab.gametrends.database.entity.JsonCache;
import br.com.devslab.gametrends.database.viewmodel.FavoritedGamesViewModel;
import br.com.devslab.gametrends.remote.APIClient;
import br.com.devslab.gametrends.util.JsonUtil;
import br.com.devslab.gametrends.util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GamesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment implements CardGameAdapter.CardGameAdapterOnclickHandler {

    public enum QueryTypeEnum{
        POPULAR(1),
        COMING(2),
        FAVORITE(3);

        private Integer id;

        QueryTypeEnum(Integer id){
            this.id = id;
        }

        public Integer getId(){
            return id;
        }
    }

    private static final String ARG_QUERY_TYPE = "ARG_QUERY_TYPE";
    private static final String SAVED_STATE_ITENS = "SAVED_STATE_ITENS";
    private static final String SAVED_STATE_OFFSET = "SAVED_STATE_OFFSET";
    private static final String SAVED_STATE_RV_MANAGER = "SAVED_STATE_RV_MANAGER";


    @BindView(R.id.recycler_games)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CardGameAdapter mAdapter;
    private LinearLayoutManager mManager;

    private static final Integer PAGE_SIZE = 50;
    private Integer mOffset = 0;

    private boolean isLoading = true;

    private JsonCache mJsonCache = null;

    private RequestQueue mRequestQueue;

    private QueryTypeEnum mQueryType;

    private AppDatabase mDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recoverySavedStateItens(savedInstanceState);

        if (getArguments() != null) {
            mQueryType = (QueryTypeEnum) getArguments().getSerializable(ARG_QUERY_TYPE);
            mDb = AppDatabase.getInstance(getContext());

            if(!isFavoriteTab()){
                mDb.jsonCacheDao().load(mQueryType.getId()).observe(this, new Observer<JsonCache>(){

                    @Override
                    public void onChanged(@Nullable JsonCache jsonCache) {
                        if(jsonCache != null){
                            mJsonCache = jsonCache;
                        }
                    }
                });
            }
            mAdapter = new CardGameAdapter(this, getContext(), mRequestQueue);
            mRequestQueue = Volley.newRequestQueue(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_games, container, false);

        ButterKnife.bind(this, rootView);

        mManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });
                mOffset = 0;
                mAdapter.clearGames();
                queryGames();
            }
        });

        recoverySavedStateItens(savedInstanceState);

        if(mOffset == 0){
            queryGames();
        }

        return rootView;
    }

    private void recoverySavedStateItens(Bundle savedInstanceState){
        if(savedInstanceState != null){
            mOffset = savedInstanceState.getInt(SAVED_STATE_OFFSET);
            List<Game> games = (List<Game>) savedInstanceState.getSerializable(SAVED_STATE_ITENS);
            if(mRecyclerView != null) {
                mAdapter.addItens(games);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_STATE_RV_MANAGER));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVED_STATE_OFFSET, mOffset);
        outState.putSerializable(SAVED_STATE_ITENS, (Serializable) mAdapter.getGameList());
        if(mRecyclerView != null
                && mRecyclerView.getLayoutManager() != null){
            outState.putParcelable(SAVED_STATE_RV_MANAGER, mRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    private boolean isFavoriteTab(){
        return QueryTypeEnum.FAVORITE.equals(mQueryType);
    }

    private boolean isPopularTab(){
        return QueryTypeEnum.POPULAR.equals(mQueryType);
    }

    private boolean isComingTab(){
        return QueryTypeEnum.COMING.equals(mQueryType);
    }

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

    private void queryGames(){

        isLoading = true;
        updateRefreshing();

        final APIClient.ApiClientResponse<List<Game>> listener = new APIClient.ApiClientResponse<List<Game>>(){


            @Override
            public void onResponse(List<Game> games, String originalJson) {

                mAdapter.addItens(games);
                isLoading = false;

                if(mOffset == 0){
                    updateCache(originalJson);
                }

                mOffset = mOffset + PAGE_SIZE;
                updateRefreshing();
            }

            @Override
            public void onErro() {
                Log.d("ERRO", "ERRO REQUEST");

                if(mJsonCache != null
                        && mJsonCache.getContent() != null){

                    mOffset = 0;

                    try {
                        List<Game> games = JsonUtil.getGames(mJsonCache.getContent());
                        mAdapter.addItens(games);
                        Util.showSnack(getView(), getContext().getResources().getString(R.string.offline));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                isLoading = false;
                updateRefreshing();
            }
        };


        if(isPopularTab()){

            APIClient.getPopularGamesRequest(mRequestQueue, listener, PAGE_SIZE, mOffset);

        }else if(isComingTab()){

            APIClient.getCommingGamesRequest(mRequestQueue, listener, PAGE_SIZE, mOffset);

        }else if(isFavoriteTab()){

            FavoritedGamesViewModel viewModel = ViewModelProviders.of(this).get(FavoritedGamesViewModel.class);
            viewModel.getFavoritedGames().observe(this, new Observer<List<GameRelation>>() {
                @Override
                public void onChanged(@Nullable List<GameRelation> gamesRelations) {
                    if(gamesRelations != null){
                        List<Game> games = new ArrayList<>();
                        for(GameRelation relation: gamesRelations){
                            Game game = relation.game;
                            game.setArtworksList(relation.artworkList);
                            game.setScreenshotsList(relation.screenshotList);
                            games.add(game);
                        }
                        mAdapter.clearGames();
                        mAdapter.addItens(games);
                        isLoading = false;
                        updateRefreshing();
                    }
                }
            });
        }
    }

    private OnFragmentInteractionListener mListener;


    public GamesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param queryTypeEnum Parameter 1.
     * @return A new instance of fragment GamesFragment.
     */
    public static GamesFragment newInstance(QueryTypeEnum queryTypeEnum) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUERY_TYPE, queryTypeEnum);
        fragment.setArguments(args);
        return fragment;
    }



    private void updateCache(String json){
        if(mJsonCache != null){
            mJsonCache.setContent(json);

            new AsyncTask<JsonCache, Void, Void>(){

                @Override
                protected Void doInBackground(JsonCache... cache) {

                    mDb.jsonCacheDao().updateAll(cache);

                    return null;
                }
            }.execute(mJsonCache);

        }
    }

    public void onSelectGame(Game game, View transitionView) {
        if (mListener != null) {
            mListener.onRequestOpenGameDetail(game, transitionView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCardClick(Game game, View transitionView) {
        onSelectGame(game, transitionView);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRequestOpenGameDetail(Game game, View transitionView);
    }
}
