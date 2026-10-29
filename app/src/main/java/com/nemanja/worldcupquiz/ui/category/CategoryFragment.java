package com.nemanja.worldcupquiz.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.model.CategoryType;
import com.nemanja.worldcupquiz.data.model.Era;
import com.nemanja.worldcupquiz.databinding.FragmentCategoryBinding;
import com.nemanja.worldcupquiz.ui.quiz.QuizViewModel;

/**
 * Tournaments of one era + topic tabs (All / Finals / Players / Records).
 */
public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private CategoryViewModel viewModel;
    private TopicAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Read the navigation argument (Safe Args)
        Era era = com.nemanja.worldcupquiz.ui.category.CategoryFragmentArgs.fromBundle(requireArguments()).getEra();
        binding.textTitle.setText(getString(R.string.category_title,
                getString(era.getDisplayNameRes()), era.getStartYear(), era.getEndYear()));
        viewModel.setEra(era);

        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        setupTabs();
        updateTabStyles();   // restore the selected tab after rotation
        setupList();

        viewModel.getTournaments().observe(getViewLifecycleOwner(), tournaments -> {
            if (tournaments != null) {
                topicAdapter.submitList(tournaments);
            }
        });
    }

    // ---------- List ----------

    private void setupList() {
        topicAdapter = new TopicAdapter(this::onTopicClicked);
        binding.recyclerTopics.setAdapter(topicAdapter);
    }

    private void onTopicClicked(Tournament tournament) {
        String quizTitle = getString(R.string.continue_title_tournament,
                tournament.getYear(), tournament.getHostCountry());
        CategoryType type = viewModel.getSelectedType();

        if (type == null) {   // "All" tab - no category filter
            navigateToQuiz(tournament.getId(), QuizViewModel.NO_ID, quizTitle);
            return;
        }

        viewModel.findCategoryId(type, categoryId -> {
            if (binding == null) return;   // user left the screen meanwhile
            if (categoryId == null) {
                Toast.makeText(requireContext(), R.string.category_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            navigateToQuiz(tournament.getId(), categoryId, quizTitle);
        });
    }

    private void navigateToQuiz(int tournamentId, int categoryId, String title) {
        NavHostFragment.findNavController(this).navigate(
                CategoryFragmentDirections.actionCategoryToQuiz(tournamentId, categoryId, title));
    }

    // ---------- Tabs ----------

    private void setupTabs() {
        setTabListener(binding.tabAll, null);
        setTabListener(binding.tabFinals, CategoryType.FINALS);
        setTabListener(binding.tabPlayers, CategoryType.PLAYERS);
        setTabListener(binding.tabRecords, CategoryType.RECORDS);
    }

    private void setTabListener(TextView tab, @Nullable CategoryType type) {
        tab.setOnClickListener(v -> {
            viewModel.setSelectedType(type);
            updateTabStyles();
        });
    }

    private void updateTabStyles() {
        CategoryType selected = viewModel.getSelectedType();
        setTabActive(binding.tabAll, selected == null);
        setTabActive(binding.tabFinals, selected == CategoryType.FINALS);
        setTabActive(binding.tabPlayers, selected == CategoryType.PLAYERS);
        setTabActive(binding.tabRecords, selected == CategoryType.RECORDS);
    }

    private void setTabActive(TextView tab, boolean active) {
        tab.setBackgroundResource(active ? R.drawable.bg_pill_active : R.drawable.bg_pill_inactive);
        tab.setTextColor(ContextCompat.getColor(requireContext(),
                active ? R.color.white : R.color.pitch_green_dark));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
