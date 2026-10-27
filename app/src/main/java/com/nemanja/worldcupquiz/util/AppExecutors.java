package com.nemanja.worldcupquiz.util;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Central place for threads in the application.
 * diskIO() - background thread for working with the database (slow tasks)
 * mainThread() - brings work back to the main thread (only it can change the screen)
 * <p>
 * Singleton: there's only one instance of this class in the whole app.
 */
public class AppExecutors {

    // The only instance of this class (Singleton). Initially, it's null
    private static volatile AppExecutors instance;

    // Background thread for the database.
    private final ExecutorService diskIO;

    // "Executor" that sends the job to the main thread.
    private final Executor mainThread;

    // Private constructor - no one outside the class can write new AppExecutors().
    private AppExecutors(ExecutorService diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    // The only way to get the object: AppExecutors.getInstance()
    public static AppExecutors getInstance() {
        if (instance == null) {                              // 1. check (quick, without locking)
            synchronized (AppExecutors.class) {              // lock - only one thread enters
                if (instance == null) {                      // 2. check (safe)
                    // Executors.newSingleThreadExecutor() : background worker with a single thread. Jobs go one after another, one by one, so database entries never collide.
                    instance = new AppExecutors(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
                }
            }
        }
        return instance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    // Inner class: Executor that queues every job on the main thread.
    private static class MainThreadExecutor implements Executor {
        /*
            Looper is the main thread's loop: it constantly takes tasks from the queue and executes them (drawing, touches…).
            Handler is the 'mailman' that puts new tasks into that queue.
            Looper.getMainLooper() gives the main thread's loop. That's why this Handler sends tasks to the main thread.
         */
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        /*
            A Runnable is a 'unit of work': a piece of code that needs to be executed.
            mainHandler.post(command) puts that work in the main thread's queue.
         */
        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);
        }
    }
}
