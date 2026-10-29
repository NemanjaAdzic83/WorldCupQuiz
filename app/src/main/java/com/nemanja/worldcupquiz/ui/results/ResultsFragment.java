package com.nemanja.worldcupquiz.ui.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.databinding.FragmentResultsBinding;

public class ResultsFragment extends Fragment {

    private static final int POINTS_PER_CORRECT_ANSWER = 10;
    private static final int PERFECT_SCORE_BONUS = 50;


    private FragmentResultsBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ResultsFragmentArgs args = ResultsFragmentArgs.fromBundle(requireArguments());
        int correct = args.getCorrectAnswers();
        int total = args.getTotalQuestions();
        int seconds = args.getTimeSeconds();

        int accuracy = calculateAccuracy(correct,total);
        int points = calculatePoints(correct,accuracy);

        // Hero
        binding.textLabel.setText(args.getQuizTitle());
        binding.textScore.setText(getString(R.string.results_score,correct,total));
        binding.textSub.setText(messageFor(accuracy));

        // Stat boxes
        binding.statAccuracy.statValue.setText(getString(R.string.results_accuracy,accuracy));
        binding.statAccuracy.statLabel.setText(R.string.stat_accuracy);

        binding.statTime.statValue.setText(getString(R.string.quiz_timer, seconds / 60, seconds % 60));
        binding.statTime.statLabel.setText(R.string.stat_time);

        binding.statPoints.statValue.setText(getString(R.string.results_points, points));
        binding.statPoints.statLabel.setText(R.string.stat_points);

        // Buttons
        NavController navController = NavHostFragment.findNavController(this);
        binding.btnNextQuiz.setOnClickListener(v -> navController.popBackStack());
        binding.btnHome.setOnClickListener(v -> navController.popBackStack(R.id.homeFragment,false));

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    private int calculateAccuracy(int correct, int total) {
        if (total == 0) {
            return 0;
        }
        return Math.round(correct * 100f / total);
    }

    private int calculatePoints(int correct, int accuracy) {
        int points = correct * POINTS_PER_CORRECT_ANSWER;
        if (accuracy == 100){
            points += PERFECT_SCORE_BONUS;
        }
        return points;
    }

    private int messageFor(int accuracy){
        if (accuracy == 100) return R.string.results_msg_perfect;
        if (accuracy >= 80) return  R.string.results_msg_great;
        if (accuracy >= 50) return R.string.results_msg_good;
        return R.string.results_msg_keep;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
