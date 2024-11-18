package com.example.androidapp.DAO;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.Entities.ExcursionEntity;

import java.util.List;

@Dao
public interface ExcursionDAO { // How to access/interact with Excursion table below - Task B.1 - Crud
    @Insert
    long insertExcursion(ExcursionEntity excursion);

    @Update
    void updateExcursion(ExcursionEntity excursion);

    @Delete
    void deleteExcursion(ExcursionEntity excursion);

    @Query("SELECT * FROM ExcursionEntity WHERE vacation_Id = :vacationId")
    List<ExcursionEntity> getExcursionsForVacation(int vacationId);
}
