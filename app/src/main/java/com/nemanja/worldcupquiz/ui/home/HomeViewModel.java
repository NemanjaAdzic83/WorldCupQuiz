package com.nemanja.worldcupquiz.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.local.entity.UserProgress;
import com.nemanja.worldcupquiz.data.repository.QuizRepository;

import java.util.List;

/**
 * Holds the Home screen data. Survives screen rotation.
 */
public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Tournament>> allTournaments;
    private final LiveData<Integer> tournamentCount;
    private final LiveData<Integer> questionCount;
    private final LiveData<UserProgress> lastUnfinished;
    private final LiveData<Tournament> continueTournament;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        QuizRepository repository = QuizRepository.getInstance(application);

        allTournaments = repository.getAllTournaments();
        tournamentCount = repository.getTournamentCount();
        questionCount = repository.getQuestionCount();
        lastUnfinished = repository.getLastUnfinishedProgress();

        // Whenever the last unfinished progress changes, load ITS tournament
        // (or emit null for a cross-era quiz / no progress at all).
        continueTournament = Transformations.switchMap(lastUnfinished, progress -> {
            if (progress == null || progress.getTournamentId() == null) {
                return new MutableLiveData<Tournament>(null);
            }
            return repository.getTournamentById(progress.getTournamentId());
        });
    }

    public LiveData<List<Tournament>> getAllTournaments() {
        return allTournaments;
    }

    public LiveData<Integer> getTournamentCount() {
        return tournamentCount;
    }

    public LiveData<Integer> getQuestionCount() {
        return questionCount;
    }

    public LiveData<UserProgress> getLastUnfinishedProgress() {
        return lastUnfinished;
    }

    /** Tournament of the unfinished quiz, or null if there is none / it is cross-era. */
    public LiveData<Tournament> getContinueTournament() {
        return continueTournament;
    }
}
