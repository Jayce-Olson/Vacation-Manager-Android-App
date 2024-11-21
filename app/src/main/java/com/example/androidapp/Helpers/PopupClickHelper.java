package com.example.androidapp.Helpers;

import com.example.androidapp.Utilities.PermissionUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.androidapp.Entities.VacationEntity;
import com.example.androidapp.R;
import com.example.androidapp.Utilities.NotificationUtils;
import com.example.androidapp.Utilities.SharingUtils;

public class PopupClickHelper {
    public static void popupListener(Context context, ImageView menuButton, VacationEntity vacation, Activity activity){
        // Popup menu button
        menuButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.menu_set_notification) {
                    // Below calls the setNotification static method within the NotificationUtils class.
                    NotificationUtils.setNotification(context, vacation.getTitle(), vacation.getStartDate());
                    // instantiate PermissionsUtils
                    PermissionUtils permissionUtils = new PermissionUtils();
                    // Register the permission launcher
                    permissionUtils.registerPermissionLauncher(activity, context);

                    // Check and request POST_NOTIFICATIONS permission if necessary
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionUtils.checkAndRequestNotificationPermission(context);
                    }
                    return true; // onMenuItemClick expects a return value so this must return a value
                }
                // case R.id.menu_share:
                SharingUtils.shareDetails(context,vacation);
                return true; // onMenuItemClick expects a return value so this must return a value
            });

            popupMenu.show();
        });
    }
}
