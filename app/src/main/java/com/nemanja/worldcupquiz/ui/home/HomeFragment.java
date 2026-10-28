package com.nemanja.worldcupquiz.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.model.Era;
import com.nemanja.worldcupquiz.databinding.FragmentHomeBinding;


/**
 * Start screen: hero header with stats, grid of eras and special sections.
 * Step 10a: layout only. Step 10b adds the ViewModel, era list and navigation.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHeroStats();
    }

    private void setupHeroStats() {
        binding.statTournaments.statLabel.setText(R.string.stat_tournaments);
        binding.statEras.statLabel.setText(R.string.stat_eras);
        binding.statQuestions.statLabel.setText(R.string.stat_questions);

        // The number of eras comes straight from the enum - always correct.
        binding.statEras.statNumber.setText(String.valueOf(Era.values().length));

        // Placeholders until step 10b reads the real counts from the database.
        binding.statTournaments.statNumber.setText("–");
        binding.statQuestions.statNumber.setText("–");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
