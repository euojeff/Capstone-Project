package br.com.devslab.gametrends.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.devslab.gametrends.database.entity.Game;

@Dao
public interface GameDao {

    @Query("SELECT * FROM game ORDER BY releaseDate desc")
    LiveData<List<Game>> loadFavorited();

    @Insert
    void insertGame(Game game);

    @Insert
    void insertAll(Game... gameEntities);
}
