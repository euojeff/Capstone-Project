package br.com.devslab.gametrends.database.config;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executors;

import br.com.devslab.gametrends.database.dao.GameDao;
import br.com.devslab.gametrends.database.dao.JsonCacheDao;
import br.com.devslab.gametrends.database.entity.Artwork;
import br.com.devslab.gametrends.database.entity.Game;
import br.com.devslab.gametrends.database.entity.JsonCache;
import br.com.devslab.gametrends.database.entity.Screenshot;


/**
 *
 * Based on https://github.com/udacity/ud851-Exercises/tree/student/Lesson09b-ToDo-List-AAC/T09b.10-Solution-AddViewModelToAddTaskActivity
 *
 */
@Database(entities = {Game.class, JsonCache.class, Artwork.class, Screenshot.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "gametrends";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");

                RoomDatabase.Builder<AppDatabase> builder  = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME);

                builder.addCallback(prePopulateDB(context.getApplicationContext()));

                sInstance = builder.build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static Callback prePopulateDB(final Context context){
       return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getInstance(context).jsonCacheDao().insertAll(JsonCache.populateData());
                    }
                });
            }
        };
    }

    public abstract GameDao gameDao();

    public abstract JsonCacheDao jsonCacheDao();

}
