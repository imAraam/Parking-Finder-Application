package com.example.freeparkingfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

class PrefManagerHistory
{
    private SharedPreferences spHistory;
    private SharedPreferences.Editor editorHistory;


    private static final String PREF_NAME = "history";

    PrefManagerHistory(Context context)
    {
        spHistory = context.getSharedPreferences(PREF_NAME, 0);
        editorHistory = spHistory.edit();
    }

    void setHistory(String name, String position)
    {
        editorHistory.putString(name, position);
        editorHistory.commit();
    }

    Map<String, ?> getHistory()
    {
        return spHistory.getAll();
    }

    void clearAll()
    {
        editorHistory.clear();
        editorHistory.commit();
    }

}
