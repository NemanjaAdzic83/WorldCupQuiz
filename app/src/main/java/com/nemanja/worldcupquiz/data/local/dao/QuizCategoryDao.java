package com.nemanja.worldcupquiz.data.local.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nemanja.worldcupquiz.data.local.entity.QuizCategory;
import com.nemanja.worldcupquiz.data.model.CategoryType;


@Dao
public interface QuizCategoryDao {

    /** Inserts a category and returns its new id (needed by the seeder). */
    @Insert
    long insert(QuizCategory category);

    /**
     * Finds the category of the given type, e.g. RECORDS -> its row.
     * Returns null if not found. Background thread only.
     */
    @Query("SELECT * FROM quiz_categories WHERE type = :type LIMIT 1")
    QuizCategory getByTypeSync(CategoryType type);
}
