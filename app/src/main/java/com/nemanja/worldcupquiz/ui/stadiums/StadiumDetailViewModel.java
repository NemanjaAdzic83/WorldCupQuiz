package com.nemanja.worldcupquiz.ui.stadiums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.data.repository.StadiumRepository;

import java.util.List;

public class StadiumDetailViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> tournamentIdTrigger = new MutableLiveData<>();
    private final LiveData<List<Stadium>> stadiums;

    public StadiumDetailViewModel(@NonNull Application application) {
        super(application);
        StadiumRepository repository = StadiumRepository.getInstance(application);
        stadiums = Transformations.switchMap(tournamentIdTrigger,repository::getStadiumsForTournament);
    }

    public void loadStadiums(int tournamentId){
        Integer current = tournamentIdTrigger.getValue();
        if (current != null && current == tournamentId){
            return;
        }
        tournamentIdTrigger.setValue(tournamentId);
    }

    public LiveData<List<Stadium>> getStadiums(){
        return stadiums;
    }
}
