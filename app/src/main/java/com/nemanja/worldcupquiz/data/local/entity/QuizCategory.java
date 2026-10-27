package com.nemanja.worldcupquiz.data.local.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.nemanja.worldcupquiz.data.model.CategoryType;


/**
 * Topic category of questions (e.g. "Finals & Winners", "Records & Stats").
 * Used for the tabs inside one tournament and for the cross-era
 * "Records & Legends" quiz.
 */
@Entity(tableName = "quiz_categories")
public class QuizCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String displayName;

    /** Stored as text ("RECORDS") - Room converts enums automatically. */
    @NonNull
    private CategoryType type;

    public QuizCategory(@NonNull String displayName, @NonNull CategoryType type) {
        this.displayName = displayName;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@NonNull String displayName) {
        this.displayName = displayName;
    }

    @NonNull
    public CategoryType getType() {
        return type;
    }

    public void setType(@NonNull CategoryType type) {
        this.type = type;
    }
}
