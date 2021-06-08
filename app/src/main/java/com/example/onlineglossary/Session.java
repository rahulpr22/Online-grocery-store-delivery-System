package com.example.onlineglossary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
    public void setuname(String uname){prefs.edit().putString("uname",uname).commit();}
    public String getuname() {
        String uname = prefs.getString("uname","");
        return uname;
    }
}