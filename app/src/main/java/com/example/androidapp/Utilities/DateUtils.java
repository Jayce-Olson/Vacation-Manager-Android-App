package com.example.androidapp.Utilities;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.function.Consumer;

public class DateUtils {
    public static void showDatePicker(Context context, Consumer<String> callback) { // I am using consumer string to call lambda rather than having it implement an interface method
        // This shows the date picker menu overlay
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month + 1) + "-" + dayOfMonth;
            /* The code in .onDatePicked() will be the lambda/callback, date will be passed to it */
            callback.accept(date);
        }, 2024, 0, 1);
        datePickerDialog.show();
    }
}
