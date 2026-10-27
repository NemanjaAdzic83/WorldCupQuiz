package com.nemanja.worldcupquiz.data.local.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One quiz question with four answer options.
 * tournamentId is nullable: "Records & Legends" questions span several
 * tournaments (e.g. "Who is the all-time World Cup top scorer?"),
 * so they are not tied to a single Tournament.
 */
@Entity(
        tableName = "questions",
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
public class Question {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /** Null = cross-era question (Records & Legends). */
    @Nullable
    private Integer tournamentId;

    private int categoryId;

    /** 1 = easy, 2 = medium, 3 = hard */
    private int difficulty;

    @NonNull
    private String questionText;

    @NonNull
    private String optionA;

    @NonNull
    private String optionB;

    @NonNull
    private String optionC;

    @NonNull
    private String optionD;

    /** Value 0-3, matching options A-D in order. */
    private int correctOptionIndex;

    /** Short explanation shown after answering (optional). */
    @Nullable
    private String explanation;

    public Question(@Nullable Integer tournamentId, int categoryId, int difficulty,
                    @NonNull String questionText, @NonNull String optionA,
                    @NonNull String optionB, @NonNull String optionC,
                    @NonNull String optionD, int correctOptionIndex,
                    @Nullable String explanation) {
        this.tournamentId = tournamentId;
        this.categoryId = categoryId;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @NonNull
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(@NonNull String questionText) {
        this.questionText = questionText;
    }

    @NonNull
    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(@NonNull String optionA) {
        this.optionA = optionA;
    }

    @NonNull
    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(@NonNull String optionB) {
        this.optionB = optionB;
    }

    @NonNull
    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(@NonNull String optionC) {
        this.optionC = optionC;
    }

    @NonNull
    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(@NonNull String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    @Nullable
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(@Nullable String explanation) {
        this.explanation = explanation;
    }
}
