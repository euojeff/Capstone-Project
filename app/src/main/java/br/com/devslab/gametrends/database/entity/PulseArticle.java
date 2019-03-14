package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "pulse_article",
        foreignKeys = @ForeignKey(entity = Game.class,
                parentColumns = "id",
                childColumns = "game_id",
                onDelete = ForeignKey.CASCADE))
public class PulseArticle implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "game_id", index = true)
    private Integer gameId;

    @ColumnInfo(name = "uid", index = true)
    private String uniqueId;

    private String imgUrl;

    private String title;

    private String author;

    private String summary;

    private String articleUrl;

    private Long publishedDate;

    public PulseArticle(){};

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

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setPublishedDate(Long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }


}
