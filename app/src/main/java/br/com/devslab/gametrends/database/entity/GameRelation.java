package br.com.devslab.gametrends.database.entity;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class GameRelation{

    @Embedded
    public Game game;

    @Relation(parentColumn = "id",
                entityColumn = "game_id")
    public List<Artwork> artworkList;

    @Relation(parentColumn = "id",
            entityColumn = "game_id")
    public List<Screenshot> screenshotList;
}
