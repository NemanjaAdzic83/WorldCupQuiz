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

    public abstract TournamentDao tournamentDao();
    public abstract QuizCategoryDao quizCategoryDao();
    public abstract QuestionDao questionDao();
    public abstract StadiumDao stadiumDao();
    public abstract UserProgressDao userProgressDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    Context appContext = context.getApplicationContext();
                    instance = Room.databaseBuilder(appContext, AppDatabase.class, DB_NAME)
                            .addCallback(createSeedCallback(appContext))
                            .build();
                }
            }
        }
        return instance;
    }

    /**
     * Seeds the database on a background thread every time it is opened.
     * DatabaseSeeder itself skips the work if data is already there, so this is cheap.
     * Using onOpen (not onCreate) means a failed first seed is retried on the next launch.
     */
    private static RoomDatabase.Callback createSeedCallback(Context appContext) {
        return new RoomDatabase.Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                AppExecutors.getInstance().diskIO().execute(() -> {
                    if (instance != null) {
                        DatabaseSeeder.seed(instance, appContext);
                    }
                });
            }
        };
    }
}