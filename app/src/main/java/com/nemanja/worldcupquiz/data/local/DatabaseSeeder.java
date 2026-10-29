package com.nemanja.worldcupquiz.data.local;


import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.local.entity.QuizCategory;
import com.nemanja.worldcupquiz.data.local.entity.Stadium;
import com.nemanja.worldcupquiz.data.local.entity.Tournament;
import com.nemanja.worldcupquiz.data.model.CategoryType;



import android.content.Context;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Fills an empty database: categories and tournaments (in code),
 * questions (from assets/questions.json) and stadiums (in code).
 */
final class DatabaseSeeder {

    private static final String QUESTIONS_FILE = "questions.json";

    private DatabaseSeeder() {
    }

    static void seed(AppDatabase db, Context context) {
        // Read the file BEFORE the transaction - file access does not belong inside it.
        String questionsJson = readAsset(context, QUESTIONS_FILE);

        db.runInTransaction(() -> {
            if (db.tournamentDao().countSync() > 0) {
                return; // already seeded
            }

            Map<CategoryType, Integer> categoryIds = insertCategories(db);
            Map<Integer, Integer> tournamentIds = insertTournaments(db);

            try {
                List<Question> questions =
                        QuestionJsonParser.parse(questionsJson, tournamentIds, categoryIds);
                db.questionDao().insertAll(questions);
            } catch (JSONException e) {
                // Invalid file: stop and roll back the whole transaction.
                throw new IllegalStateException("Invalid " + QUESTIONS_FILE + ": " + e.getMessage(), e);
            }

            insertStadiums(db, tournamentIds);
        });
    }

    // ---------- Categories ----------

    private static Map<CategoryType, Integer> insertCategories(AppDatabase db) {
        Map<CategoryType, Integer> ids = new EnumMap<>(CategoryType.class);
        addCategory(db, ids, "General", CategoryType.GENERAL);
        addCategory(db, ids, "Finals & Winners", CategoryType.FINALS);
        addCategory(db, ids, "Players & Legends", CategoryType.PLAYERS);
        addCategory(db, ids, "Records & Stats", CategoryType.RECORDS);
        addCategory(db, ids, "Hosts & Fun Facts", CategoryType.HOSTS);
        addCategory(db, ids, "Trivia", CategoryType.TRIVIA);
        return ids;
    }

    private static void addCategory(AppDatabase db, Map<CategoryType, Integer> ids,
                                    String name, CategoryType type) {
        int id = (int) db.quizCategoryDao().insert(new QuizCategory(name, type));
        ids.put(type, id);
    }

    // ---------- Tournaments ----------

    private static Map<Integer, Integer> insertTournaments(AppDatabase db) {
        // Tournament(year, host, winner, runnerUp, mascot, notableFact, teamsCount)
        List<Tournament> tournaments = Arrays.asList(
                new Tournament(1930, "Uruguay", "Uruguay", "Argentina", null,
                        "The first World Cup ever, played entirely in Montevideo.", 13),
                new Tournament(1966, "England", "England", "West Germany", "World Cup Willie",
                        "The first official World Cup mascot - a lion in an England shirt.", 16),
                new Tournament(1974, "West Germany", "West Germany", "Netherlands", "Tip and Tap",
                        "Cruyff's Netherlands showed the world \"Total Football\".", 16),
                new Tournament(1978, "Argentina", "Argentina", "Netherlands", "Gauchito",
                        "The hosts won their first title amid a tense political climate.", 16),
                new Tournament(1982, "Spain", "Italy", "West Germany", "Naranjito",
                        "Paolo Rossi finished as top scorer and led Italy to the title.", 24),
                new Tournament(1986, "Mexico", "Argentina", "West Germany", "Pique",
                        "Maradona's \"Hand of God\" and the \"Goal of the Century\" against England.", 24),
                new Tournament(1990, "Italy", "West Germany", "Argentina", "Ciao",
                        "Known for defensive football and the anthem \"Un'estate italiana\".", 24),
                new Tournament(1994, "USA", "Brazil", "Italy", "Striker",
                        "The first World Cup final decided by a penalty shootout.", 24),
                new Tournament(2010, "South Africa", "Spain", "Netherlands", "Zakumi",
                        "The first World Cup in Africa, famous for the vuvuzelas.", 32),
                new Tournament(2014, "Brazil", "Germany", "Argentina", "Fuleco",
                        "Germany stunned the hosts 7-1 in the semi-final.", 32),
                new Tournament(2018, "Russia", "France", "Croatia", "Zabivaka",
                        "Croatia reached its first ever World Cup final.", 32),
                new Tournament(2022, "Qatar", "Argentina", "France", "La'eeb",
                        "Messi finally lifted the trophy at the first winter World Cup.", 32),
                new Tournament(2026, "USA, Canada & Mexico", "Spain", "Argentina", "Maple, Zayu and Clutch",
                        "The first World Cup with 48 teams and the first hosted by three countries.", 48)
        );

        Map<Integer, Integer> ids = new HashMap<>();
        for (Tournament t : tournaments) {
            int id = (int) db.tournamentDao().insert(t);
            ids.put(t.getYear(), id);
        }
        return ids;
    }

    // ---------- Stadiums ----------

    private static void insertStadiums(AppDatabase db, Map<Integer, Integer> tournamentIds) {
        int t2022 = tournamentIds.get(2022);
        int t2026 = tournamentIds.get(2026);

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
    }

    // ---------- Reading a file from assets ----------

    private static String readAsset(Context context, String fileName) {
        try (InputStream input = context.getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(input, StandardCharsets.UTF_8))) {

            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
            return text.toString();

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read assets/" + fileName, e);
        }
    }
}