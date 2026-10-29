package com.nemanja.worldcupquiz.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.nemanja.worldcupquiz.data.local.entity.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insertAll(List<Question> questions);

    // ORDER BY id: always the same order, so a quiz can be resumed at question N.

    /** All questions of one World Cup ("All" tab). */
    @Query("SELECT * FROM questions WHERE tournamentId = :tournamentId ORDER BY id")
    LiveData<List<Question>> getQuestionsForTournament(int tournamentId);

    /** Questions of one World Cup in one category (Finals / Players / Records tabs). */
    @Query("SELECT * FROM questions WHERE tournamentId = :tournamentId AND categoryId = :categoryId ORDER BY id")
    LiveData<List<Question>> getQuestionsForTournamentAndCategory(int tournamentId, int categoryId);

    /** Cross-era questions of one category. */
    @Query("SELECT * FROM questions WHERE tournamentId IS NULL AND categoryId = :categoryId ORDER BY id")
    LiveData<List<Question>> getCrossEraQuestions(int categoryId);

    /** ALL cross-era questions - the "Records & Legends" quiz. */
    @Query("SELECT * FROM questions WHERE tournamentId IS NULL ORDER BY id")
    LiveData<List<Question>> getAllCrossEraQuestions();

    @Query("SELECT COUNT(*) FROM questions")
    LiveData<Integer> getCount();
}
