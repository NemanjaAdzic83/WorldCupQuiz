package com.nemanja.worldcupquiz.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.nemanja.worldcupquiz.data.local.entity.Tournament;

import java.util.List;

@Dao
public interface TournamentDao {

    /** Inserts a tournament and returns its new id. Fails if the year already exists. */
    @Insert
    long insert(Tournament tournament);

    /** Updates an existing tournament (matched by id). Children are NOT touched. */
    @Update
    void update(Tournament tournament);

    /** All tournaments, oldest first (Home and Stadiums screens). */
    @Query("SELECT * FROM tournaments ORDER BY year ASC")
    LiveData<List<Tournament>> getAllTournaments();

    /** Tournaments inside one era (Category screen). */
    @Query("SELECT * FROM tournaments WHERE year BETWEEN :startYear AND :endYear ORDER BY year ASC")
    LiveData<List<Tournament>> getTournamentsByEra(int startYear, int endYear);

    /** One tournament by id (title of the "Continue quiz" card). */
    @Query("SELECT * FROM tournaments WHERE id = :id")
    LiveData<Tournament> getTournamentById(int id);

    /** Number of tournaments, live (Home screen stats). */
    @Query("SELECT COUNT(*) FROM tournaments")
    LiveData<Integer> getCount();

    /** Number of tournaments, immediately. Background thread only (seeder check). */
    @Query("SELECT COUNT(*) FROM tournaments")
    int countSync();
}
