package br.com.devslab.gametrends.database.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.GameRelation;

public class LoadGameViewModel extends AndroidViewModel{

    private LiveData<GameRelation> game;

    public LoadGameViewModel(@NonNull Application application, Integer id) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        game = database.gameDao().loadGame(id);
    }

    public LiveData<GameRelation> getGame() {
        return game;
    }
}
