package com.example.androidapp.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.Entities.VacationEntity;

import java.util.List;

@Dao
public interface VacationDAO { // How to interact with Vacation table below - Task B.1 - Crud
    /* The annotations above the below allows the Room Framework to know  how to implement these */
    @Insert
    long insertVacation(VacationEntity vacation);

    @Update
    void updateVacation(VacationEntity vacation);

    @Delete
    void deleteVacation(VacationEntity vacation);

    @Query("SELECT * FROM vacations") // Using LiveData automatically multithreads the query - Doesn't work for writing to DB so that is why it isn't used above
    LiveData<List<VacationEntity>> getAllVacations();

    @Query("SELECT * FROM vacations WHERE id = :vacationId")
    VacationEntity getVacationById(int vacationId);

}
