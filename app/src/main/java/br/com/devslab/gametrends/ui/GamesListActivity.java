package br.com.devslab.gametrends.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.Game;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {


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
    }

    @Override
    public void onRequestOpenGameDetail(Game game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME, game);

        startActivity(intent);
    }
}
