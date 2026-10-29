package com.nemanja.worldcupquiz.ui.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.databinding.FragmentQuizBinding;
import com.nemanja.worldcupquiz.databinding.ItemAnswerOptionBinding;

import java.util.List;

/**
 * Shows one question at a time with four answers, a timer and a progress bar.
 */
public class QuizFragment extends Fragment {

    private static final long NEXT_QUESTION_DELAY_MS = 900;
    private static final long TIMER_TICK_MS = 1000;

    private FragmentQuizBinding binding;
    private QuizViewModel viewModel;

    @Nullable
    private List<Question> questionList;
    private boolean answerLocked = false;
    private String quizTitle = "";

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            viewModel.tick();
            updateTimerDisplay();
            handler.postDelayed(this, TIMER_TICK_MS);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        QuizFragmentArgs args = QuizFragmentArgs.fromBundle(requireArguments());
        quizTitle = args.getQuizTitle();
        viewModel.start(args.getTournamentId(), args.getCategoryId());

        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        updateTimerDisplay();

        viewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions == null) return;

            if (questions.isEmpty()) {
                showEmptyState();
                return;
            }
            questionList = questions;
            if (viewModel.isFinished(questions.size())) {
                finishQuiz();
            } else {
                showQuestion();
                startTimer();
            }
        });
    }

    // ---------- Timer ----------

    private void startTimer() {
        handler.removeCallbacks(timerRunnable);   // never run two timers
        handler.postDelayed(timerRunnable, TIMER_TICK_MS);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }

    private void updateTimerDisplay() {
        int seconds = viewModel.getElapsedSeconds();
        binding.scoreboardTimer.setText(getString(R.string.quiz_timer, seconds / 60, seconds % 60));
    }

    // ---------- Showing a question ----------

    private void showEmptyState() {
        stopTimer();
        binding.scrollQuestion.setVisibility(View.GONE);
        binding.textEmpty.setVisibility(View.VISIBLE);
        binding.scoreboardProgress.setText(getString(R.string.quiz_progress, 0, 0));
    }

    private void showQuestion() {
        int index = viewModel.getCurrentIndex();
        int total = questionList.size();
        Question q = questionList.get(index);
        answerLocked = false;

        binding.scrollQuestion.setVisibility(View.VISIBLE);
        binding.textEmpty.setVisibility(View.GONE);

        binding.scoreboardProgress.setText(getString(R.string.quiz_progress, index + 1, total));
        updateProgressBar(index, total);

        binding.textEyebrow.setText(getString(R.string.quiz_question_number, index + 1));
        binding.textQuestion.setText(q.getQuestionText());

        bindAnswer(binding.answerA, q.getOptionA(), 0, q);
        bindAnswer(binding.answerB, q.getOptionB(), 1, q);
        bindAnswer(binding.answerC, q.getOptionC(), 2, q);
        bindAnswer(binding.answerD, q.getOptionD(), 3, q);
    }

    private void updateProgressBar(int answered, int total) {
        // The track has a width only after it is laid out - post() waits for that.
        binding.progressTrack.post(() -> {
            if (binding == null) return;
            int trackWidth = binding.progressTrack.getWidth();
            ViewGroup.LayoutParams params = binding.progressFill.getLayoutParams();
            params.width = Math.max(4, trackWidth * answered / total);
            binding.progressFill.setLayoutParams(params);
        });
    }

    private void bindAnswer(ItemAnswerOptionBinding answer, String text, int index, Question q) {
        char letter = (char) ('A' + index);
        answer.answerLetter.setText(String.valueOf(letter));
        answer.answerText.setText(text);

        // Reset colors (the card may still show the previous question's colors)
        answer.getRoot().setBackgroundResource(R.drawable.bg_answer_default);
        answer.answerLetter.setBackgroundResource(R.drawable.bg_circle_mint);

        answer.getRoot().setOnClickListener(v -> onAnswerSelected(index, q));
    }

    // ---------- Answering ----------

    private void onAnswerSelected(int selectedIndex, Question q) {
        if (answerLocked) return;   // ignore extra taps during the delay
        answerLocked = true;

        boolean isCorrect = selectedIndex == q.getCorrectOptionIndex();

        revealAnswer(binding.answerA, 0, selectedIndex, q);
        revealAnswer(binding.answerB, 1, selectedIndex, q);
        revealAnswer(binding.answerC, 2, selectedIndex, q);
        revealAnswer(binding.answerD, 3, selectedIndex, q);

        handler.postDelayed(() -> goToNextQuestion(isCorrect), NEXT_QUESTION_DELAY_MS);
    }

    /** Correct answer -> green. The selected wrong answer -> red. Others unchanged. */
    private void revealAnswer(ItemAnswerOptionBinding answer, int index, int selectedIndex, Question q) {
        if (index == q.getCorrectOptionIndex()) {
            answer.getRoot().setBackgroundResource(R.drawable.bg_answer_correct);
            answer.answerLetter.setBackgroundResource(R.drawable.bg_circle_green);
        } else if (index == selectedIndex) {
            answer.getRoot().setBackgroundResource(R.drawable.bg_answer_wrong);
            answer.answerLetter.setBackgroundResource(R.drawable.bg_circle_red);
        }
    }

    private void goToNextQuestion(boolean wasCorrect) {
        if (binding == null || questionList == null) return;   // screen already closed

        viewModel.advance(wasCorrect, questionList.size());

        if (viewModel.isFinished(questionList.size())) {
            finishQuiz();
        } else {
            showQuestion();
        }
    }

    private void finishQuiz() {
        stopTimer();
        int total = questionList != null ? questionList.size() : 0;
        NavHostFragment.findNavController(this).navigate(
                QuizFragmentDirections.actionQuizToResults(viewModel.getCorrectCount(),total, viewModel.getElapsedSeconds(), quizTitle));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);   // stop timer + pending "next question"
        binding = null;
    }
}
