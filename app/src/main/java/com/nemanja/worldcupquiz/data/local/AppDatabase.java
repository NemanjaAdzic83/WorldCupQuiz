package com.nemanja.worldcupquiz.data.local;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nemanja.worldcupquiz.data.local.dao.QuestionDao;
import com.nemanja.worldcupquiz.data.local.dao.QuizCategoryDao;
import com.nemanja.worldcupquiz.data.local.dao.StadiumDao;
import com.nemanja.worldcupquiz.data.local.dao.TournamentDao;
import com.nemanja.worldcupquiz.data.local.dao.UserProgressDao;
import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.local.entity.QuizCategory;
import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.local.entity.UserProgress;
import com.nemanja.worldcupquiz.util.AppExecutors;

/**
 * The app's Room database. Singleton - one instance for the whole app.
 * Room generates the implementation (AppDatabase_Impl) at build time.
 */
@Database(
        entities = {
                Tournament.class,
                QuizCategory.class,
                Question.class,
                Stadium.class,
                UserProgress.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "worldcup_quiz.db";

    private static volatile AppDatabase instance;

    // Room implements these and returns ready-to-use DAOs.
    public abstract TournamentDao tournamentDao();
    public abstract QuizCategoryDao quizCategoryDao();
    public abstract QuestionDao questionDao();
    public abstract StadiumDao stadiumDao();
    public abstract UserProgressDao userProgressDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME)
                            .addCallback(SEED_CALLBACK)
                            .build();
                }
            }
        }
        return instance;
    }

    /**
     * Called by Room only ONCE - when the database file is created (fresh install).
     * Seeding writes to the database, so it must run on a background thread.
     */
    private static final RoomDatabase.Callback SEED_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (instance != null) {
                    DatabaseSeeder.seed(instance);
                }
            });
        }
    };
}
