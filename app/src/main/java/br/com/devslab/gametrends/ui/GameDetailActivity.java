package br.com.devslab.gametrends.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.GameRelation;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.database.entity.Screenshot;
import br.com.devslab.gametrends.remote.APIClient;
import br.com.devslab.gametrends.util.JsonUtil;
import br.com.devslab.gametrends.util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDetailActivity extends AppCompatActivity implements CardScreenshotAdapter.CardScreenshotAdapterOnclickHandler {

    public static String EXTRA_GAME = "EXTRA_GAME";

    private Game mGame;
    private List<Screenshot> mScreenshots;
    private CardScreenshotAdapter mCardScreenshotAdapter;
    private AppDatabase mDb;
    private boolean mFavorited = false;
    private Context mContext;
    private RequestQueue mRequestQueue;

    @BindView(R.id.img_parallax)
    ImageView mParallaxIV;
    @BindView(R.id.img_cover)
    ImageView mCoverIV;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.rating)
    TextView mRating;
    @BindView(R.id.sumary)
    TextView mSumary;
    @BindView(R.id.recycler_screenshots)
    RecyclerView mScreenshotsRecycler;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        mRequestQueue = Volley.newRequestQueue(this);

        mDb = AppDatabase.getInstance(this);
        mGame = (Game)getIntent().getSerializableExtra(EXTRA_GAME);
        loadParallax(mGame);
        loadCover(mGame);
        loadPulse(mGame);
        populateData(mGame);
        configScreenshots(mGame);
        configFab();

        mDb.gameDao().loadGame(mGame.getId()).observe(this, new Observer<GameRelation>() {
            @Override
            public void onChanged(@Nullable GameRelation game) {
                if(game != null){
                    mFavorited = true;
                    fab.setImageResource( R.drawable.ic_clear_white);
                }else{
                    mFavorited = false;
                    fab.setImageResource(R.drawable.ic_add_white);
                }
            }
        });
    }

    private void loadPulse(Game game){

        final APIClient.ApiClientResponse<List<PulseArticle>> listener = new APIClient.ApiClientResponse<List<PulseArticle>>(){


            @Override
            public void onResponse(List<PulseArticle> articles, String originalJson) {

                if(articles != null){
                    for(PulseArticle article : articles){
                        Log.d("Article", article.getSummary());
                    }
                }
            }

            @Override
            public void onErro() {
                Log.d("ERRO", "ERRO REQUEST");

            }
        };

        APIClient.getGameArticlePulse(mRequestQueue, listener, game.getId());

    }

    private void configFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(mFavorited){
                            mDb.gameDao().deleteAll(mGame);
                        }else{
                            mDb.gameDao().insertGameWithRelations(mGame);
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void configScreenshots(Game game){

        mScreenshots = new ArrayList<>();
        mScreenshots.addAll(game.getScreenshotsList());
        mCardScreenshotAdapter = new CardScreenshotAdapter(this, this);
        mScreenshotsRecycler.setAdapter(mCardScreenshotAdapter);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mScreenshotsRecycler.setLayoutManager(horizontalLayoutManagaer);

        mCardScreenshotAdapter.addItens(mScreenshots);
    }

    private void populateData(Game game){

        mTitle.setText(game.getName());
        mReleaseDate.setText("November 14, 2018");
        mRating.setText("Rating: 99/100");
        mSumary.setText(game.getSummary());
    }

    private void loadParallax(Game game){

        if(game.getArtworksList() != null
                && game.getArtworksList().size() > 0){

            int randIndex = new Random().nextInt(game.getArtworksList().size());

            String idImg = game.getArtworksList().get(randIndex).getApiImageId();
            String urlImg = APIClient.getImgUrl(idImg);
            Util.loadImg(urlImg, mParallaxIV);
        }
    }

    private void loadCover(Game game){

        String urlImg = APIClient.getImgUrl(game.getCoverId());
        Util.loadImg(urlImg, mCoverIV);
    }

    @Override
    public void onScreenshotClick(Screenshot screenshot) {
        //Todo implement Fullscreen after Capstone
        Log.d(GameDetailActivity.class.getName(), "Clicked Screenshot");
    }
}
