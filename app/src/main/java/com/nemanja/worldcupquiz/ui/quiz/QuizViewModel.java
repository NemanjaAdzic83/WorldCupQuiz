package com.nemanja.worldcupquiz.ui.quiz;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.local.entity.UserProgress;
import com.nemanja.worldcupquiz.data.repository.QuizRepository;

import java.util.List;

/**
 * Quiz state: questions, current question, score, elapsed time and saved progress.
 * Lives in the ViewModel so a screen rotation does not restart the quiz.
 */
public class QuizViewModel extends AndroidViewModel {

    /** Marker for "no tournament" / "no category" in navigation arguments. */
    public static final int NO_ID = -1;

    /** Which questions to load. */
    private static class QuizParams {
        final int tournamentId;
        final int categoryId;

        QuizParams(int tournamentId, int categoryId) {
            this.tournamentId = tournamentId;
            this.categoryId = categoryId;
        }
    }

    private final QuizRepository repository;
    private final MutableLiveData<QuizParams> paramsTrigger = new MutableLiveData<>();
    private final LiveData<List<Question>> questions;

    private boolean started = false;
    private int tournamentId = NO_ID;
    private int categoryId = NO_ID;

    private int currentIndex = 0;
    private int correctCount = 0;
    private int elapsedSeconds = 0;

    /** Saved progress of this quiz run (null until the first answer of a new run). */
    @Nullable
    private UserProgress progress;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = QuizRepository.getInstance(application);

        questions = Transformations.switchMap(paramsTrigger, p -> {
            if (p.tournamentId == NO_ID && p.categoryId == NO_ID) {
                return repository.getAllCrossEraQuestions();          // Records & Legends
            } else if (p.tournamentId == NO_ID) {
                return repository.getCrossEraQuestions(p.categoryId);
            } else if (p.categoryId == NO_ID) {
                return repository.getQuestionsForTournament(p.tournamentId);  // "All" tab
            } else {
                return repository.getQuestionsForTournamentAndCategory(p.tournamentId, p.categoryId);
            }
        });
    }

    /**
     * Starts the quiz once (ignored after rotation). If an unfinished run of the
     * same quiz exists, it is resumed; then the questions are loaded.
     */
    public void start(int tournamentId, int categoryId) {
        if (started) return;
        started = true;
        this.tournamentId = tournamentId;
        this.categoryId = categoryId;

        repository.findUnfinishedProgress(toNullable(tournamentId), toNullable(categoryId), existing -> {
            if (existing != null) {
                progress = existing;
                currentIndex = existing.getQuestionsAnswered();
                correctCount = existing.getCorrectAnswers();
                elapsedSeconds = existing.getTotalTimeSeconds();
            }
            paramsTrigger.setValue(new QuizParams(tournamentId, categoryId));
        });
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    /** Called every second by the Fragment's timer. */
    public void tick() {
        elapsedSeconds++;
    }

    public boolean isFinished(int totalQuestions) {
        return currentIndex >= totalQuestions;
    }

    /** Records the answer, moves to the next question and saves progress. */
    public void advance(boolean wasCorrect, int totalQuestions) {
        if (wasCorrect) {
            correctCount++;
        }
        currentIndex++;
        saveProgress(totalQuestions);
    }

    private void saveProgress(int totalQuestions) {
        if (progress == null) {
            progress = new UserProgress(toNullable(tournamentId), toNullable(categoryId),
                    0, 0, totalQuestions, 0, 0L, false);
        }
        progress.setQuestionsAnswered(currentIndex);
        progress.setCorrectAnswers(correctCount);
        progress.setTotalQuestions(totalQuestions);
        progress.setTotalTimeSeconds(elapsedSeconds);
        progress.setLastPlayedAt(System.currentTimeMillis());
        progress.setCompleted(currentIndex >= totalQuestions);
        repository.saveProgress(progress);
    }

    /** -1 (NO_ID) becomes null for the database, anything else stays as it is. */
    @Nullable
    private static Integer toNullable(int id) {
        return id == NO_ID ? null : id;
    }
}
