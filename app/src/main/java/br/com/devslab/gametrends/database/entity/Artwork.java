package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "atwork",
        foreignKeys = @ForeignKey(entity = Game.class,
        parentColumns = "id",
        childColumns = "game_id",
        onDelete = ForeignKey.CASCADE))
public class Artwork implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String apiImageId;

    @ColumnInfo(name = "game_id", index = true)
    private Integer gameId;

    public Artwork(){}

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

    public void setApiImageId(String apiImageId) {
        this.apiImageId = apiImageId;
    }

    public String getApiImageId() {
        return apiImageId;
    }
}
