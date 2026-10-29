package com.nemanja.worldcupquiz.ui.stadiums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.databinding.ItemStadiumCardBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.StadiumViewHolder> {

    private final List<Stadium> items = new ArrayList<>();

    public void submitList(List<Stadium> newItems){
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StadiumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStadiumCardBinding binding = ItemStadiumCardBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false);
        return new StadiumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StadiumViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StadiumViewHolder extends RecyclerView.ViewHolder {
        private final ItemStadiumCardBinding binding;
        public StadiumViewHolder(ItemStadiumCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Stadium stadium){
            Context context = binding.getRoot().getContext();

            binding.textStadiumName.setText(stadium.getName());
            binding.textStadiumCity.setText(context.getString(R.string.stadium_location, stadium.getCity(),stadium.getCountry()));

            String capacity = NumberFormat.getIntegerInstance().format(stadium.getCapacity());
            binding.factCapacity.setText(context.getString(R.string.stadium_capacity,capacity));

            if (stadium.getYearOpened() != null){
                binding.factOpened.setVisibility(View.VISIBLE);
                binding.factOpened.setText(context.getString(R.string.stadium_opened,stadium.getYearOpened()));
            }else {
                binding.factOpened.setVisibility(View.GONE);
            }

            if (stadium.getHighlightTag() != null){
                binding.factHighlight.setVisibility(View.VISIBLE);
                binding.factHighlight.setText(stadium.getHighlightTag());
            }else {
                binding.factHighlight.setVisibility(View.GONE);
            }

            if (stadium.getFunFact() != null){
                binding.noteDivider.setVisibility(View.VISIBLE);
                binding.textFunFact.setVisibility(View.VISIBLE);
                binding.textFunFact.setText(stadium.getFunFact());
            }else {
                binding.noteDivider.setVisibility(View.GONE);
                binding.textFunFact.setVisibility(View.GONE);
            }


        }
    }
}
