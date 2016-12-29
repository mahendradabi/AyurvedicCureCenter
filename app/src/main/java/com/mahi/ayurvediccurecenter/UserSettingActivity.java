package com.mahi.ayurvediccurecenter;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by HP on 12/26/2016.
 */

public class UserSettingActivity extends PreferenceActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // add the xml resource

        addPreferencesFromResource(R.xml.user_settings);


    }

}