package com.example.androidapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.Database.VacationsDatabase;
import com.example.androidapp.Entities.ExcursionEntity;
import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.Helpers.DateClickHelper;
import com.example.androidapp.Helpers.PopupClickHelper;
import com.example.androidapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcursionAddEditActivity extends AppCompatActivity {
    private EditText editTextExcursionTitle;
    private TextView textViewDate;
    private Button saveButton, deleteButton, buttonPickDate;
    private Toolbar toolbar;
    private int vacationId;
    private Boolean isEditMode;
    private ExcursionEntity excursion;
    private VacationEntity vacation;
    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_excursion);

        bindAndSetComponents();
        setupButtons();
    }

    private void bindAndSetComponents(){

        editTextExcursionTitle = findViewById(R.id.editTextExcursionTitle);
        textViewDate = findViewById(R.id.textViewDate);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        vacation = (VacationEntity) getIntent().getSerializableExtra("VACATION");
        vacationId = vacation.getId();
        startDate = vacation.getStartDate();
        endDate = vacation.getEndDate();

        // The initialization and configuration for the custom toolbar that the back button is attached to
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkMode();

    }

    private void checkMode(){
        // Getting intent to access any potentially passed data - There will be no data if add icon was clicked
        Intent intent = getIntent();
        // Retrieve intent info
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode) {
            excursion = (ExcursionEntity) intent.getSerializableExtra("Excursion");
            editTextExcursionTitle.setText(excursion.getTitle());
            textViewDate.setText(excursion.getDate());
        }
    }

    private void setupButtons(){
        // Functionality for the back button (it is on a "custom" toolbar)
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        saveButton.setOnClickListener(v -> {
          save();
        });

        // There is a separate set of these buttons for each items, which means separate values for dates.
        // which is why I decided not to make it static
        DateClickHelper dateClickHelper = new DateClickHelper();
        dateClickHelper.dateExcursionListener(this, buttonPickDate, textViewDate,startDate, endDate);

        deleteButton.setOnClickListener(v -> {
            if(!isEditMode){
                Toast.makeText(this, "Excursion has not been created yet", Toast.LENGTH_SHORT).show();
                return;
            }
            ExecutorService databaseThread = Executors.newSingleThreadExecutor();
            databaseThread.execute(() -> {
                    VacationsDatabase.getInstance(this).excursionDao().deleteExcursion(excursion);
            });
            databaseThread.shutdown();
            finish();
        });
    }

    private void save(){
        String title = editTextExcursionTitle.getText().toString().trim();
        String date = textViewDate.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vacationId == -1) {
            Toast.makeText(this, "You can only add excursion after vacation is created.", Toast.LENGTH_SHORT).show();
            return;
        }
        ExecutorService databaseThread = Executors.newSingleThreadExecutor();
        if(!isEditMode) excursion = new ExcursionEntity();

        excursion.setTitle(title);
        excursion.setDate(date);
        excursion.setVacationId(vacationId);
        databaseThread.execute(() -> {
            if(isEditMode) VacationsDatabase.getInstance(this).excursionDao().updateExcursion(excursion);
            if(!isEditMode) VacationsDatabase.getInstance(this).excursionDao().insertExcursion(excursion);
        });

        databaseThread.shutdown();
        finish();
    }
}

