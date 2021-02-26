package com.example.elementsfoodapp.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {

    private FoodDao mFoodDao;
    private LiveData<List<Food>> mAllFoods;

    public FoodRepository(Application application) {
        FoodRoomDatabase db = FoodRoomDatabase.getDatabase(application);
        mFoodDao = db.foodDao();
        mAllFoods = mFoodDao.getAllFoods();
    }

    public LiveData<List<Food>> getAllFoods() { return mAllFoods; }

    public void insert(Food food) {
        FoodRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFoodDao.insert(food);
        });
    }

    public void deleteFood(Food food) {
        FoodRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFoodDao.deleteFood(food);
        });
    }

    public void update(Food food) {
        FoodRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFoodDao.update(food);
        });
    }

    public void deleteAll() {
        FoodRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFoodDao.deleteAll();
        });
    }

    public LiveData<List<Food>> getSearchResults(String foodName) {
            return mFoodDao.getSearchResults(foodName);
    }
}
