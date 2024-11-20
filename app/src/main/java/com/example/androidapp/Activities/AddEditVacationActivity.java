package com.example.androidapp.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;
import com.example.androidapp.Database.VacationsDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditVacationActivity extends AppCompatActivity {

    /* Below, the type EditText is a class within the Android SDK and is used
     * as a view/widget for inputting text. These variables will later be attached to
     * their xml counter parts defined within AddEditVacationActivity.xml with
     * findViewById(R.id.{{id}})
     *
     * Button is a button. It is also a class within the Android SDK
     *
     */
    private EditText editTextTitle, editTextHotel, editTextStartDate, editTextEndDate;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Retrieves the current saved instance state (if any -- there most likely will be with this Activity)
        setContentView(R.layout.activity_add_edit_vacation); // Sets the current content view to the activity_add_edit_vacation.xml file

        /* Below, the class variables are being set equal/bound to the XML components  */
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextHotel = findViewById(R.id.editTextHotel);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        buttonSave = findViewById(R.id.buttonSave);

        /* setOnClickListener attached to the save button that calls the saveVacation method within this class */
        buttonSave.setOnClickListener(view -> saveVacation());
    }

    private void saveVacation() {
        ExecutorService databaseThread = Executors.newSingleThreadExecutor();
        // Below initializes variables to store the values inputted to the EditText widgets
        String title = editTextTitle.getText().toString(); // gets the text within the field and converts it to a string
        String hotel = editTextHotel.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

        // A new VacationEntity object is createed and inserted into the database
        VacationEntity vacation = new VacationEntity(title, hotel, startDate, endDate);

        databaseThread.execute(() -> { // Second thread for database operations, allows main thread to focus on UI and not freeze UI
            /*
             * The line below, .getInstance() is a singleton method within the VacationsDatabase class,
             * which basically means that the instance field inside of vacationsDatabase is static and there is
             * only one instance of it across this entire app. It takes the parameter "context" in case an instance
             * does not exist, because in order to create an instance, context is required (this will allow the room
             * databaseBuilder to sync with the apps lifecycle and know where to store the database file).
             *
             * .vacationDAO accesses the DAO interface (which the room framework has implemented)
             *  for the vacations table and .insertVacation() inserts the passed value into the table.
             *
             */
            VacationsDatabase.getInstance(this).vacationDao().insertVacation(vacation);
        });
        databaseThread.shutdown();
        finish(); // This is part of the Activity class. It "finishes"/closes the current activity and removes it from the activity stack
    }
}
