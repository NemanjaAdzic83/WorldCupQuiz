package com.nemanja.worldcupquiz.ui.model;


import com.nemanja.worldcupquiz.data.model.Era;

/**
 * UI-only model (not stored in the database): an era plus how many
 * tournaments from the database fall into it. Built by HomeFragment
 * and displayed by EraAdapter.
 */
public class EraUiModel {

    private final Era era;
    private final int tournamentCount;

    public EraUiModel(Era era, int tournamentCount) {
        this.era = era;
        this.tournamentCount = tournamentCount;
    }

    public Era getEra() {
        return era;
    }

    public int getTournamentCount() {
        return tournamentCount;
    }
}