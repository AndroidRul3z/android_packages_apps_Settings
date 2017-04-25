/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
*/
package com.android.settings.purity;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.media.AudioSystem;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.preference.SystemCheckBoxPreference;
import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.internal.util.omni.PackageUtils;
import com.android.internal.util.omni.DeviceUtils;

public class ButtonSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener, Indexable {

    private static final String CATEGORY_KEYS = "button_keys";
    private static final String KEYS_BRIGHTNESS_KEY = "button_brightness";
    private static final String KEYS_SHOW_NAVBAR_KEY = "navigation_bar_show";
    private static final String KEYS_DISABLE_HW_KEY = "hardware_keys_disable";

    private boolean mButtonBrightnessSupport;
    private PreferenceScreen mButtonBrightness;
    private PreferenceCategory mKeysBackCategory;
    private SwitchPreference mEnableNavBar;
    private SwitchPreference mDisabkeHWKeys;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_settings);

        final ContentResolver resolver = getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        mButtonBrightnessSupport = res.getBoolean(com.android.internal.R.bool.config_button_brightness_support);

        final int deviceKeys = res.getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);
        final PreferenceCategory keysCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_KEYS);

        if (deviceKeys == 0) {
            prefScreen.removePreference(keysCategory);
        } else {
        mButtonBrightness = (PreferenceScreen) prefScreen.findPreference(
            KEYS_BRIGHTNESS_KEY);
        if (!mButtonBrightnessSupport) {
            keysCategory.removePreference(mButtonBrightness);
        }

        mEnableNavBar = (SwitchPreference) prefScreen.findPreference(
                KEYS_SHOW_NAVBAR_KEY);

        mDisabkeHWKeys = (SwitchPreference) prefScreen.findPreference(
                KEYS_DISABLE_HW_KEY);

        boolean showNavBarDefault = DeviceUtils.deviceSupportNavigationBar(getActivity());
        boolean showNavBar = Settings.System.getInt(resolver,
                    Settings.System.NAVIGATION_BAR_SHOW, showNavBarDefault ? 1:0) == 1;
        mEnableNavBar.setChecked(showNavBar);

        boolean harwareKeysDisable = Settings.System.getInt(resolver,
                    Settings.System.HARDWARE_KEYS_DISABLE, 0) == 1;
        mDisabkeHWKeys.setChecked(harwareKeysDisable);
        }

        boolean showNavBarDefault = DeviceUtils.deviceSupportNavigationBar(getActivity());
        boolean showNavBar = Settings.System.getInt(resolver,
                Settings.System.NAVIGATION_BAR_SHOW, showNavBarDefault ? 1:0) == 1;
        mEnableNavBar.setChecked(showNavBar);
    }


    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
      if (preference == mEnableNavBar) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_SHOW, checked ? 1:0);
            // remove hw button disable if we disable navbar
            if (!checked) {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.HARDWARE_KEYS_DISABLE, 0);
                mDisabkeHWKeys.setChecked(false);
            }
            return true;
        } else if (preference == mDisabkeHWKeys) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HARDWARE_KEYS_DISABLE, checked ? 1:0);
            //updateDisableHWKeyEnablement(checked);
            return true;
         }
        return super.onPreferenceTreeClick(preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

}
