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
import br.com.devslab.gametrends.database.entity.Screenshot;
import br.com.devslab.gametrends.remote.APIClient;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeff on 29/01/2019.
 */
class CardScreenshotAdapter extends RecyclerView.Adapter <CardScreenshotAdapter.CardScreenshotHolder> {

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

    public interface CardScreenshotAdapterOnclickHandler {
        void onScreenshotClick(Screenshot screenshot);
    }

    private List<Screenshot> listaScreenshots;
    private CardScreenshotAdapterOnclickHandler mHandler;
    private Context mContext;

    public void addItens(List<Screenshot> moreItens) {
        listaScreenshots.addAll(moreItens);
        notifyDataSetChanged();
    }

    public void clearScreeshot(){
        Integer size = listaScreenshots.size();
        listaScreenshots.clear();
        notifyItemRangeRemoved(0, size);
    }

    public CardScreenshotAdapter(CardScreenshotAdapterOnclickHandler handler, Context context){
        this.mHandler = handler;
        this.mContext = context;
        this.listaScreenshots = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return listaScreenshots.size();
    }

    @Override
    public CardScreenshotAdapter.CardScreenshotHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.card_screenshot, viewGroup, false);

        return new CardScreenshotAdapter.CardScreenshotHolder(view);
    }


    @Override
    public void onBindViewHolder(final CardScreenshotAdapter.CardScreenshotHolder holder, int i) {
        final int posicao = i;

        String idImg;

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.onScreenshotClick(listaScreenshots.get(posicao));
            }
        });

        Screenshot screenshot = listaScreenshots.get(i);
        idImg = screenshot.getApiImageId();

        String urlImg = APIClient.getImgUrl(idImg);
        Glide.with(mContext).clear(holder.mImage);
        Glide.with(mContext).load(urlImg).into(holder.mImage);
    }
}