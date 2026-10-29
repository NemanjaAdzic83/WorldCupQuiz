package com.nemanja.worldcupquiz.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nemanja.worldcupquiz.data.local.entity.UserProgress;


@Dao
public interface UserProgressDao {

    /** Starts a new progress record and returns its id (so later saves can update it). */
    @Insert
    long insert(UserProgress progress);

    /** Saves changes to an existing progress record (matched by id). */
    @Update
    void update(UserProgress progress);

    /** The most recently played unfinished quiz - for the "Continue quiz" card. */
    @Query("SELECT * FROM user_progress WHERE completed = 0 ORDER BY lastPlayedAt DESC LIMIT 1")
    LiveData<UserProgress> getLastUnfinished();

    /**
     * The unfinished progress of ONE specific quiz (same tournament + category).
     * "IS" instead of "=" so that NULL matches NULL. Background thread only.
     */
    @Query("SELECT * FROM user_progress WHERE completed = 0 " +
            "AND tournamentId IS :tournamentId AND categoryId IS :categoryId " +
            "ORDER BY lastPlayedAt DESC LIMIT 1")
    UserProgress findUnfinishedSync(Integer tournamentId, Integer categoryId);
}
