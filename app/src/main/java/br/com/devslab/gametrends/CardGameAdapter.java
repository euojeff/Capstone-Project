package br.com.devslab.gametrends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.devslab.gametrends.util.APIClient;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeff on 07/01/2019.
 */
class CardGameAdapter extends RecyclerView.Adapter <CardGameAdapter.CardGameHolder> {

    static class CardGameHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_game) @Nullable
        CardView mCard;
        @BindView(R.id.card_cover) @Nullable
        ImageView cover;

        StringRequest jsObjRequest;

        public CardGameHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CardGameAdapterOnclickHandler {
        void onCardClick(String jsonFilme);
    }

    private ArrayList<String> listaGames;
    private CardGameAdapterOnclickHandler mHandler;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public void addItens(ArrayList<String> moreItens) {
        listaGames.addAll(moreItens);
        notifyDataSetChanged();
    }

    public void clearGames(){
        listaGames.clear();
        notifyDataSetChanged();
    }

    public CardGameAdapter(CardGameAdapterOnclickHandler handler, Context context, RequestQueue requestQueue){
        this.mHandler = handler;
        this.mContext = context;
        this.listaGames = new ArrayList<>();
        this.mRequestQueue = requestQueue;
    }

    @Override
    public int getItemCount() {
        return listaGames.size();
    }

    @Override
    public CardGameAdapter.CardGameHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cartd_game, viewGroup, false);

        return new CardGameAdapter.CardGameHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull CardGameHolder holder) {
        super.onViewRecycled(holder);

        Glide.with(mContext).clear(holder.cover);

        if(holder.jsObjRequest != null){
            holder.jsObjRequest.cancel();
        }
    }

    @Override
    public void onBindViewHolder(final CardGameAdapter.CardGameHolder holder, int i) {
        final int posicao = i;

        String idCover = "-1";

//            FilmeJsonHelper filme = new FilmeJsonHelper(listaFilmes.get(i));

            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mHandler.onCardClick(listaFilmes.get(posicao));
                }
            });

        try {
            JSONObject game = new JSONObject(listaGames.get(i));
            idCover = game.getString("cover");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String cover = idCover;

        holder.jsObjRequest = APIClient.getCoverGameRequest(new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    String imgId = new JSONArray(response).getJSONObject(0).getString("image_id");

                    String urlImg = "https://images.igdb.com/igdb/image/upload/t_original/" + imgId + ".jpg";

                    Glide.with(mContext).load(urlImg).into(holder.cover);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERRO", "ERRO REQUEST");
                        error.printStackTrace();
                    }
                }
        , Integer.valueOf(cover));

        mRequestQueue.add(holder.jsObjRequest);
    }
}