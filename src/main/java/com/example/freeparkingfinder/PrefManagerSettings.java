package com.example.freeparkingfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

class PrefManagerSettings
{
    private SharedPreferences spSettings;
    private SharedPreferences.Editor editorSettings;


    private static final String PREF_NAME = "settings";

    PrefManagerSettings(Context context)
    {
        spSettings= context.getSharedPreferences(PREF_NAME, 0);
        editorSettings = spSettings.edit();
    }

    void setRadius(String name, int radius)
    {
        editorSettings.putInt(name, radius);
        editorSettings.commit();
    }

    int getRadius()
    {
        return spSettings.getInt("radius", -1);
    }

    void setZoom(String name, int zoom)
    {
        editorSettings.putInt(name, zoom);
        editorSettings.commit();
    }

    int getZoom()
    {
        return spSettings.getInt("zoom", -1);
    }

}
