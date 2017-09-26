package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.service.quicksettings.Tile;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.systemui.qs.QSHost;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.R;

/** Quick settings tile: ANBI **/
public class ANBITile extends QSTileImpl<BooleanState> {

    public ANBITile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    public void handleClick() {
        setEnabled(!mState.value);
        refreshState();
    }

    @Override
    public Intent getLongClickIntent() {
        return new Intent().setComponent(new ComponentName(
            "com.android.settings", "com.android.settings.Settings$NavigationBarSettingsActivity"));
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_anbi_label);
    }
    
    private void setEnabled(boolean enabled) {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.ANBI_ENABLED,
                enabled ? 1 : 0);
    }

   private boolean getAnbiState() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.ANBI_ENABLED, 0) == 1;
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.value = getAnbiState();
        if (state.value) {
            state.icon = ResourceIcon.get(R.drawable.ic_qs_anbi_on);
            state.label =  mContext.getString(
                    R.string.accessibility_quick_settings_anbi_on);
        } else {
            state.icon = ResourceIcon.get(R.drawable.ic_qs_anbi_off);
            state.label =  mContext.getString(
                    R.string.accessibility_quick_settings_anbi_off);
        }
    }

    @Override
    protected String composeChangeAnnouncement() {
        if (mState.value) {
            return mContext.getString(
                    R.string.accessibility_quick_settings_anbi_changed_on);
        } else {
            return mContext.getString(
                    R.string.accessibility_quick_settings_anbi_changed_off);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.QUICK_SETTINGS;
    }

    @Override
    public void setListening(boolean listening) {
        // Do nothing
    }
}
