package com.nemanja.worldcupquiz.ui.stadiums;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.databinding.ItemWcPillBinding;

import java.util.ArrayList;
import java.util.List;

public class WcPillAdapter extends RecyclerView.Adapter<WcPillAdapter.PillViewHolder> {

    public interface OnPillClickListener{
        void onPillClick(Tournament tournament);
    }

    private final List<Tournament> items = new ArrayList<>();
    private final OnPillClickListener listener;

    public WcPillAdapter(OnPillClickListener listener){
        this.listener = listener;
    }

    public void submitList(List<Tournament> newItems){
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWcPillBinding binding = ItemWcPillBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PillViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PillViewHolder extends RecyclerView.ViewHolder{

        private final ItemWcPillBinding binding;

        PillViewHolder(ItemWcPillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Tournament tournament, OnPillClickListener listener){
            binding.pillYear.setText(String.valueOf(tournament.getYear()));
            binding.pillHost.setText(String.valueOf(tournament.getHostCountry()));
            binding.getRoot().setOnClickListener(v -> listener.onPillClick(tournament));
        }
    }
}
