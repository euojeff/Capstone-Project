package br.com.devslab.gametrends.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Random;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.remote.APIClient;
import br.com.devslab.gametrends.util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDetailActivity extends AppCompatActivity {

    public static String EXTRA_GAME = "EXTRA_GAME";

    private Game mGame;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mGame = (Game)getIntent().getSerializableExtra(EXTRA_GAME);
        loadParallax(mGame);
        loadCover(mGame);
        populateData(mGame);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
}
