package com.example.androidapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;
import com.example.androidapp.Database.VacationsDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AddEditVacationActivity extends AppCompatActivity {

    /* Below, the type EditText is a class within the Android SDK and is used
     * as a view/widget for inputting text. These variables will later be attached to
     * their xml counter parts defined within AddEditVacationActivity.xml with
     * findViewById(R.id.{{id}})
     *
     * Button is a button. It is also a class within the Android SDK
     *
     */
    private EditText editTextTitle, editTextHotel,  editTextEndDate;
    private TextView textViewStartDate, textViewEndDate;
    private Button buttonSave, buttonPickStartDate, buttonPickEndDate;
    private Date startDate = null, endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Retrieves the current saved instance state (if any -- there most likely will be with this Activity)
        setContentView(R.layout.activity_add_edit_vacation); // Sets the current content view to the activity_add_edit_vacation.xml file

        /* Below, the class variables are being set equal/bound to the XML components  */
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextHotel = findViewById(R.id.editTextHotel);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent(); // Getting intent to access any potentially passed data - There will be no data if add icon was clicked

        boolean isEditMode = intent.getBooleanExtra("isEditMode", false); // false is the default value "isEditMode" was not passed


        if (isEditMode) {
            // Retrieve passed intent data
            String title = intent.getStringExtra("Title");
            String startDate = intent.getStringExtra("StartDate");
            String endDate = intent.getStringExtra("EndDate");
            String hotel = intent.getStringExtra("Hotel");
            // Set fields
            editTextTitle.setText(title);
            textViewStartDate.setText(startDate);
            textViewEndDate.setText(endDate);
            editTextHotel.setText(hotel);
        }

        /* setOnClickListener attached to the save button that calls the saveVacation method within this class */
        buttonSave.setOnClickListener(view -> saveVacation());

        // Functionality for the back button (it is on a "custom" toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // The buttons for choosing the date
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        buttonPickStartDate = findViewById(R.id.buttonPickStartDate);
        buttonPickEndDate = findViewById(R.id.buttonPickEndDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        // Pick start date button with validation logic
        buttonPickStartDate.setOnClickListener(view -> showDatePicker((date) -> {
            try {
                startDate = dateFormat.parse(date);
                if(endDate==null){
                    textViewStartDate.setText(date);
                    return;
                }
                if(startDate.before(endDate)) {
                    textViewStartDate.setText(date);
                }
                Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                Log.e("DateParsingError", "Error parsing start date: " + date, e);
            }
        }));

        // Pick end date button with validation logic
        buttonPickEndDate.setOnClickListener(v -> showDatePicker((date) -> {
            try {
                endDate = dateFormat.parse(date);
                if(startDate==null) { // no need to validate if endate is before startdate if the startdate is null
                    textViewEndDate.setText(date);
                    return;
                }
                if(endDate.after(startDate)){ // validate end date
                    textViewEndDate.setText(date);
                    return;
                }
                Toast.makeText(this, "Invalid input date", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("DateParsingError", "Error parsing start date: " + date, e);

            }
        }));
    }

    private void saveVacation() {
        ExecutorService databaseThread = Executors.newSingleThreadExecutor();
        // Below initializes variables to store the values inputted to the EditText widgets
        String title = editTextTitle.getText().toString(); // gets the text within the field and converts it to a string
        String hotel = editTextHotel.getText().toString();
        String startDate = textViewStartDate.getText().toString();
        String endDate = textViewEndDate.getText().toString();

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

    private void showDatePicker(Consumer<String> callback) { // I am using consumer string to call lambda rather than having it implement an interface method
        // This shows the date picker menu overlay
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month + 1) + "-" + dayOfMonth;
            /* The code in .onDatePicked() will be the lambda/callback, date will be passed to it */
            callback.accept(date);
        }, 2024, 0, 1);
        datePickerDialog.show();
    }

}
