package com.example.elementsfoodapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Food.class}, version = 5, exportSchema = false)
public abstract class FoodRoomDatabase extends RoomDatabase {

    public abstract FoodDao foodDao();
    private static FoodRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static FoodRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FoodRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodRoomDatabase.class, "food_database")
                            .createFromAsset("database/foodelements_data.db")
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}