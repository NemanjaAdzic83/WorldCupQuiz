package com.nemanja.worldcupquiz.data.local.entity;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * The user's progress through one quiz (a tournament quiz or a topic category quiz).
 * Powers the "Continue quiz" card on Home and real stats on the Results screen.
 *
 * Exactly one of tournamentId / categoryId is expected to identify the quiz:
 * tournamentId for a single World Cup quiz, categoryId for "Records & Legends".
 */
@Entity(
        tableName = "user_progress",
        foreignKeys = {
                @ForeignKey(
                        entity = Tournament.class,
                        parentColumns = "id",
                        childColumns = "tournamentId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = QuizCategory.class,
                        parentColumns = "id",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("tournamentId"),
                @Index("categoryId")
        }
)
public class UserProgress {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Nullable
    private Integer tournamentId;

    @Nullable
    private Integer categoryId;

    private int questionsAnswered;

    private int correctAnswers;

    private int totalQuestions;

    private int totalTimeSeconds;

    /** Time of last play, in milliseconds since 1 Jan 1970 (System.currentTimeMillis()). */
    private long lastPlayedAt;

    private boolean completed;

    public UserProgress(@Nullable Integer tournamentId, @Nullable Integer categoryId,
                        int questionsAnswered, int correctAnswers, int totalQuestions,
                        int totalTimeSeconds, long lastPlayedAt, boolean completed) {
        this.tournamentId = tournamentId;
        this.categoryId = categoryId;
        this.questionsAnswered = questionsAnswered;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.totalTimeSeconds = totalTimeSeconds;
        this.lastPlayedAt = lastPlayedAt;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(@Nullable Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Nullable
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@Nullable Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(int totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public long getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(long lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
