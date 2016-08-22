package com.squalala.tariki.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
public class Session {

    private SharedPreferences preferences;
    private Context context;

    public Session(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.context = context;
    }
}
