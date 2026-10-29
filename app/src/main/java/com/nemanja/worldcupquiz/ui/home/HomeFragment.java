package com.nemanja.worldcupquiz.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.local.entity.UserProgress;
import com.nemanja.worldcupquiz.data.model.Era;
import com.nemanja.worldcupquiz.databinding.FragmentHomeBinding;
import com.nemanja.worldcupquiz.ui.model.EraUiModel;
import com.nemanja.worldcupquiz.ui.quiz.QuizViewModel;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;


import java.util.ArrayList;
import java.util.List;

/**
 * Start screen: hero header with stats, grid of eras, "Continue quiz" card
 * and special sections (Records & Legends, Stadiums).
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private EraAdapter eraAdapter;

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
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupHeroStats();
        setupEraGrid();
        setupSpecialCards();
        observeTournaments();
        observeContinueCard();
    }

    // ---------- Hero stats ----------

    private void setupHeroStats() {
        binding.statTournaments.statLabel.setText(R.string.stat_tournaments);
        binding.statEras.statLabel.setText(R.string.stat_eras);
        binding.statQuestions.statLabel.setText(R.string.stat_questions);

        binding.statEras.statNumber.setText(String.valueOf(Era.values().length));

        viewModel.getTournamentCount().observe(getViewLifecycleOwner(), count ->
                binding.statTournaments.statNumber.setText(String.valueOf(count)));
        viewModel.getQuestionCount().observe(getViewLifecycleOwner(), count ->
                binding.statQuestions.statNumber.setText(String.valueOf(count)));
    }

    // ---------- Era grid ----------

    private void setupEraGrid() {
        eraAdapter = new EraAdapter(this::onEraClicked);
        binding.recyclerEras.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerEras.setAdapter(eraAdapter);
    }

    private void observeTournaments() {
        viewModel.getAllTournaments().observe(getViewLifecycleOwner(), tournaments -> {
            if (tournaments == null) return;

            List<EraUiModel> eraList = new ArrayList<>();
            for (Era era : Era.values()) {
                int count = 0;
                for (Tournament t : tournaments) {
                    if (era.contains(t.getYear())) {
                        count++;
                    }
                }
                eraList.add(new EraUiModel(era, count));
            }
            eraAdapter.submitList(eraList);
        });
    }

    private void onEraClicked(EraUiModel eraModel) {
        navigateToCategory(eraModel.getEra());
    }

    private void navigateToCategory(Era era) {
        NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeToCategory(era));
    }

    private void navigateToQuiz(int tournamentId, int categoryId, String title){
        NavHostFragment.findNavController(this).navigate(
                HomeFragmentDirections.actionHomeToQuiz(tournamentId,categoryId,title));
    }

    // ---------- "Continue quiz" card ----------

    private void observeContinueCard() {
        viewModel.getLastUnfinishedProgress().observe(getViewLifecycleOwner(), progress -> {
            if (progress == null) {
                binding.cardContinue.setVisibility(View.GONE);
                return;
            }
            binding.cardContinue.setVisibility(View.VISIBLE);
            binding.textContinueSub.setText(getString(R.string.continue_progress,
                    progress.getQuestionsAnswered(), progress.getTotalQuestions()));

            if (progress.getTournamentId() == null) {
                String title = getString(R.string.home_records_title);
                binding.textContinueTitle.setText(title);
                binding.cardContinue.setOnClickListener(v -> resumeQuiz(progress, title));
            }
        });

        viewModel.getContinueTournament().observe(getViewLifecycleOwner(), tournament -> {
            UserProgress progress = viewModel.getLastUnfinishedProgress().getValue();
            if (tournament != null && progress != null) {
                String title = getString(R.string.continue_title_tournament,
                        tournament.getYear(), tournament.getHostCountry());
                binding.textContinueTitle.setText(title);
                binding.cardContinue.setOnClickListener(v -> resumeQuiz(progress, title));
            }
        });
    }

    /** Opens the same quiz again - QuizViewModel finds the unfinished run and resumes it. */
    private void resumeQuiz(UserProgress progress, String title) {
        int tournamentId = progress.getTournamentId() != null ? progress.getTournamentId() : QuizViewModel.NO_ID;
        int categoryId = progress.getCategoryId() != null ? progress.getCategoryId() : QuizViewModel.NO_ID;
        navigateToQuiz(tournamentId, categoryId, title);
    }

    // ---------- Special sections ----------

    private void setupSpecialCards() {
        // Records & Legends = all cross-era questions (no tournament, no category filter)
        binding.cardRecords.setOnClickListener(v -> navigateToQuiz(
                QuizViewModel.NO_ID, QuizViewModel.NO_ID, getString(R.string.home_records_title)));

        binding.cardStadiums.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(HomeFragmentDirections.actionHomeToStadiums()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}