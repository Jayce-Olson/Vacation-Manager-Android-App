package com.example.androidapp.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.androidapp.DAO.VacationDAO;
import com.example.androidapp.DAO.ExcursionDAO;
import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.Entities.ExcursionEntity;

@Database(entities = {VacationEntity.class, ExcursionEntity.class}, version = 4)
public abstract class VacationsDatabase extends RoomDatabase { // This is abstract so that the room framework can extend/implement it at runtime

    private static VacationsDatabase instance; // Static to hold only a single instance of the Database

    /* The Room framework will automatically generate the implementations for the below DAO. The
    * Room framework knows to look for these in order to extend because of the @Database annotation.
    * Marking the DAO as abstract lets the Room framework know it needs to extend the DAOs (the Room framework
    * will extend this entire class, as well as other classes within this app).
    *
    * Inside the Interfaces for these there is more info but in short, the annotations in each interface allows
    * the Room framework to know how to extend them (what code to generate for each method).
    *
    *  */
    public abstract VacationDAO vacationDao();
    public abstract ExcursionDAO excursionDao();

    /*
      Below is the getInstance method for database. It is synchronized out of good practice to make
      sure multithreading doesn't call the method at the same time and accidentally create multiple
      instances of the Database at the same time.
     */
    public static synchronized VacationsDatabase getInstance(Context context) {
        if (instance == null) {
            /* The Room database builder requires context, which is why it has the be pass through for this method.
            * That didn't make much sense to me at first but after looking into it seems the context is needed because
            * the room databaseBuilder needs to know where to store the database file, and passing the context through
            * allows it to know where to store the file. Passing through the context also supposedly allows the database
            * to be tied to the apps lifecycle, which will help prevent memory leaks.
            *
            * The reason the context of the current file is because the current file extends RoomDatabase, which does
            * not extend Context. Most of the other files in this app extend Context though, such as the activity files.
            * */
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            VacationsDatabase.class, "vacations_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
