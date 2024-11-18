package com.example.androidapp.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VacationEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="hotel")
    private String hotel;

    @ColumnInfo(name="start")
    private String start;

    @ColumnInfo(name="end")
    private String end;
}
