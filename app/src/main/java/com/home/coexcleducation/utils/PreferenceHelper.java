package com.home.coexcleducation.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Set;

public class PreferenceHelper {

    Context mContext;
    public static final String NOTIFICATION_COUNT                = "notificationCount";

    public PreferenceHelper(Context pContext) {
        this.mContext = pContext;
    }

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("com.coexcl.education", 0);
    }


    public boolean persist(HashMap<String, Object> pPreferecnceObj) {


        SharedPreferences.Editor lEditor =  getSharedPreference(mContext).edit();
        for(String lKey : pPreferecnceObj.keySet()) {
            Object lValue = pPreferecnceObj.get(lKey);
            if(lValue instanceof String)
                lEditor.putString(lKey, (String) lValue);
            else if(lValue instanceof Integer)
                lEditor.putInt(lKey, (Integer) lValue);
            else if(lValue instanceof Long)
                lEditor.putLong(lKey, (Long) lValue);
            else if(lValue instanceof Float)
                lEditor.putFloat(lKey, (Float) lValue);
            else if(lValue instanceof Boolean)
                lEditor.putBoolean(lKey, (Boolean) lValue);
            else if(lValue instanceof Set<?>)
                lEditor.putStringSet(lKey, (Set<String>) lValue);
        }
        lEditor.apply();
        return true;
    }


    public void persist(String pKey, Object pValue) {

        SharedPreferences.Editor lEditor =  getSharedPreference(mContext).edit();

        if(pValue instanceof String)
            lEditor.putString(pKey, (String) pValue);
        else if(pValue instanceof Integer)
            lEditor.putInt(pKey, (Integer) pValue);
        else if(pValue instanceof Long)
            lEditor.putLong(pKey, (Long) pValue);
        else if(pValue instanceof Float)
            lEditor.putFloat(pKey, (Float) pValue);
        else if(pValue instanceof Boolean)
            lEditor.putBoolean(pKey, (Boolean) pValue);
        else if(pValue instanceof Set<?>)
            lEditor.putStringSet(pKey, (Set<String>) pValue);

        lEditor.apply();
    }


    public void clearAllPreferenceData() {
        SharedPreferences.Editor lEditor =  getSharedPreference(mContext).edit();
        lEditor.clear().apply();
    }
}
