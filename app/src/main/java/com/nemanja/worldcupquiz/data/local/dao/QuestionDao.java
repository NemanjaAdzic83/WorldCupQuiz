package com.nemanja.worldcupquiz.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.nemanja.worldcupquiz.data.local.entity.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    /** Inserts many questions at once (seeder). */
    @Insert
    void insertAll(List<Question> questions);

    /** All questions of one World Cup ("All questions" tab). */
    @Query("SELECT * FROM questions WHERE tournamentId = :tournamentId")
    LiveData<List<Question>> getQuestionsForTournament(int tournamentId);

    /** Questions of one World Cup filtered by category (Finals / Players / Records tabs). */
    @Query("SELECT * FROM questions WHERE tournamentId = :tournamentId AND categoryId = :categoryId")
    LiveData<List<Question>> getQuestionsForTournamentAndCategory(int tournamentId, int categoryId);

    /** Cross-era questions (no tournament) of one category - "Records & Legends" quiz. */
    @Query("SELECT * FROM questions WHERE tournamentId IS NULL AND categoryId = :categoryId")
    LiveData<List<Question>> getCrossEraQuestions(int categoryId);

    /** Total number of questions, live (Home screen stats). */
    @Query("SELECT COUNT(*) FROM questions")
    LiveData<Integer> getCount();
}
