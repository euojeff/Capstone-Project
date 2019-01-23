package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "json_cache")
public class JsonCache {

    @PrimaryKey
    private Integer queryType;
    private String content;

    public JsonCache(){};

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
