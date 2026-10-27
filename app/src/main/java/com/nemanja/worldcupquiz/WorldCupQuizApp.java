package com.nemanja.worldcupquiz;

import android.app.Application;

import com.nemanja.worldcupquiz.data.local.AppDatabase;


/**
 * Created by Android before any screen, lives as long as the app process.
 */
public class WorldCupQuizApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Prepares the Room database object early. This is cheap: build() does
        // not open the database file - that happens on the first query.
        AppDatabase.getInstance(this);
    }
}
