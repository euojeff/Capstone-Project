package br.com.devslab.gametrends.database.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.GameRelation;

public class FavoritedGamesViewModel extends AndroidViewModel{

    private LiveData<List<GameRelation>> favoritedGames;

    public FavoritedGamesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoritedGames = database.gameDao().loadFavorited();
    }

    public LiveData<List<GameRelation>> getFavoritedGames() {
        return favoritedGames;
    }
}
