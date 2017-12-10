/*
 * Copyright (C) 2017 The halogenOS Project
                 2017 Cardinal-AOSP
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

package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import com.android.systemui.R;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;

/** Quick settings tile: NotificationsTile **/
public class NotificationsTile extends QSTileImpl<BooleanState> {

    public NotificationsTile(QSHost host) {
        super(host);
    }

    private String mNotif = "";


    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    public void handleClick() {
        switch(mNotif){
            case "":
                mNotif = Settings.System.STATUS_BAR_SHOW_TICKER;
                break;
            case Settings.System.STATUS_BAR_SHOW_TICKER:
                mNotif = Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED;
                break;
            case Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED:
                mNotif = "";
                break;
        }
        refreshState();
    }


    @Override
    public Intent getLongClickIntent() {
        return new Intent().setComponent(new ComponentName(
            "com.android.settings", "com.android.settings.Settings$NotificationSettingsActivity"));
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_notifications_label);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.QUICK_SETTINGS;
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        switch (mNotif){
            case "":
                state.label = mContext.getString(R.string.quick_settings_notifications_none_label);
                state.icon = ResourceIcon.get(R.drawable.ic_qs_notifications_off);
                setValues(false, false);
                break;
            case Settings.System.STATUS_BAR_SHOW_TICKER:
                state.label = mContext.getString(R.string.quick_settings_notifications_ticker_label);
                state.icon = ResourceIcon.get(R.drawable.ic_qs_notifications_ticker);
                setValues(true, false);
                break;
            case Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED:
                state.label = mContext.getString(R.string.quick_settings_notifications_headsup_label);
                state.icon = ResourceIcon.get(R.drawable.ic_qs_heads_up_on);
                setValues(false, true);
                break;
        }
    }

    private void setValues(boolean mTickerEnable, boolean mHeadsupEnable) {
        Settings.Global.putInt(mContext.getContentResolver(),
            Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, mHeadsupEnable ? 1 : 0);
        Settings.System.putInt(mContext.getContentResolver(),
            Settings.System.STATUS_BAR_SHOW_TICKER, mTickerEnable ? 1 : 0);
    }

    private String getCurrentNotifier() {
        if (Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1) != 0) {
            return Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED;
        } else if (Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.STATUS_BAR_SHOW_TICKER, 0) != 0) {
            return Settings.System.STATUS_BAR_SHOW_TICKER;
        } else {
            return "";
        }
    }

    @Override
    public void handleSetListening(boolean listening) {
    }
}
