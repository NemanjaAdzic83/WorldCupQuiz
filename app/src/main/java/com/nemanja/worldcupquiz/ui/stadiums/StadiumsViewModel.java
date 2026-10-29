package com.nemanja.worldcupquiz.ui.stadiums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.repository.StadiumRepository;

import java.util.List;

public class StadiumsViewModel extends AndroidViewModel {

    private final LiveData<List<Tournament>> tournaments;

    public StadiumsViewModel(@NonNull Application application) {
        super(application);
        tournaments = StadiumRepository.getInstance(application).getAllTournaments();

    }

    public LiveData<List<Tournament>> getTournaments(){
        return tournaments;
    }
}
