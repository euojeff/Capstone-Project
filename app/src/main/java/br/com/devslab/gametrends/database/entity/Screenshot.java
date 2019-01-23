package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "screenshot",
        foreignKeys = @ForeignKey(entity = Game.class,
                parentColumns = "id",
                childColumns = "game_id",
                onDelete = ForeignKey.CASCADE))
public class Screenshot {

    @PrimaryKey
    private Integer id;

    @ColumnInfo(name = "game_id", index = true)
    private Integer gameId;

    private String apiImageId;

    public Screenshot(){};

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public String getApiImageId() {
        return apiImageId;
    }

    public void setApiImageId(String apiImageId) {
        this.apiImageId = apiImageId;
    }
}
