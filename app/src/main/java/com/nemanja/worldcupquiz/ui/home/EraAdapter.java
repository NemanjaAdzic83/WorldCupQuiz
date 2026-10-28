package com.nemanja.worldcupquiz.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nemanja.worldcupquiz.R;
import com.nemanja.worldcupquiz.data.model.Era;
import com.nemanja.worldcupquiz.databinding.ItemEraCardBinding;
import com.nemanja.worldcupquiz.ui.model.EraUiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the five era cards in a grid on the Home screen.
 */
public class EraAdapter extends RecyclerView.Adapter<EraAdapter.EraViewHolder> {

    /** Reports a click on an era card back to the Fragment. */
    public interface OnEraClickListener {
        void onEraClick(EraUiModel era);
    }

    private final List<EraUiModel> items = new ArrayList<>();
    private final OnEraClickListener listener;

    public EraAdapter(OnEraClickListener listener) {
        this.listener = listener;
    }

    /** Replaces the whole list and redraws it. */
    public void submitList(List<EraUiModel> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEraCardBinding binding = ItemEraCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new EraViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EraViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Holds the views of one era card. */
    static class EraViewHolder extends RecyclerView.ViewHolder {

        private final ItemEraCardBinding binding;

        EraViewHolder(ItemEraCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EraUiModel model, OnEraClickListener listener) {
            Context context = binding.getRoot().getContext();
            Era era = model.getEra();
            int count = model.getTournamentCount();

            binding.textYears.setText(
                    context.getString(R.string.era_years, era.getStartYear(), era.getEndYear()));
            binding.textName.setText(era.getDisplayNameRes());
            binding.textCount.setText(context.getResources()
                    .getQuantityString(R.plurals.era_tournament_count, count, count));

            binding.getRoot().setOnClickListener(v -> listener.onEraClick(model));
        }
    }
}
