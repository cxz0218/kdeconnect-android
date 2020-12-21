/*
 * SPDX-FileCopyrightText: 2014 Albert Vaca Cintora <albertvaka@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
*/

package org.kde.kdeconnect.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.jaredrummler.android.device.DeviceName;

import org.kde.kdeconnect.Device;

/**
 * Help class for device-specific settings such as screen-size displays provide help in getting device information.
 */
public class DeviceHelper {

    public static final int ProtocolVersion = 7;

    public static final String KEY_DEVICE_NAME_PREFERENCE = "device_name_preference";

    private static boolean fetchingName = false;

    private static boolean isTablet() {
        Configuration config = Resources.getSystem().getConfiguration();
        //This assumes that the values for the screen sizes are consecutive, so XXLARGE > XLARGE > LARGE
        return ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    private static boolean isTv(Context context) {
        int uiMode = context.getResources().getConfiguration().uiMode;
        return (uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    /**
     * Get the device type.
     * @param context
     * @return
     */
    public static Device.DeviceType getDeviceType(Context context) {
        if (isTv(context)) {
            return Device.DeviceType.Tv;
        } else if (isTablet()) {
            return Device.DeviceType.Tablet;
        } else {
            return Device.DeviceType.Phone;
        }
    }

    /**
     * It returns getAndroidDeviceName() if no user-defined name has been set with setDeviceName().
     *
     * @param context
     * @return
     */
    public static String getDeviceName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Could use preferences.contains but would need to check for empty String anyway.
        String deviceName = preferences.getString(KEY_DEVICE_NAME_PREFERENCE, "");
        if (deviceName.isEmpty()) {
            if (!fetchingName) {
                fetchingName = true;
                DeviceHelper.backgroundFetchDeviceName(context); //Starts a background thread that will eventually update the shared pref
            }
            return DeviceName.getDeviceName(); //Temp name while we fetch it from the internet
        }
        return deviceName;
    }

    /**
     * Fetch the device name.
     *
     * @param context
     */
    private static void backgroundFetchDeviceName(final Context context) {
        DeviceName.with(context).request((info, error) -> {
            fetchingName = false;
            if (error != null) {
                Log.e("DeviceHelper", "Error fetching device name");
                error.printStackTrace();
            }
            if (info != null) {
                String deviceName = info.getName();
                Log.i("DeviceHelper", "Got device name: " + deviceName);
                // Update the shared preference. Places that display the name should be listening to this change and update it
                setDeviceName(context, deviceName);
            }
        });
    }

    /**
     * Set the device name.
     *
     * @param context
     * @param name
     */
    public static void setDeviceName(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(KEY_DEVICE_NAME_PREFERENCE, name).apply();
    }

    /**
     * Get the device id.
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
