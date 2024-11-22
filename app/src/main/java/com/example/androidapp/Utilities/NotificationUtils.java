package com.example.androidapp.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.androidapp.BroadcastReceivers.NotificationReceiver;
import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationUtils {

    public static void setNotification(Context context, String title, String startDate) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Do not proceed without permission
                PermissionUtils.requestExactAlarmPermission(context);
                Toast.makeText(context, "Notification permission is required", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try {
            // Parse the startDate string into a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(startDate);
            if (date == null)  throw new IllegalArgumentException("Invalid date format");



            // Set up AlarmManager
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Create an intent to broadcast
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", "Your vacation has started!");

            // Create a PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    title.hashCode(), // Unique ID for this alarm
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Schedule the alarm
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    date.getTime(),
                    pendingIntent
            );

            Toast.makeText(context, "Notification set for " + title + " on " + startDate, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to set notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Notification","Failed to set notification: " + e.getMessage(), e);
        }
    }
}
