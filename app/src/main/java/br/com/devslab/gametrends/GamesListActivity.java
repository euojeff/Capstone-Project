package br.com.devslab.gametrends;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);


        TabAdapter adapter = new TabAdapter( getSupportFragmentManager());
        adapter.adicionar( new GamesFragment() , "Populares");
        adapter.adicionar( new GamesFragment(), "Lançamentos");
        adapter.adicionar( new GamesFragment(), "Favoritos");

        ViewPager viewPager = findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
