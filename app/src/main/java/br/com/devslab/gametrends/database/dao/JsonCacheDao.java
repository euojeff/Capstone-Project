package br.com.devslab.gametrends.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import br.com.devslab.gametrends.database.entity.JsonCache;

@Dao
public interface JsonCacheDao {

    @Insert
    void insertJsonCache(JsonCache jsonCache);

    @Insert
    void insertAll(JsonCache... jsonCaches);

    @Update
    void updateAll(JsonCache... jsonCaches);

    @Query("SELECT * FROM json_cache where queryType = :queryType")
    LiveData<JsonCache> load(Integer queryType);
}
