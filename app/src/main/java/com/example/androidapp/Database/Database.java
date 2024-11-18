package com.example.androidapp.Database;

import androidx.room.RoomDatabase;

import com.example.androidapp.Entities.ExcursionEntity;
import com.example.androidapp.Entities.VacationEntity;

@Database(entities = {VacationEntity.class, ExcursionEntity.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();
}
