package com.nemanja.worldcupquiz.data.local.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * A stadium where a World Cup was played. Purely informational -
 * NOT linked to questions, shown only in the "Stadiums" section.
 *
 * imageAsset is the name of a local drawable resource (e.g. "stadium_lusail"),
 * not a URL. Real stadium photos must be legally obtained (e.g. Wikimedia
 * Commons under a CC license, or your own photo) because they are usually
 * copyrighted.
 */
@Entity(
        tableName = "stadiums",
        foreignKeys = @ForeignKey(
                entity = Tournament.class,
                parentColumns = "id",
                childColumns = "tournamentId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("tournamentId")
)
public class Stadium {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int tournamentId;

    @NonNull
    private String name;

    @NonNull
    private String city;

    @NonNull
    private String country;

    private int capacity;

    @Nullable
    private Integer yearOpened;

    /** Local image resource name (placeholder until licensed photos are added). */
    @Nullable
    private String imageAsset;

    /** Short highlight label, e.g. "World Cup Final", "Opening match". */
    @Nullable
    private String highlightTag;

    @Nullable
    private String funFact;

    public Stadium(int tournamentId, @NonNull String name, @NonNull String city,
                   @NonNull String country, int capacity, @Nullable Integer yearOpened,
                   @Nullable String imageAsset, @Nullable String highlightTag,
                   @Nullable String funFact) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.city = city;
        this.country = country;
        this.capacity = capacity;
        this.yearOpened = yearOpened;
        this.imageAsset = imageAsset;
        this.highlightTag = highlightTag;
        this.funFact = funFact;
    }

    // ===== Ako koristiš Alt+Insert, zalijepi samo do ovdje =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Nullable
    public Integer getYearOpened() {
        return yearOpened;
    }

    public void setYearOpened(@Nullable Integer yearOpened) {
        this.yearOpened = yearOpened;
    }

    @Nullable
    public String getImageAsset() {
        return imageAsset;
    }

    public void setImageAsset(@Nullable String imageAsset) {
        this.imageAsset = imageAsset;
    }

    @Nullable
    public String getHighlightTag() {
        return highlightTag;
    }

    public void setHighlightTag(@Nullable String highlightTag) {
        this.highlightTag = highlightTag;
    }

    @Nullable
    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(@Nullable String funFact) {
        this.funFact = funFact;
    }
}
