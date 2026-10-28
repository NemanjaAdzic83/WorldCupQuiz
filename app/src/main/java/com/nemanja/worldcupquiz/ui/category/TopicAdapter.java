package com.nemanja.worldcupquiz.ui.category;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.databinding.ItemTopicCardBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * List of tournaments (one card each) on the Category screen.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(Tournament tournament);
    }

    // Badge colors rotate by position - purely visual variety.
    private static final int[] BADGE_COLORS = {
            R.color.pitch_green, R.color.navy_soft, R.color.red_wrong, R.color.gold_dark
    };

    private final List<Tournament> items = new ArrayList<>();
    private final OnTopicClickListener listener;

    public TopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Tournament> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopicCardBinding binding = ItemTopicCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TopicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        int colorRes = BADGE_COLORS[position % BADGE_COLORS.length];
        holder.bind(items.get(position), listener, colorRes);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {

        private final ItemTopicCardBinding binding;

        TopicViewHolder(ItemTopicCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Tournament tournament, OnTopicClickListener listener, @ColorRes int colorRes) {
            Context context = binding.getRoot().getContext();

            // "1986" -> "86"
            String yearShort = String.valueOf(tournament.getYear()).substring(2);
            binding.yearBadge.setText(yearShort);
            binding.yearBadge.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)));

            binding.textTitle.setText(context.getString(R.string.continue_title_tournament,
                    tournament.getYear(), tournament.getHostCountry()));

            if (tournament.getMascotName() != null) {
                binding.textMascotName.setVisibility(View.VISIBLE);
                binding.textMascotName.setText(
                        context.getString(R.string.topic_mascot, tournament.getMascotName()));
            } else {
                binding.textMascotName.setVisibility(View.GONE);
            }

            if (tournament.getNotableFact() != null) {
                binding.textFact.setVisibility(View.VISIBLE);
                binding.textFact.setText(tournament.getNotableFact());
            } else {
                binding.textFact.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> listener.onTopicClick(tournament));
        }
    }
}
