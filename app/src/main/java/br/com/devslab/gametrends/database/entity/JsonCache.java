package br.com.devslab.gametrends.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.devslab.gametrends.ui.GamesFragment;

@Entity(tableName = "json_cache")
public class JsonCache {

    @PrimaryKey
    private Integer queryType;
    private String content;

    @Ignore
    public JsonCache(){};

    public JsonCache(Integer queryType, String content){
        this.queryType = queryType;
        this.content = content;
    };

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

    public static JsonCache[] populateData(){

        List<JsonCache> list = new ArrayList<>();
        list.add(new JsonCache(GamesFragment.QueryTypeEnum.POPULAR.getId(), null));
        list.add(new JsonCache(GamesFragment.QueryTypeEnum.COMING.getId(), null));

        return list.toArray(new JsonCache[list.size()]);
    }
}
