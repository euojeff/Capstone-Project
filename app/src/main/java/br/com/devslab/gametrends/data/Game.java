package br.com.devslab.gametrends.data;

public class Game {

    private Integer id;
    private String name;
    private String summary;
    private String storyline;
    private Integer rating;
    private String coverId;
    private Long releaseDate;

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

}
