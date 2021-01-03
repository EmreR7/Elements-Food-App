package com.example.elementsfoodapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    // @Update and @Delete available

    @Insert
    void insert(Food food);

    @Query("DELETE FROM food_table")
    void deleteAll();

    @Query("SELECT * from food_table ORDER BY food ASC")
    LiveData<List<Food>> getAllFoods();
}
