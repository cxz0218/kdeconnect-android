/*
 * SPDX-FileCopyrightText: 2014 Albert Vaca Cintora <albertvaka@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
*/

package org.kde.kdeconnect.Helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Authentication login help class for the App administrator role.
 */
public class AppsHelper {

    /**
     * Look up the name of app.
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String appNameLookup(Context context, String packageName) {

        try {

            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return pm.getApplicationLabel(ai).toString();

        } catch (final PackageManager.NameNotFoundException e) {

            Log.e("AppsHelper", "Could not resolve name " + packageName, e);

            return null;

        }

    }

    /**
     * Look up the app icon.
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Drawable appIconLookup(Context context, String packageName) {

        try {

            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationIcon(ai);

        } catch (final PackageManager.NameNotFoundException e) {
            Log.e("AppsHelper", "Could not find icon for " + packageName, e);
            return null;
        }
    }
}
