package br.com.devslab.gametrends.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;
import java.util.concurrent.Phaser;

import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.GameRelation;
import br.com.devslab.gametrends.database.entity.JsonCache;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.ui.GamesFragment;
import br.com.devslab.gametrends.widget.WidgetService;

public class SyncDataJobService extends JobService {

    AppDatabase mDB;
    RequestQueue mRequestQueue;
    final Phaser phaser = new Phaser(1);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        mDB = AppDatabase.getInstance(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        updateComingCache();
        updatePopularCache();
        updatePulse();
        waitAndFinallyJob(jobParameters);

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void waitAndFinallyJob(final JobParameters jobParameters){

        new Thread(){
            @Override
            public void run() {

                phaser.arriveAndAwaitAdvance();
                WidgetService.startActionUpdateWidget(getBaseContext());
                jobFinished(jobParameters, false);
            }
        }.start();

    }

    private void updatePopularCache(){

        phaser.register();

        final APIClient.ApiClientResponse<List<Game>> listenerPopular = new APIClient.ApiClientResponse<List<Game>>(){


            @Override
            public void onResponse(List<Game> games, String originalJson) {

                updateCache(originalJson, GamesFragment.QueryTypeEnum.POPULAR.getId());
            }

            @Override
            public void onErro() {

                phaser.arriveAndDeregister();
            }
        };

        APIClient.getPopularGamesRequest(mRequestQueue, listenerPopular, 50, 0);
    }

    private void updateComingCache(){

        phaser.register();

        final APIClient.ApiClientResponse<List<Game>> listenerComing = new APIClient.ApiClientResponse<List<Game>>(){


            @Override
            public void onResponse(List<Game> games, String originalJson) {

                updateCache(originalJson, GamesFragment.QueryTypeEnum.COMING.getId());
            }

            @Override
            public void onErro() {

                phaser.arriveAndDeregister();
            }
        };

        APIClient.getCommingGamesRequest(mRequestQueue, listenerComing, 50, 0);
    }

    private void updatePulse(){
        final LiveData<List<GameRelation>> data =  mDB.gameDao().loadFavorited();

        data.observeForever(new Observer<List<GameRelation>>() {
            @Override
            public void onChanged(@Nullable List<GameRelation> gameRelations) {

                updatePulse(gameRelations);

                data.removeObserver(this);
            }
        });
    }

    private void updatePulse(List<GameRelation> gameRelations){

        if(gameRelations != null){
            for(GameRelation gameRelation: gameRelations){
                loadSavePulse(gameRelation.game);
            }
        }
    }

    private void loadSavePulse(final Game game){

        phaser.register();
        final AppDatabase db = mDB;

        final APIClient.ApiClientResponse<List<PulseArticle>> listener = new APIClient.ApiClientResponse<List<PulseArticle>>(){


            @Override
            public void onResponse(final List<PulseArticle> articles, String originalJson) {

                if(articles != null){

                    game.setPulseArticleList(articles);

                    new AsyncTask<Void, Void, Void>(){

                        @Override
                        protected Void doInBackground(Void... voids) {
                            db.gameDao().insertPulse(articles, game);
                            phaser.arriveAndDeregister();
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onErro() {
                phaser.arriveAndDeregister();
            }
        };

        APIClient.getGameArticlePulse(mRequestQueue, listener, game.getId());
    }




    private void updateCache(String json, Integer queryType){
        JsonCache jsonCache = new JsonCache( queryType, json);

        new AsyncTask<JsonCache, Void, Void>(){

            @Override
            protected Void doInBackground(JsonCache... cache) {

                mDB.jsonCacheDao().updateAll(cache);
                phaser.arriveAndDeregister();

                return null;
            }
        }.execute(jsonCache);
    }
}
