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

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.databinding.FragmentStadiumsBinding;

public class StadiumsFragment extends Fragment {

    private FragmentStadiumsBinding binding;
    private StadiumsViewModel viewModel;
    private WcPillAdapter pillAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStadiumsBinding.inflate(inflater,container,false);
        return  binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StadiumsViewModel.class);

        binding.btnBack.setOnClickListener( v -> NavHostFragment.findNavController(this).navigateUp());

        pillAdapter = new WcPillAdapter(this::onPillClicked);
        binding.recyclerPills.setAdapter(pillAdapter);

        viewModel.getTournaments().observe(getViewLifecycleOwner(), tournaments -> {
            if (tournaments != null){
                pillAdapter.submitList(tournaments);
            }
        });


    }

    private void onPillClicked(Tournament tournament) {
        String title = getString(R.string.stadium_detail_title, tournament.getYear(), tournament.getHostCountry());
        NavHostFragment.findNavController(this).navigate(StadiumsFragmentDirections.actionStadiumsToDetail(tournament.getId(),title));
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
