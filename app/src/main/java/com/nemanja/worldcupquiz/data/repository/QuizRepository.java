package com.nemanja.worldcupquiz.data.repository;


import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.nemanja.worldcupquiz.data.local.AppDatabase;
import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.local.entity.QuizCategory;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.local.entity.UserProgress;
import com.nemanja.worldcupquiz.data.model.CategoryType;
import com.nemanja.worldcupquiz.util.AppExecutors;

import java.util.List;

/**
 * Single entry point to quiz data (tournaments, questions, categories, progress).
 * ViewModels never touch DAOs directly - everything goes through this class.
 */
public class QuizRepository {

    /** Callback for results computed on a background thread. Delivered on the main thread. */
    public interface CategoryIdCallback {
        void onResult(@Nullable Integer categoryId);
    }

    private static volatile QuizRepository instance;

    private final AppDatabase db;
    private final AppExecutors executors;

    private QuizRepository(Context context) {
        db = AppDatabase.getInstance(context);
        executors = AppExecutors.getInstance();
    }

    public static QuizRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (QuizRepository.class) {
                if (instance == null) {
                    instance = new QuizRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // ---------- Tournaments ----------

    public LiveData<List<Tournament>> getAllTournaments() {
        return db.tournamentDao().getAllTournaments();
    }

    public LiveData<List<Tournament>> getTournamentsByEra(int startYear, int endYear) {
        return db.tournamentDao().getTournamentsByEra(startYear, endYear);
    }

    public LiveData<Tournament> getTournamentById(int id) {
        return db.tournamentDao().getTournamentById(id);
    }

    public LiveData<Integer> getTournamentCount() {
        return db.tournamentDao().getCount();
    }

    // ---------- Questions ----------

    public LiveData<List<Question>> getQuestionsForTournament(int tournamentId) {
        return db.questionDao().getQuestionsForTournament(tournamentId);
    }

    public LiveData<List<Question>> getQuestionsForTournamentAndCategory(int tournamentId, int categoryId) {
        return db.questionDao().getQuestionsForTournamentAndCategory(tournamentId, categoryId);
    }

    public LiveData<List<Question>> getCrossEraQuestions(int categoryId) {
        return db.questionDao().getCrossEraQuestions(categoryId);
    }

    public LiveData<Integer> getQuestionCount() {
        return db.questionDao().getCount();
    }

    // ---------- Categories ----------

    /**
     * Finds the id of the category with the given type (e.g. RECORDS).
     * The lookup runs on a background thread; the result (or null if the
     * category does not exist yet) is delivered on the main thread.
     */
    public void getCategoryIdByType(CategoryType type, CategoryIdCallback callback) {
        executors.diskIO().execute(() -> {
            QuizCategory category = db.quizCategoryDao().getByTypeSync(type);
            Integer id = (category != null) ? category.getId() : null;
            executors.mainThread().execute(() -> callback.onResult(id));
        });
    }

    // ---------- Progress ----------

    public LiveData<UserProgress> getLastUnfinishedProgress() {
        return db.userProgressDao().getLastUnfinished();
    }

    /**
     * Saves progress on a background thread.
     * New record (id == 0) -> insert, and the new id is written back into the object,
     * so the next save of the same object becomes an update.
     */
    public void saveProgress(UserProgress progress) {
        executors.diskIO().execute(() -> {
            if (progress.getId() == 0) {
                long newId = db.userProgressDao().insert(progress);
                progress.setId((int) newId);
            } else {
                db.userProgressDao().update(progress);
            }
        });
    }
}