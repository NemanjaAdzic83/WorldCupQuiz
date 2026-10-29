package com.nemanja.worldcupquiz.ui.stadiums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.nemanja.worldcupquiz.databinding.FragmentStadiumDetailBinding;

public class StadiumDetailFragment extends Fragment {

    private FragmentStadiumDetailBinding binding;
    private StadiumDetailViewModel viewModel;
    private StadiumAdapter stadiumAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStadiumDetailBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StadiumDetailViewModel.class);

        StadiumDetailFragmentArgs args = StadiumDetailFragmentArgs.fromBundle(requireArguments());
        binding.textTitle.setText(args.getTournamentTitle());

        binding.btnBack.setOnClickListener( v -> NavHostFragment.findNavController(this).navigateUp());

        stadiumAdapter = new StadiumAdapter();
        binding.recyclerStadiums.setAdapter(stadiumAdapter);

        viewModel.loadStadiums(args.getTournamentId());
        viewModel.getStadiums().observe(getViewLifecycleOwner(), stadiums -> {
            if (stadiums == null) return;

            boolean empty = stadiums.isEmpty();
            binding.recyclerStadiums.setVisibility(empty ? View.GONE : View.VISIBLE);
            binding.textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            stadiumAdapter.submitList(stadiums);
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
