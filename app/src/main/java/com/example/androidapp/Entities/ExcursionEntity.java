package com.example.androidapp.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/* The fact that android studio can basically predict what you are going to write before you write it is literally
awesome. It is trying to do it with this comment right now haha. Anyways I just wanted to explain the Annotation below
because it took me a second to understand it. First off, Entity represents a Table in a database. Inside of @Entity you
can set the foreignKeys parameter. You do this in a somewhat quirky way because you use the @ForeignKey annotation to do
so. This takes the parameters of entity, parentColumns, and childColumns. VacationEntity just points to the entities table
and parentColumns/childColumns point to the primary/foreign keys of the VacationEntity table.
 */


@Entity(foreignKeys = @ForeignKey(entity = VacationEntity.class, parentColumns = "id", childColumns = "vacation_id", onDelete = ForeignKey.CASCADE), tableName = "excursions")
public class ExcursionEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "vacation_id")
    private int vacationId;

    public static final DiffUtil.ItemCallback<ExcursionEntity> anonymousDiffUtil =
            new DiffUtil.ItemCallback<ExcursionEntity>() {
                @Override // DiffUtil.ItemCallback<VacationEntity> has abstract method "areItemsTheSame"
                public boolean areItemsTheSame(@NonNull ExcursionEntity oldItem, @NonNull ExcursionEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override // DiffUtil.ItemCallback<VacationEntity> has abstract method "areContentsTheSame"
                public boolean areContentsTheSame(@NonNull ExcursionEntity oldItem, @NonNull ExcursionEntity newItem) {
                    return oldItem.equals(newItem); // Calls the overrided equals method below
                }
            };


    /*
     * Another thing I learned today is that every class in Java implicitly extends the Object classes.
     * The equals class by default just checks if two objects for "reference" equality, which just checks
     * to see if two objects have the same memory address. The Overrided method below checks for "logical"
     * equality.
     *
     * */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Checks for reference equality first (the object is being compared to itself)
        if (obj == null || getClass() != obj.getClass()) return false; // This is for is somehow a null object, or object of an entirely different class gets through
        // Since I am overriding obj, I can't pass through specifically a VacationEntity object
        // And since Java's Type System does not explicitly know the objs type, I have to cast it to
        // ExcursionEntity in order to access its field/methods like .id
        ExcursionEntity excursionEntity = (ExcursionEntity) obj;
        // Below is checking for logical equality
        return id == excursionEntity.id &&
                title.equals(excursionEntity.title) &&
                date.equals(excursionEntity.date) &&
                vacationId == excursionEntity.vacationId;
    }

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
