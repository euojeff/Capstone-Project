package br.com.devslab.gametrends.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import br.com.devslab.gametrends.R;


public class GamesListActivity extends AppCompatActivity implements GamesFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);


        TabAdapter adapter = new TabAdapter( getSupportFragmentManager());
        adapter.adicionar( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.POPULAR) , "Popular");
        adapter.adicionar( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.COMMING), "Comming");
        adapter.adicionar( GamesFragment.newInstance(GamesFragment.QueryTypeEnum.POPULAR), "Favorite");

        ViewPager viewPager = findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestOpenGameDetail(String json) {
        //Todo imlementar chamada para detalhe
        Log.i("TODO", "Implementar Detalhe");
    }
}
