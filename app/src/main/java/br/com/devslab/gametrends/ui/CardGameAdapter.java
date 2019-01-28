package br.com.devslab.gametrends.ui;

import br.com.devslab.gametrends.R;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.remote.APIClient;
import br.com.devslab.gametrends.util.Util;
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
        @BindView(R.id.tv_card_title) @Nullable
        TextView title;

        @BindView(R.id.tv_card_rating) @Nullable
        TextView rating;

        public CardGameHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CardGameAdapterOnclickHandler {
        void onCardClick(Game game);
    }

    private List<Game> listaGames;
    private CardGameAdapterOnclickHandler mHandler;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public void addItens(List<Game> moreItens) {
        listaGames.addAll(moreItens);
        notifyDataSetChanged();
    }

    public void clearGames(){
        Integer size = listaGames.size();
        listaGames.clear();
        notifyItemRangeRemoved(0, size);
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
    public void onBindViewHolder(final CardGameAdapter.CardGameHolder holder, int i) {
        final int posicao = i;

        String idCover = "-1";

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.onCardClick(listaGames.get(posicao));
            }
        });

        Game game = listaGames.get(i);
        idCover = game.getCoverId();
        holder.title.setText(game.getName());

        String rateLimit = " / " + mContext.getResources().getString(R.string.rating_limit);
        String none = mContext.getResources().getString(R.string.none);
        if(game.getRating() == null){
            holder.rating.setText(none + rateLimit);
        }else{
            holder.rating.setText(game.getRating() + rateLimit);
        }

        String urlImg = APIClient.getImgUrl(idCover);
        Glide.with(mContext).clear(holder.cover);
        Glide.with(mContext).load(urlImg).into(holder.cover);

    }
}