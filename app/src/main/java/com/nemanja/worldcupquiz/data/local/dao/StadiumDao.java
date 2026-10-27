package com.nemanja.worldcupquiz.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.nemanja.worldcupquiz.data.local.entity.Stadium;

import java.util.List;

@Dao
public interface StadiumDao {

    @Insert
    void insertAll(List<Stadium> stadiums);

    /** Stadiums of one World Cup (Stadium detail screen). */
    @Query("SELECT * FROM stadiums WHERE tournamentId = :tournamentId")
    LiveData<List<Stadium>> getStadiumsForTournament(int tournamentId);
}
