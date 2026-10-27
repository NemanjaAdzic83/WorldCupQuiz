package com.nemanja.worldcupquiz.data.model;

import androidx.annotation.StringRes;

import com.nemanja.worldcupquiz.R;

/**
 * World Cup eras - used to group tournaments on the Home screen.
 * Intentionally NOT a database table: the era is derived from the
 * tournament year and never changes.
 */
public enum Era {

    EARLY(1930, 1950, R.string.era_early),
    RISE(1954, 1970, R.string.era_rise),
    GOLDEN(1974, 1990, R.string.era_golden),
    MODERN(1994, 2010, R.string.era_modern),
    NEW_AGE(2014,2026,R.string.era_new_age);

    private final int startYear;
    private final int endYear;

    @StringRes
    private final int displayNames;

    Era(int startYear, int endYear,@StringRes int displayNames) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.displayNames = displayNames;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    // String resource id of the era name, e.g. R.string.era_golden
    public int getDisplayNames() {
        return displayNames;
    }

    // Does the given year belong to this era? e.g. GOLDEN.contains(1986) -> true
    public boolean contains(int year){
        return year >= startYear && year <= endYear;
    }

    /*
        Returns the era that contains the given year.
        Throws IllegalArgumentException if no era matches (e.g.2030)
     */
    public static Era fromYear(int year){
        for (Era era : values()){
            if (era.contains(year)){
                return era;
            }
        }

        throw new IllegalArgumentException("Unknown era fro year: " + year);
    }
}
