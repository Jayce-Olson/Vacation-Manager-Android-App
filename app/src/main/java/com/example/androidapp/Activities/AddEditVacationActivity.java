package com.example.androidapp.Activities;

import com.example.androidapp.Helpers.DateClickHelper;
import com.example.androidapp.Helpers.PopupClickHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;
import com.example.androidapp.Database.VacationsDatabase;

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
    private EditText editTextTitle, editTextHotel,  editTextEndDate;
    private TextView textViewStartDate, textViewEndDate;
    private Toolbar toolbar;
    private ImageView menuButton;
    private Button buttonSave, buttonPickStartDate, buttonPickEndDate;
    boolean isEditMode = false;
    private VacationEntity vacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Retrieves the current saved instance state (if any -- there most likely will be with this Activity)
        setContentView(R.layout.activity_add_edit_vacation); // Sets the current content view to the activity_add_edit_vacation.xml file

        bindAndSetComponents(); // Method set up to bind Xml components to class variables (and pre fill areas if editing)
        setupButtons(); // Method setup to handle the listeners for the buttons on the activity

    }

    private void saveVacation() { // Only called on save button click

        // Below initializes variables to store the values inputted to the EditText widgets
        String title = editTextTitle.getText().toString(); // gets the text within the field and converts it to a string
        String hotel = editTextHotel.getText().toString();
        String startDate = textViewStartDate.getText().toString();
        String endDate = textViewEndDate.getText().toString();
        ExecutorService databaseThread = Executors.newSingleThreadExecutor();

        if(isEditMode){ // update current entity with new information
            vacation.setTitle(title);
            vacation.setHotel(hotel);
            vacation.setStartDate(startDate);
            vacation.setEndDate(endDate);
            databaseThread.execute(() -> {
                VacationsDatabase.getInstance(this).vacationDao().updateVacation(vacation);
            });
        }else {// A new VacationEntity object is created and inserted into the database
            VacationEntity vacation = new VacationEntity(title, hotel, startDate, endDate);
            databaseThread.execute(() -> { // Second thread for database operations, allows main thread to focus on UI and not freeze UI
                /* (this comment is only here because this is a school assignment and I wanted to write is down to help learn/remember it - I would not do this in professional environment):
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
        }
        databaseThread.shutdown();
        finish(); // This is part of the Activity class. It "finishes"/closes the current activity and removes it from the activity stack
    }



    private void bindAndSetComponents(){
        // Below, the class variables are being set equal/bound to the XML components
        // Title/Hotel
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextHotel = findViewById(R.id.editTextHotel);
        // Save button
        buttonSave = findViewById(R.id.buttonSave);
        // Popup menu button
        menuButton = findViewById(R.id.menuButton);

        // The initialization and configuration for the custom toolbar that the back button is attached to
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // The text views and buttons for choosing the date
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        buttonPickStartDate = findViewById(R.id.buttonPickStartDate);
        buttonPickEndDate = findViewById(R.id.buttonPickEndDate);

        // Check if intent is for editing/creating with method inside this class
        checkMode();

    }

    private void checkMode(){
        // Getting intent to access any potentially passed data - There will be no data if add icon was clicked
        Intent intent = getIntent();
        // Retrieve intent info
        isEditMode = intent.getBooleanExtra("isEditMode", false); // false is the default value "isEditMode" was not passed

        /* Below is the logic for if an existing item was clicked on to be edited.*/
        if (isEditMode) {
            // Retrieve passed intent data
            vacation = (VacationEntity) getIntent().getSerializableExtra("vacation");
            String title = vacation.getTitle();
            String hotel = vacation.getHotel();
            String startDate = vacation.getStartDate();
            String endDate = vacation.getEndDate();
            // Set fields
            editTextTitle.setText(title);
            textViewStartDate.setText(startDate);
            textViewEndDate.setText(endDate);
            editTextHotel.setText(hotel);
        }
    }

    private void setupButtons(){
        /* setOnClickListener attached to the save button that calls the saveVacation method within this class */
        buttonSave.setOnClickListener(view -> saveVacation()); // I didn't put this in it's own file due to a lack of logic

        // Functionality for the back button (it is on a "custom" toolbar)
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // There is a separate set of these buttons for each items, which means separate values for start and end date.
        // which is why I decided not to make it static
        DateClickHelper dateClickHelper = new DateClickHelper();
        dateClickHelper.dateListener(this, buttonPickStartDate, buttonPickEndDate, textViewStartDate, textViewEndDate); // passing the button components
        // Listener and logic for the popup menu button - static method in PopupClickHelper file (within helper package)
        Activity activity = (Activity) this; // Activity will be needed for permissions later
        PopupClickHelper.popupListener(this, menuButton, vacation, activity);
    }

}
