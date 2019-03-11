package br.com.devslab.gametrends.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.database.entity.Screenshot;
import br.com.devslab.gametrends.remote.APIClient;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeff on 11/03/2019.
 */
class CardPulseAdapter extends RecyclerView.Adapter <CardPulseAdapter.CardScreenshotHolder> {

    static class CardScreenshotHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_screenshot) @Nullable
        CardView mCard;
        @BindView(R.id.card_img) @Nullable
        ImageView mImage;

        public CardScreenshotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CardPulseAdapterOnclickHandler {
        void onPulseClick(PulseArticle screenshot);
    }

    private List<PulseArticle> listPulses;
    private CardPulseAdapterOnclickHandler mHandler;
    private Context mContext;

    public void addItens(List<PulseArticle> moreItens) {
        listPulses.addAll(moreItens);
        notifyDataSetChanged();
    }

    public void clearPulse(){
        Integer size = listPulses.size();
        listPulses.clear();
        notifyItemRangeRemoved(0, size);
    }

    public CardPulseAdapter(CardPulseAdapterOnclickHandler handler, Context context){
        this.mHandler = handler;
        this.mContext = context;
        this.listPulses = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return listPulses.size();
    }

    @Override
    public CardPulseAdapter.CardScreenshotHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.card_screenshot, viewGroup, false);

        return new CardPulseAdapter.CardScreenshotHolder(view);
    }


    @Override
    public void onBindViewHolder(final CardPulseAdapter.CardScreenshotHolder holder, int i) {
        final int posicao = i;

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.onPulseClick(listPulses.get(posicao));
            }
        });

        PulseArticle pulse = listPulses.get(i);

        Glide.with(mContext).clear(holder.mImage);
        Glide.with(mContext).load(pulse.getImgUrl()).into(holder.mImage);
    }
}