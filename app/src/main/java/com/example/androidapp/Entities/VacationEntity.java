package com.example.androidapp.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "vacations")
public class VacationEntity implements Serializable { // This later implements Serializable so I can pass it through Intent() (within VacationAdapter)
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Below are the following required properties for a vacation entity
    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="hotel")
    private String hotel;

    @ColumnInfo(name="start")
    private String startDate;

    @ColumnInfo(name="end")
    private String endDate;


    public VacationEntity(String title, String hotel, String startDate, String endDate) {
        this.title = title;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate; // I love the fact that Android SDK basically automatically filled all of this for me.
    }

    /* I didn't mean for this to get this overally complex for a school assessment but by accident it slightly did.
        Below is a utility function that uses an "Anonymous Class Instantiation" which was a new concept to me, so
        i'll explain what it is. An anonymous class Instantiation is the creation of a class without a name. Here it
        is being used to implement the DiffUtil.ItemCallback<VacationEntity> class. Another way to describe
        the method below would be an "unnamed subclass of DiffUtil.ItemCallback<VacationEntity>".

        Below is like a shorthand sexy version of doing:
        public class VacationEntityDiffCallback extends DiffUtil.ItemCallback<VacationEntity> {

        but the benefit here is mainly just convince and code readability. Defining an anonymous class
        basically allows you to instantiate and implement required methods/fields at the same time.
     */
    public static final DiffUtil.ItemCallback<VacationEntity> anonymousDiffUtil =
            new DiffUtil.ItemCallback<VacationEntity>() {
                @Override // DiffUtil.ItemCallback<VacationEntity> has abstract method "areItemsTheSame"
                public boolean areItemsTheSame(@NonNull VacationEntity oldItem, @NonNull VacationEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override // DiffUtil.ItemCallback<VacationEntity> has abstract method "areContentsTheSame"
                public boolean areContentsTheSame(@NonNull VacationEntity oldItem, @NonNull VacationEntity newItem) {
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
        // VacationEntity in order to access its field/methods like .id
        VacationEntity vacationEntity = (VacationEntity) obj;
        // Below is checking for logical equality
        return id == vacationEntity.id && // return true/false if all the fields are the same (title, hotel, startDate, endDate)
                title.equals(vacationEntity.title) &&
                hotel.equals(vacationEntity.hotel) &&
                startDate.equals(vacationEntity.startDate) &&
                endDate.equals(vacationEntity.endDate);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, title, hotel, startDate, endDate);
//    }

    // Sadly I was unable to use Lombok to automatically generate Getter/Setters as it was conflicting with Room framework
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

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
