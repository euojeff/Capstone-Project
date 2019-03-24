package br.com.devslab.gametrends.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.widget.PulseAppWidget;


// Based: https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
public class PulseAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private AppDatabase mDb;

    private List<PulseArticle> mArticles;

    public PulseAppWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mDb = AppDatabase.getInstance(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mArticles =  mDb.gameDao().latestArticles();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mArticles == null ? 0 : mArticles.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || mArticles == null) {
            return null;
        }

        int size = mContext.getResources().getDimensionPixelSize(R.dimen.pulse_thumb_widget_size);

        PulseArticle pulseArticle = mArticles.get(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.pulse_app_widget_list_item);
        rv.setTextViewText(R.id.widget_item_label, mArticles.get(position).getTitle());
        configureClick(rv, pulseArticle);


        try {

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .autoClone()
                    .circleCrop();

            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(pulseArticle.getImgUrl())
                    .apply(requestOptions)
                    .submit(size, size)
                    .get();

            rv.setImageViewBitmap(R.id.pulse_thumbs_iv, bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return rv;
    }

    private void configureClick(RemoteViews rv, PulseArticle pulseArticle){
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putString(PulseAppWidget.EXTRAS_PULSE_ARTICLE_URL, pulseArticle.getArticleUrl());
        intent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item_container, intent);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
