package com.nemanja.worldcupquiz.data.local.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One FIFA World Cup (e.g. 1986 - Mexico).
 * Central entity: questions, stadiums and user progress all reference a tournament.
 */
@Entity(
        tableName = "tournaments",
        indices = {@Index(value = "year", unique = true)}
)
public class Tournament {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /** Year the tournament was held, e.g. 2022. Must be unique. */
    private int year;

    @NonNull
    private String hostCountry;

    /** Null while the tournament is not finished yet. */
    @Nullable
    private String winner;

    @Nullable
    private String runnerUp;

    /**
     * Mascot name only (e.g. "Zabivaka").
     * The official mascot artwork is NOT stored/shown because of copyright.
     */
    @Nullable
    private String mascotName;

    /** Short fun fact shown on the Category screen. */
    @Nullable
    private String notableFact;

    private int teamsCount;

    public Tournament(int year, @NonNull String hostCountry, @Nullable String winner,
                      @Nullable String runnerUp, @Nullable String mascotName,
                      @Nullable String notableFact, int teamsCount) {
        this.year = year;
        this.hostCountry = hostCountry;
        this.winner = winner;
        this.runnerUp = runnerUp;
        this.mascotName = mascotName;
        this.notableFact = notableFact;
        this.teamsCount = teamsCount;
    }

    // ----- Getters & setters (Room uses them to read and write fields) -----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @NonNull
    public String getHostCountry() {
        return hostCountry;
    }

    public void setHostCountry(@NonNull String hostCountry) {
        this.hostCountry = hostCountry;
    }

    @Nullable
    public String getWinner() {
        return winner;
    }

    public void setWinner(@Nullable String winner) {
        this.winner = winner;
    }

    @Nullable
    public String getRunnerUp() {
        return runnerUp;
    }

    public void setRunnerUp(@Nullable String runnerUp) {
        this.runnerUp = runnerUp;
    }

    @Nullable
    public String getMascotName() {
        return mascotName;
    }

    public void setMascotName(@Nullable String mascotName) {
        this.mascotName = mascotName;
    }

    @Nullable
    public String getNotableFact() {
        return notableFact;
    }

    public void setNotableFact(@Nullable String notableFact) {
        this.notableFact = notableFact;
    }

    public int getTeamsCount() {
        return teamsCount;
    }

    public void setTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
    }
}