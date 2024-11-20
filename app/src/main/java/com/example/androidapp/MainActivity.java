package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Activities.AddEditVacationActivity;
import com.example.androidapp.Adapters.VacationAdapter;
import com.example.androidapp.Database.VacationsDatabase;
import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
/*
Still learning android SDK and interacting with it's views so i'm just writing this to help me learn:
this ins't neccessarily the initilizer for the app (the initilizer is defined in AndroidManifest.xml),
but instead it is the controller for different "lifecycle events" like onCreate(), onStart(), onResume(),
onPause(), onStop(), and onDestroy(). The AndroidManifiest has this file defined as the initial/main activity.

onCreate() is called when the activity is first created (as the name suggests). The current
code retrieves the savedInstanceState and then sets the contentView to activity_main (or whatever view is passed through).

for this application I will be using Recycler view in order to display multiple views such as main/add/edit.

 */

public class MainActivity extends AppCompatActivity { // Apps than extend AppCompat automatically get one of those bars at the top of the screen
    /*
        Below, RecyclerView is much like a ListView in the sense that it is for displaying a list of data.
        The reason it is called a RecyclerView, and the main reason I am using it is because it is a more
        efficient version of a ListView that reuses views. "reusing views" basically means that instead of creating
        a new view for every single piece of data, views are only created an used for data that can be displayed and
        as the data is scrolled, the scrolled away views are replaced with the new views. In this case there is not
        a lot of data so this is not necessarily needed. However in situations where there is a lot of data that needs
        to be displayed in a list, this view is basically mandatory as it saves memory from having to store all of the
        data at once.

        This view is responsible for displaying the list of vacations, detecting events on the list items, and
        managing the "recycling" of views as the user scrolls through the list. Since this is a school assignment
        there likely will not be enough data for recycling views to be needed, however this is still good practice,
        which is why I went with recyclerView instead ofl ListView.

        Inside of activity_main.xml you will see a reference to this (within onCreate
        recyclerView = findViewById(R.id.recyclerViewVacations); binds the view to the id within the
        activity_main.xml file
     */
    private RecyclerView recyclerView;
    /*
    * Below VacationAdapter is declared. I have a description of that class within the VacationAdapter.java file.
    * In short it is the middle man between the recyclerView and the Database.
    * */
    private VacationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Retrieves the current saved instance state (if any)
        setContentView(R.layout.activity_main); // Sets the current content view to the activity_main.xml file (without setting a view the app would just be blank)

        recyclerView = findViewById(R.id.recyclerViewVacations); // Binds recycler view to the id for it in the activity_main.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Sets the type of layout for the recycler view to a linear layout
        /*
        * Below VacationAdapter is initilized. I have a description of that class within the VacationAdapter.java file.
        * In short it is the middle man between the recyclerView and the Database. Directly after initilization the
        * recyclerView has it's adapter set to adapter.
        *
        * Note: Using recyclerView may have caused a little bit of overcomplexity for a school assginment. There
        * will be more methods related to recyclerViews effiecient rendering/updating in VacationAdapter.java and
        * VacationEntity.java.
        * */
        adapter = new VacationAdapter();
        recyclerView.setAdapter(adapter);


        /*
         * The code below is highly specific, it involves:
         * FloatingActionButton: This is an object for a floating action button. below it is being
         * assigned to the FloatingActionButton within the activity_main.xml file.
         *
         * .setOnClickListener: is a method for FloatingActionButtons that runs the lambda function on
         * click
         *
         * Intent: is a message object that is used to start a new activity, send data between activities, and
         * trigger "system components" such as notifications - which will be used later to set up notifications
         * for excursion dates.
         * The way Intent is initilized below (its parameters), pass through the context, and then the target
         * activity to opened, which is the AddEditVacationActivity.class activity. This doesn't open the target
         * activity right away though, instead startAcitivity(object w/ target activity) must be called first,
         * as it is below.
         */
        FloatingActionButton AddVacationButton = findViewById(R.id.fabAddVacation);
        AddVacationButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditVacationActivity.class);
            // There will be more info on the flow of this logic inside addEditVacationActivity.java as after
            // this is called, the onCreate() method inside AddEditVacationActivity.java will be called.
            startActivity(intent);
        });

        loadVacations(); // Calls the method below to load the vacations from the database
    }

    private void loadVacations() {
        /* Method info:
            The method below fetches vacations from the database and submits them to adapter.
            submit is not a method inside VacationAdapter.java but a method within
            RecyclerView.Adapter, which VacationAdapter extends. */

        /*
            The line below, .getInstance() is a singleton method within the VacationsDatabase class,
             which basically means that the database instance within it is static and there is only one instance
             across the app and .getInstance is a single point of access to it. it takes the parameter "context" in
             case an instance doesn't exist, because in order to create an instance, context is required (this will
             allow the room databaseBuilder to sync with the apps lifecycle and know where to store the database file).

            I have a bit more info on this within the VacationsDatabase.java file.
         */
        VacationsDatabase db = VacationsDatabase.getInstance(this); // Retrieves instance of database
         // List<VacationEntity> vacations = db.vacationDao().getAllVacations(); // Before multithreading
        db.vacationDao().getAllVacations().observe(this, vacations -> { // stores Vacation Entities in a list (vacations). Using observe because of multithreading
            /*
             * Submitting the list to adapter basically involves running "vacations" through "diff calculations"
             * which calculate the difference between the passed through vacations, and the vacations currently.
             * The point of this is to make updating the views more efficient. Mainly because if you had hundereds
             * of views and only added one view, it would be highly inefficient to update all of the views with the
             * same data. Instead, this just finds the changed views and updates those. This will also automatically
             * call notifyDataSetChanged(). Which will refresh the list on the UI.
             * */
            adapter.submitList(vacations);
        });

    }
}