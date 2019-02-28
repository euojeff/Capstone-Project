package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "game")
public class Game implements Serializable{


    @PrimaryKey
    private Integer id;
    private String name;
    private String summary;
    private String storyline;
    private Integer rating;
    private String coverId;
    private Long releaseDate;
    @Ignore
    private List<Screenshot> screenshotsList;
    @Ignore
    private List<Artwork> artworksList;
    @Ignore
    private List<PulseArticle> pulseArticleList;

    public Game(){};

    public Long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<Screenshot> getScreenshotsList() {
        return screenshotsList;
    }

    public void setScreenshotsList(List<Screenshot> screenshotsList) {
        this.screenshotsList = screenshotsList;
    }

    public List<Artwork> getArtworksList() {
        return artworksList;
    }

    public void setArtworksList(List<Artwork> artworksList) {
        this.artworksList = artworksList;
    }

    public void setPulseArticleList(List<PulseArticle> pulseArticleList) {
        this.pulseArticleList = pulseArticleList;
    }

    public List<PulseArticle> getPulseArticleList() {
        return pulseArticleList;
    }
}
