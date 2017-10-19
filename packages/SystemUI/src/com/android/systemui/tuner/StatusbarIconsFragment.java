/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.hardware.AmbientDisplayConfiguration;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.systemui.R;
import com.android.systemui.plugins.PluginPrefs;

public class StatusbarIconsFragment extends TunerFragment {

    private static final String TAG = "StatusbarIconsFragment";

    private static final String STATUS_BAR_LOGO = "status_bar_logo";

    private SwitchPreference mCardinalLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ContentResolver resolver = getActivity().getContentResolver();

        mCardinalLogo = (SwitchPreference) findPreference(STATUS_BAR_LOGO);
        mCardinalLogo.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_LOGO, 0) == 1));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.statusbar_icon_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.statusbar_icons_blacklist);

        MetricsLogger.visibility(getContext(), MetricsEvent.TUNER, true);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if  (preference == mCardinalLogo) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.STATUS_BAR_LOGO, checked ? 1:0);
            return true;
          }
        return super.onPreferenceTreeClick(preference);
    }
}
