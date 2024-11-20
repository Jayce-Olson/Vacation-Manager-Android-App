package com.example.androidapp.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


/* The fact that android studio can basically predict what you are going to write before you write it is literally
awesome. It is trying to do it with this comment right now haha. Anyways I just wanted to explain the Annotation below
because it took me a second to understand it. First off, Entity represents a Table in a database. Inside of @Entity you
can set the foreignKeys parameter. You do this in a somewhat quirky way because you use the @ForeignKey annotation to do
so. This takes the parameters of entity, parentColumns, and childColumns. VacationEntity just points to the entities table
and parentColumns/childColumns point to the primary/foreign keys of the VacationEntity table.
 */


@Entity(foreignKeys = @ForeignKey(entity = VacationEntity.class, parentColumns = "id", childColumns = "vacation_id"), tableName = "excursions")
public class ExcursionEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "vacation_id")
    private int vacationId;

    // Sadly Lombok was having issues with Room Framework so Getters/Setters are below

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

}
