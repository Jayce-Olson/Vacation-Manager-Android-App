package com.example.androidapp.Helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.Utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateClickHelper {
    private Date startDate = null, endDate = null;
    private Date excursionDate = null;
    public void dateListener(Context context, Button buttonPickStartDate, Button buttonPickEndDate, TextView textViewStartDate, TextView textViewEndDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Enforces format

        // Pick start date button with validation logic
        buttonPickStartDate.setOnClickListener(view -> DateUtils.showDatePicker(context, date -> {
            try{
                startDate = dateFormat.parse(date);
                if(endDate==null){
                    textViewStartDate.setText(date);
                    return;
                }
                if(startDate.before(endDate)) {
                    textViewStartDate.setText(date);
                }
                Toast.makeText(context, "Invalid date", Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                Log.e("DateParsingError", "Error parsing start date: " + date, e);
            }
        }));

        // Pick end date button with validation logic
        buttonPickEndDate.setOnClickListener(v -> DateUtils.showDatePicker(context, date -> {
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
                Toast.makeText(context, "Invalid input date", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("DateParsingError", "Error parsing start date: " + date, e);

            }
        }));
    }

    public void dateExcursionListener(Context context, Button buttonPickDate, TextView textViewDate, String sDate, String eDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Enforces format

        buttonPickDate.setOnClickListener(view -> DateUtils.showDatePicker(context, date -> {

            try {
                excursionDate = dateFormat.parse(date); // this is just for .after/.before methods
                startDate = dateFormat.parse(sDate);
                endDate = dateFormat.parse(eDate);
                if((startDate.before(excursionDate) && endDate.after(excursionDate)) || startDate.equals(excursionDate) || endDate.equals(excursionDate)){
                    textViewDate.setText(date);
                    return;
                }
                Toast.makeText(context, "Invalid input date", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("DateParsingError", "Error parsing start date: " + date, e);

            }
        }));
    }
}
