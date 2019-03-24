package br.com.devslab.gametrends.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.devslab.gametrends.database.entity.Artwork;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.GameRelation;
import br.com.devslab.gametrends.database.entity.PulseArticle;
import br.com.devslab.gametrends.database.entity.Screenshot;

@Dao
public abstract class GameDao {

    @Transaction @Query("SELECT * FROM game ORDER BY releaseDate desc")
    public abstract LiveData<List<GameRelation>> loadFavorited();

    @Query("SELECT * FROM pulse_article order by publishedDate desc limit 20")
    public abstract List<PulseArticle> latestArticles();

    @Transaction @Query("SELECT * FROM game where id = :id limit 1")
    public abstract LiveData<GameRelation> loadGame(Integer id);

    @Query("SELECT * FROM pulse_article where uid = :uid limit 1")
    public abstract PulseArticle loadPulse(String uid);

    @Transaction
    public void insertGameWithRelations(Game game){

        insertGames(Arrays.asList(game));

        List<Artwork> artworkList = new ArrayList<>();
        List<Screenshot> screenshotList = new ArrayList<>();

        if(game.getArtworksList() != null){
            for(Artwork artwork: game.getArtworksList()){
                artwork.setGameId(game.getId());
                artworkList.add(artwork);
            }
        }

        if(game.getScreenshotsList() != null){
            for(Screenshot screenshot: game.getScreenshotsList()){
                screenshot.setGameId(game.getId());
                screenshotList.add(screenshot);
            }
        }

        insertArtworks(artworkList);
        insertScreenshots(screenshotList);
        insertPulse(game.getPulseArticleList(), game);
    };

    @Transaction
    public void insertPulse(List<PulseArticle> pulses, Game game){

        if(pulses != null){
            for(PulseArticle pulse: pulses){
                boolean alreadyStored = loadPulse(pulse.getUniqueId()) != null;
                if(!alreadyStored){
                    pulse.setGameId(game.getId());
                    insertPulse(pulse);
                }
            }
        }
    };

    @Insert
    public abstract void insertPulse(PulseArticle pulse);

    @Insert
    public abstract void insertGames(List<Game> games);

    @Insert
    public abstract void insertArtworks(List<Artwork> artworks);

    @Insert
    public abstract void insertScreenshots(List<Screenshot> screenshots);

    @Delete
    public abstract void deleteAll(Game... games);
}
