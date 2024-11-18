package com.example.androidapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.Entities.VacationEntity;

import java.util.List;

@Dao
public interface VacationDAO { // How to interact with Vacation table below - Task B.1 - Crud
    @Insert
    long insertVacation(VacationEntity vacation);

    @Update
    void updateVacation(VacationEntity vacation);

    @Delete
    void deleteVacation(VacationEntity vacation);

    @Query("SELECT * FROM VacationEntity")
    List<VacationEntity> getAllVacations();
}
