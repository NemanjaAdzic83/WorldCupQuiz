package com.nemanja.worldcupquiz.data.local;


import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.local.entity.QuizCategory;
import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.model.CategoryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Starter data inserted on first launch: categories, a representative set of
 * tournaments across all eras, sample questions and stadiums.
 * More questions will be added later in the same structure.
 *
 * Package-private (no "public"): only AppDatabase in this package uses it.
 */
final class DatabaseSeeder {

    private DatabaseSeeder() {
        // No instances - this class only has a static method.
    }

    static void seed(AppDatabase db) {
        // Everything inside runs as ONE transaction: all rows are saved, or none.
        db.runInTransaction(() -> {
            if (db.tournamentDao().countSync() > 0) {
                return; // already seeded - do not duplicate
            }

            // ---------- 1. Categories (parents) ----------
            int catGeneral = (int) db.quizCategoryDao().insert(
                    new QuizCategory("General", CategoryType.GENERAL));
            int catFinals = (int) db.quizCategoryDao().insert(
                    new QuizCategory("Finals & Winners", CategoryType.FINALS));
            int catPlayers = (int) db.quizCategoryDao().insert(
                    new QuizCategory("Players & Legends", CategoryType.PLAYERS));
            int catRecords = (int) db.quizCategoryDao().insert(
                    new QuizCategory("Records & Stats", CategoryType.RECORDS));
            int catHosts = (int) db.quizCategoryDao().insert(
                    new QuizCategory("Hosts & Fun Facts", CategoryType.HOSTS));
            db.quizCategoryDao().insert(
                    new QuizCategory("Trivia", CategoryType.TRIVIA));

            // ---------- 2. Tournaments (parents) ----------
            // Tournament(year, host, winner, runnerUp, mascot, notableFact, teamsCount)
            int t1930 = (int) db.tournamentDao().insert(new Tournament(
                    1930, "Uruguay", "Uruguay", "Argentina", null,
                    "The first World Cup ever, played entirely in Montevideo.", 13));

            db.tournamentDao().insert(new Tournament(
                    1966, "England", "England", "West Germany", "World Cup Willie",
                    "The first official World Cup mascot - a lion in an England shirt.", 16));

            db.tournamentDao().insert(new Tournament(
                    1974, "West Germany", "West Germany", "Netherlands", "Tip and Tap",
                    "Cruyff's Netherlands showed the world \"Total Football\".", 16));

            db.tournamentDao().insert(new Tournament(
                    1978, "Argentina", "Argentina", "Netherlands", "Gauchito",
                    "The hosts won their first title amid a tense political climate.", 16));

            db.tournamentDao().insert(new Tournament(
                    1982, "Spain", "Italy", "West Germany", "Naranjito",
                    "Paolo Rossi finished as top scorer and led Italy to the title.", 24));

            int t1986 = (int) db.tournamentDao().insert(new Tournament(
                    1986, "Mexico", "Argentina", "West Germany", "Pique",
                    "Maradona's \"Hand of God\" and the \"Goal of the Century\" against England.", 24));

            db.tournamentDao().insert(new Tournament(
                    1990, "Italy", "West Germany", "Argentina", "Ciao",
                    "Known for defensive football and the anthem \"Un'estate italiana\".", 24));

            db.tournamentDao().insert(new Tournament(
                    1994, "USA", "Brazil", "Italy", "Striker",
                    "The first World Cup final decided by a penalty shootout.", 24));

            db.tournamentDao().insert(new Tournament(
                    2010, "South Africa", "Spain", "Netherlands", "Zakumi",
                    "The first World Cup in Africa, famous for the vuvuzelas.", 32));

            int t2014 = (int) db.tournamentDao().insert(new Tournament(
                    2014, "Brazil", "Germany", "Argentina", "Fuleco",
                    "Germany stunned the hosts 7-1 in the semi-final.", 32));

            db.tournamentDao().insert(new Tournament(
                    2018, "Russia", "France", "Croatia", "Zabivaka",
                    "Croatia reached its first ever World Cup final.", 32));

            int t2022 = (int) db.tournamentDao().insert(new Tournament(
                    2022, "Qatar", "Argentina", "France", "La'eeb",
                    "Messi finally lifted the trophy at the first winter World Cup.", 32));

            int t2026 = (int) db.tournamentDao().insert(new Tournament(
                    2026, "USA, Canada & Mexico", "Spain", "Argentina", "Maple, Zayu and Clutch",
                    "The first World Cup with 48 teams and the first hosted by three countries.", 48));

            // ---------- 3. Questions (children) ----------
            // Question(tournamentId, categoryId, difficulty, text, A, B, C, D, correctIndex, explanation)
            List<Question> questions = new ArrayList<>();

            questions.add(new Question(t1930, catGeneral, 1,
                    "Which team won the first World Cup in 1930?",
                    "Brazil", "Uruguay", "Argentina", "Italy", 1,
                    "Hosts Uruguay beat Argentina 4-2 in the final."));

            questions.add(new Question(t1986, catPlayers, 2,
                    "Who scored the famous \"Hand of God\" goal?",
                    "Diego Maradona", "Jorge Valdano", "Gary Lineker", "Careca", 0,
                    "Maradona scored it with his hand against England in the quarter-final."));

            questions.add(new Question(t1986, catRecords, 3,
                    "How many teams played at the 1986 World Cup in Mexico?",
                    "16", "20", "24", "32", 2,
                    "1986 was the second World Cup with 24 teams."));

            questions.add(new Question(t2014, catRecords, 2,
                    "What was the score when Germany beat Brazil in the 2014 semi-final?",
                    "5-0", "7-1", "4-0", "6-2", 1,
                    "Known as the \"Mineirazo\", one of the biggest shocks in World Cup history."));

            questions.add(new Question(t2022, catFinals, 3,
                    "Who won the 2022 World Cup final on penalties?",
                    "France", "Argentina", "Croatia", "Morocco", 1,
                    "Argentina won 4-2 on penalties after a 3-3 draw."));

            questions.add(new Question(t2026, catFinals, 2,
                    "Who won the 2026 World Cup final?",
                    "Argentina", "France", "Spain", "Brazil", 2,
                    "Spain beat Argentina 1-0 after extra time at MetLife Stadium."));

            // Cross-era questions: tournamentId = null ("Records & Legends")
            questions.add(new Question(null, catRecords, 2,
                    "Who is the all-time top scorer in World Cup history?",
                    "Miroslav Klose", "Ronaldo Nazario", "Pele", "Lionel Messi", 0,
                    "Miroslav Klose scored 16 goals across four World Cups (2002-2014)."));

            questions.add(new Question(null, catHosts, 1,
                    "Which was the first World Cup hosted by three countries?",
                    "2002 (Japan & South Korea)", "2026 (USA, Canada & Mexico)",
                    "2018 (Russia)", "1994 (USA)", 1,
                    "2002 had two hosts; 2026 was the first with three."));

            db.questionDao().insertAll(questions);

            // ---------- 4. Stadiums (children, info section only) ----------
            // Stadium(tournamentId, name, city, country, capacity, yearOpened, imageAsset, highlightTag, funFact)
            List<Stadium> stadiums = new ArrayList<>();

            stadiums.add(new Stadium(t2022, "Lusail Stadium", "Lusail", "Qatar",
                    88966, 2021, "stadium_lusail", "World Cup Final",
                    "The largest stadium of the tournament - Argentina won the title here on penalties."));

            stadiums.add(new Stadium(t2022, "Al Bayt Stadium", "Al Khor", "Qatar",
                    60000, 2021, "stadium_albayt", "Opening match",
                    "Designed to look like a traditional Bedouin tent (\"bayt al sha'ar\")."));

            stadiums.add(new Stadium(t2022, "Stadium 974", "Doha", "Qatar",
                    44089, 2021, "stadium_974", "Demountable stadium",
                    "The first fully demountable World Cup stadium, built from shipping containers."));

            stadiums.add(new Stadium(t2026, "MetLife Stadium", "East Rutherford (New Jersey)", "USA",
                    82500, 2010, "stadium_metlife", "World Cup Final",
                    "Hosted the 2026 final, where Spain beat Argentina 1-0 after extra time."));

            stadiums.add(new Stadium(t2026, "Estadio Azteca", "Mexico City", "Mexico",
                    80824, 1966, "stadium_azteca", "Opening match",
                    "The first stadium to host matches at three World Cups (1970, 1986 and 2026)."));

            db.stadiumDao().insertAll(stadiums);
        });
    }
}
