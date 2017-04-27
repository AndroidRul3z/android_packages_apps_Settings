/*
 * Copyright (C) 2016 Benzo Rom
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

package com.android.settings.purity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.net.Uri;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.List;

public class PuritySettings extends SettingsPreferenceFragment {

    private static final String KEY_OMS_APP = "oms_app";

    private Preference mOmsApp;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.purity_settings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        mOmsApp = (Preference) findPreference(KEY_OMS_APP);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mOmsApp) {
            Intent omsIntent = getPackageManager().getLaunchIntentForPackage("projekt.substratum");
            if (omsIntent != null) {
                startActivity(omsIntent);
            } else {
                downloadOmsApp();
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void downloadOmsApp(){
        Intent omsDownloadIntent = new Intent(Intent.ACTION_VIEW);
        omsDownloadIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=projekt.substratum"));
        startActivity(omsDownloadIntent);
    }
}
