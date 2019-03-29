package br.com.devslab.gametrends.database.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import br.com.devslab.gametrends.database.config.AppDatabase;
import br.com.devslab.gametrends.database.entity.GameRelation;

public class LoadGameViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private Integer mGameId;


    public LoadGameViewModelFactory(Application application, Integer gameId) {
        mApplication = application;
        mGameId = gameId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LoadGameViewModel(mApplication, mGameId);
    }
}
