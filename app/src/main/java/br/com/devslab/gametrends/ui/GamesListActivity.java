package br.com.devslab.gametrends.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.remote.SyncDataJobService;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {

    private FirebaseAnalytics mFirebaseAnalytics;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        TabAdapter adapter = new TabAdapter( getSupportFragmentManager());
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.POPULAR) , getResources().getString(R.string.tab_popular));
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.COMING), getResources().getString(R.string.tab_coming));
        adapter.addTab( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.FAVORITE), getResources().getString(R.string.tab_favorite));

        ViewPager viewPager = findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        configureSyncDataJob();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void configureSyncDataJob(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job = dispatcher.newJobBuilder()
                .setService(SyncDataJobService.class)
                .setTag("SyncDataJob")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(2, 10))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK, Constraint.DEVICE_CHARGING)
                .build();

        dispatcher.mustSchedule(job);
    }

    @Override
    public void onRequestOpenGameDetail(Game game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME, game);

        startActivity(intent);
    }
}
