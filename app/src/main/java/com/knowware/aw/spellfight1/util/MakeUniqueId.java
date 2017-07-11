package com.knowware.aw.spellfight1.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

/**
 * Created by Aaron on 4/25/2017.
 */

public class MakeUniqueId
{
    private String gameUUID;
    private String PrefName="spellFightPrefs";//com.knowware.aw.spellfight1.
    private SharedPreferences prefs;
    private String GameUUIDKey="GameUUIDKey";

    public String getGameUUID(Activity parentActivity)
    {
        String stemp;

        prefs =parentActivity.getSharedPreferences( PrefName,Context.MODE_PRIVATE);
        stemp= prefs.getString(GameUUIDKey,"0");

        return(stemp);
    }

    public void setGameUUID(Activity parentActivity, String UUID)
    {
        SharedPreferences.Editor editor;
        boolean bres;

        prefs =parentActivity.getSharedPreferences( PrefName,Context.MODE_PRIVATE);
        editor=prefs.edit();

        editor.putString(GameUUIDKey,UUID);
        bres=editor.commit();
        //editor.apply();
    }

}
