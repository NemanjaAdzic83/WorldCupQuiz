package com.nemanja.worldcupquiz.data.model;

public enum CategoryType {
    /**
     * The thematic type of a question category.
     * Used for tabs on the Category screen (All Questions / Finals / Players / Records)
     * and for the quiz "Records and Legends" which mixes questions from all eras.
     */

    GENERAL,        // general questions about the tournament
    FINALS,         // finals and winners
    PLAYERS,        // players and legends
    RECORDS,        // records and statistics
    HOSTS,          // hosts and fun facts
    TRIVIA          // miscellaneous

}
