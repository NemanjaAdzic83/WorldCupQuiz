package com.nemanja.worldcupquiz.data.repository;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nemanja.worldcupquiz.data.local.AppDatabase;
import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;

import java.util.List;

/**
 * Kept separate from QuizRepository because the Stadiums section has nothing
 * to do with quiz logic - it is purely informational content.
 */
public class StadiumRepository {

    private static volatile StadiumRepository instance;

    private final AppDatabase db;

    private StadiumRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public static StadiumRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (StadiumRepository.class) {
                if (instance == null) {
                    instance = new StadiumRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public LiveData<List<Stadium>> getStadiumsForTournament(int tournamentId) {
        return db.stadiumDao().getStadiumsForTournament(tournamentId);
    }

    /** Tournaments are shown as "pills" on the Stadiums screen - reuse the Tournament table. */
    public LiveData<List<Tournament>> getAllTournaments() {
        return db.tournamentDao().getAllTournaments();
    }
}
