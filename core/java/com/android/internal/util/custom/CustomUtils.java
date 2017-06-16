/*
 * Copyright (C) 2017 The Pure Nexus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.custom;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.Locale;

public class CustomUtils {

    // avoids hardcoding the tag
    private static final String TAG = Thread.currentThread().getStackTrace()[1].getClassName();

    public static boolean isChineseLanguage() {
       return Resources.getSystem().getConfiguration().locale.getLanguage().startsWith(
               Locale.CHINESE.getLanguage());
    }

    public static void restartSystemUI(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            IActivityManager amn = ActivityManagerNative.getDefault();
            context.stopService(new Intent().setComponent(new ComponentName("com.android.systemui", "com.android.systemui.SystemUIService")));
            am.killBackgroundProcesses("com.android.systemui");
            for (ActivityManager.RunningAppProcessInfo app : am.getRunningAppProcesses()) {
                if ("com.android.systemui".equals(app.processName)) {
                    amn.killApplicationProcess(app.processName, app.uid);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
