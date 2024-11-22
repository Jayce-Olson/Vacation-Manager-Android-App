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

    private static Boolean checkPermissions(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Do not proceed without permission
                PermissionUtils.requestExactAlarmPermission(context);
                Toast.makeText(context, "Notification permission is required", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public static void setNotification(Context context, String title, String date) {
        if(checkPermissions(context)) return;
        try {
            // Parse the startDate and endDate strings into a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date notificationDate = dateFormat.parse(date);

            if (notificationDate == null)  throw new IllegalArgumentException("Invalid date format");

            // Set up AlarmManager
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Intent for message - start date
            Intent startIntent = new Intent(context, NotificationReceiver.class);
            startIntent.putExtra("title", title);
            startIntent.putExtra("message", "Your vacation has started!");
            // Intent for message - end date


            // Start alarm pending intent
            PendingIntent startPendingIntent = PendingIntent.getBroadcast(
                    context,
                    title.hashCode(), // Unique ID for this alarm
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            // End alarm pending intent

                // Schedule the alarms
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationDate.getTime(),
                    startPendingIntent
            );



            Toast.makeText(context, "Notification set for " + title + " on " + notificationDate, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to set notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Notification","Failed to set notification: " + e.getMessage(), e);
        }
    }
}
