package com.example.androidapp.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import android.widget.Toast;


public class PermissionUtils {
    private ActivityResultLauncher<String> launcher;
    public void registerPermissionLauncher(Activity activity, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Launch the permission request
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                Toast.makeText(context, "Notification permission already granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Permission not required for this Android version", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkAndRequestNotificationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }
}
