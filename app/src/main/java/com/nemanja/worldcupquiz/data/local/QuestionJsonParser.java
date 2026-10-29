package com.nemanja.worldcupquiz.data.local;

import androidx.annotation.Nullable;


import com.nemanja.worldcupquiz.data.local.entity.Question;
import com.nemanja.worldcupquiz.data.model.CategoryType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Turns questions.json into Question objects.
 * The file refers to tournaments by YEAR and to categories by TYPE NAME,
 * so whoever writes questions never needs to know database ids.
 * Every question is validated; a bad one stops seeding with a clear message.
 */
final class QuestionJsonParser {

    private static final int OPTION_COUNT = 4;

    private QuestionJsonParser() {
        // No instances - static methods only.
    }

    /**
     * @param json          content of questions.json
     * @param tournamentIds tournament year -> database id
     * @param categoryIds   category type   -> database id
     */
    static List<Question> parse(String json,
                                Map<Integer, Integer> tournamentIds,
                                Map<CategoryType, Integer> categoryIds) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray array = root.getJSONArray("questions");

        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            questions.add(parseQuestion(item, i + 1, tournamentIds, categoryIds));
        }
        return questions;
    }

    private static Question parseQuestion(JSONObject item, int number,
                                          Map<Integer, Integer> tournamentIds,
                                          Map<CategoryType, Integer> categoryIds) throws JSONException {
        // Tournament: year -> id (null year = cross-era question)
        Integer tournamentId = null;
        if (!item.isNull("tournamentYear")) {
            int year = item.getInt("tournamentYear");
            tournamentId = tournamentIds.get(year);
            if (tournamentId == null) {
                throw new JSONException("Question #" + number + ": no tournament for year " + year);
            }
        }

        // Category: "PLAYERS" -> CategoryType.PLAYERS -> id
        String categoryName = item.getString("category");
        Integer categoryId = null;
        try {
            categoryId = categoryIds.get(CategoryType.valueOf(categoryName));
        } catch (IllegalArgumentException e) {
            // valueOf() throws this when the name is not one of the enum constants
        }
        if (categoryId == null) {
            throw new JSONException("Question #" + number + ": unknown category \"" + categoryName + "\"");
        }

        // Exactly four options
        JSONArray options = item.getJSONArray("options");
        if (options.length() != OPTION_COUNT) {
            throw new JSONException("Question #" + number + ": expected " + OPTION_COUNT
                    + " options, found " + options.length());
        }

        // Correct answer index must point to one of the options
        int correct = item.getInt("correct");
        if (correct < 0 || correct >= OPTION_COUNT) {
            throw new JSONException("Question #" + number + ": \"correct\" must be 0-3, found " + correct);
        }

        @Nullable String explanation = item.isNull("explanation") ? null : item.getString("explanation");

        return new Question(
                tournamentId,
                categoryId,
                item.getInt("difficulty"),
                item.getString("text"),
                options.getString(0),
                options.getString(1),
                options.getString(2),
                options.getString(3),
                correct,
                explanation);
    }
}
