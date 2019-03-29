package br.com.devslab.gametrends.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.remote.SyncDataJobService;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd;
    private View mTransitionItemView;
    private Game mGameToOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        configInterstitialAd();

        ViewPager viewPager = findViewById(R.id.abas_view_pager);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.POPULAR), getResources().getString(R.string.tab_popular));
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.COMING), getResources().getString(R.string.tab_coming));
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.FAVORITE), getResources().getString(R.string.tab_favorite));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        configureSyncDataJob();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /*
    *
    * This is a solution to grab the tag created by FragmentPagerAdapter.
    * This basic class is very poor in implementation and forces the developer to copy the
     * makeFragmentName method because it is private.
    * */
    private static String getAdapterFragmentTag(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private void configureSyncDataJob(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job = dispatcher.newJobBuilder()
                .setService(SyncDataJobService.class)
                .setTag("SyncDataJob")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(5, 60))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK, Constraint.DEVICE_CHARGING)
                .build();

        dispatcher.mustSchedule(job);
    }

    private void configInterstitialAd(){
        MobileAds.initialize(this, getResources().getString(R.string.ADMOB_ID));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADMOB_INTERSTITIAL_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                openGame(mGameToOpen, mTransitionItemView);
            }
        });
    }

    @Override
    public void onRequestOpenGameDetail(Game game, View transitionItemView) {

        mGameToOpen = game;
        mTransitionItemView = transitionItemView;

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }else{

            openGame(mGameToOpen, mTransitionItemView);

        }
    }

    private void openGame(Game game, View transitionItemView){

        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME, game);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Bundle bundle = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, transitionItemView, transitionItemView.getTransitionName())
                    .toBundle();

            startActivity(intent, bundle);

        } else {
            startActivity(intent);
        }
    }
}
