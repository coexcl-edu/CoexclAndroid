package com.home.coexcleducation.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.home.coexcleducation.utils.CoexclLogs;

public class DatabaseHelper extends SQLiteOpenHelper {

    String TAG = getClass().getName();
    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "epathsala_mobile_database";
    private static DatabaseHelper mDataBaseHelperInstance = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        setWriteAheadLoggingEnabled(true);
    }

    public static synchronized DatabaseHelper getInstance(Context pContext){
        if(mDataBaseHelperInstance == null) {
            mDataBaseHelperInstance = new DatabaseHelper(pContext.getApplicationContext());
        }
        return mDataBaseHelperInstance;
    }


    @Override
    protected void finalize() throws Throwable {
        mDataBaseHelperInstance.close();
        super.finalize();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        CoexclLogs.verboseLog(getClass().getName(), "CREATING TABLES");
        try {
            NotificationTable.onCreate(db);

        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CoexclLogs.verboseLog(getClass().getName(), "UPDATING SETMORE DATABASE");
        NotificationTable.onUpgrade(db, oldVersion, newVersion);

    }

    public static void setDatabaseVersion(int version){
        DATABASE_VERSION = version;
    }
}

