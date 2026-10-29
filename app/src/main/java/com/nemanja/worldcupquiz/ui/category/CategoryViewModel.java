package com.nemanja.worldcupquiz.ui.category;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.model.CategoryType;
import com.nemanja.worldcupquiz.data.model.Era;
import com.nemanja.worldcupquiz.data.repository.QuizRepository;

import java.util.List;

/**
 * Category screen data: tournaments of the selected era + the selected topic tab.
 */
public class CategoryViewModel extends AndroidViewModel {

    private final QuizRepository repository;
    private final MutableLiveData<Era> selectedEra = new MutableLiveData<>();
    private final LiveData<List<Tournament>> tournaments;

    /** Selected topic tab. null = "All" (no category filter). Survives rotation. */
    @Nullable
    private CategoryType selectedType = null;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = QuizRepository.getInstance(application);

        tournaments = Transformations.switchMap(selectedEra, era ->
                repository.getTournamentsByEra(era.getStartYear(), era.getEndYear()));
    }

    public void setEra(@NonNull Era era) {
        if (era != selectedEra.getValue()) {
            selectedEra.setValue(era);
        }
    }

    public LiveData<List<Tournament>> getTournaments() {
        return tournaments;
    }

    @Nullable
    public CategoryType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(@Nullable CategoryType type) {
        selectedType = type;
    }

    /** Looks up the database id of a topic category (result on the main thread). */
    public void findCategoryId(CategoryType type, QuizRepository.CategoryIdCallback callback) {
        repository.getCategoryIdByType(type, callback);
    }
}