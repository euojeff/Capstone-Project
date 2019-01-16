package br.com.devslab.gametrends;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.com.devslab.gametrends.util.APIClient;
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
        POPULAR,
        COMMING,
        FAVORITE
    };

    private static final String ARG_QUERY_TYPE = "ARG_QUERY_TYPE";


    @BindView(R.id.recycler_games)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CardGameAdapter mAdapter;
    private LinearLayoutManager mManager;

    private static final Integer PAGE_SIZE = 50;
    private Integer offset = 0;

    private boolean isLoading = true;

    RequestQueue mRequestQueue;

    private QueryTypeEnum mQueryType;

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


        final Response.Listener<String> response =
                new Response.Listener<String>() {

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
                };

        final Response.ErrorListener responseErro =
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERRO", "ERRO REQUEST");
                        error.printStackTrace();
                        isLoading = false;
                    }
                };

        if(QueryTypeEnum.POPULAR.equals(mQueryType)){

            APIClient.getPopularGamesRequest(mRequestQueue, response, responseErro, PAGE_SIZE, offset);

        }else if(QueryTypeEnum.COMMING.equals(mQueryType)){

            APIClient.getCommingGamesRequest(mRequestQueue, response, responseErro, PAGE_SIZE, offset);

        }else if(QueryTypeEnum.FAVORITE.equals(mQueryType)){

            APIClient.getPopularGamesRequest(mRequestQueue, response, responseErro, PAGE_SIZE, offset);
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
    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance(QueryTypeEnum queryTypeEnum) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUERY_TYPE, queryTypeEnum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQueryType = (QueryTypeEnum) getArguments().getSerializable(ARG_QUERY_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_games, container, false);

        ButterKnife.bind(this, rootView);

        mRequestQueue = Volley.newRequestQueue(getContext());
        mAdapter = new CardGameAdapter(this, getContext(), mRequestQueue);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        mManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });
                offset = 0;
                mAdapter.clearGames();
                queryGames();
            }
        });

        queryGames();

        return rootView;
    }

    public void onSelectGame(String jsonGame) {
        if (mListener != null) {
            mListener.onRequestOpenGameDetail(jsonGame);
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
    public void onCardClick(String jsonFilme) {
        onSelectGame(jsonFilme);
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
        void onRequestOpenGameDetail(String json);
    }
}
