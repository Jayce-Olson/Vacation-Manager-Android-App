package com.example.androidapp.Utilities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.example.androidapp.Entities.VacationEntity;

import java.util.ArrayList;

public class SharingUtils {
    public static void shareDetails(Context context, VacationEntity vacation) {
        String shareText = "Vacation Details:\n\n"
                + "Title: " + vacation.getTitle() + "\n"
                + "Hotel: " + vacation.getHotel() + "\n"
                + "Start Date: " + vacation.getStartDate() + "\n"
                + "End Date: " + vacation.getEndDate() + "\n";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, "Share Vacation Details"));
    }
}
