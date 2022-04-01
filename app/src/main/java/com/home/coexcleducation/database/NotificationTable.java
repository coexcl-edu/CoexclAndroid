package com.home.coexcleducation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.home.coexcleducation.jdo.NotificationJDO;
import com.home.coexcleducation.utils.CoexclLogs;

import java.util.ArrayList;
import java.util.List;

public class NotificationTable implements QueryHelper {

    public static final String TAG_NAME			        = NotificationTable.class.getName();
    public static final String NOTIFICATION_TABLE       = "notificationTable";
    private DatabaseHelper lDatabase;

    public static final String ID                          = "notification_id";
    public static final String TITLE_NAME                  = "title_name";
    public static final String NOTIFICATION_MESSAGE        = "notification_msg";
    public static final String IMAGE_URL				   = "course_thumbnail";
    public static final String TIME_STAMP		           = "time_stamp";


    private static String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + NOTIFICATION_TABLE + "("
            + ID + " TEXT PRIMARY KEY,"               + TITLE_NAME + " TEXT ,"   + NOTIFICATION_MESSAGE + " TEXT ,"
            + IMAGE_URL + " TEXT, " +TIME_STAMP+ " TEXT)";

    public NotificationTable(Context context) {
        lDatabase = DatabaseHelper.getInstance(context);
    }

    public static void onCreate(SQLiteDatabase pDb) {
        CoexclLogs.infoLog(NotificationTable.class.getName(), "Inside onCreate");
        try {
            pDb.execSQL(CREATE_NOTIFICATION_TABLE);
            CoexclLogs.infoLog(NotificationTable.class.getName(), "CREATE_NOTIFICATION_TABLE - "+ CREATE_NOTIFICATION_TABLE);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public static void onUpgrade(SQLiteDatabase pDb, int pOldVersion, int pNewVersion) {
        try {
            pDb.execSQL("DROP TABLE IF EXISTS "+ NOTIFICATION_TABLE);
            onCreate(pDb);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }


    @Override
    public void insertAll(Object listObj) {

    }

    @Override
    public void insert(Object pObj) {
        CoexclLogs.infoLog(TAG_NAME, "Inside insertService....");
        SQLiteDatabase db            = lDatabase.getWritableDatabase();
        NotificationJDO lNotification    = (NotificationJDO) pObj;
        try {
            ContentValues values = new ContentValues();
            values.put(ID, lNotification.getId());
            values.put(TITLE_NAME, lNotification.getTitle());
            values.put(NOTIFICATION_MESSAGE, lNotification.getMessage());
            values.put(IMAGE_URL, lNotification.getImage());
            values.put(TIME_STAMP, lNotification.getTimeStamp());
            Long lInsertedSuccessfully = db.insertWithOnConflict(NOTIFICATION_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            CoexclLogs.infoLog(getClass().getName(), "Insert notification - "+lInsertedSuccessfully);

        } catch (Exception e) {
            CoexclLogs.printException(e);
        } finally {
        }
    }

    @Override
    public void deleteAllRecord() {
        SQLiteDatabase db 							    = lDatabase.getWritableDatabase();
        try {
            db.delete(NOTIFICATION_TABLE, null, null);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        } finally {

        }
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getAll() {
        SQLiteDatabase db 							    = lDatabase.getWritableDatabase();
        List<NotificationJDO> lNotifcationList    = new ArrayList<NotificationJDO>();
        try {
            String sql = "SELECT "+ ID + ","+ TITLE_NAME + ","+ NOTIFICATION_MESSAGE + "," + IMAGE_URL +"," + TIME_STAMP +" FROM "+ NOTIFICATION_TABLE;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.moveToFirst()) {
                do {
                    NotificationJDO lNotification  = new NotificationJDO();
                    lNotification.setId(cursor.getString(0));
                    lNotification.setTitle(cursor.getString(1));
                    lNotification.setMessage(cursor.getString(2));
                    lNotification.setImage(cursor.getString(3));
                    lNotification.setTimeStamp(cursor.getLong(4));
                    lNotifcationList.add(lNotification);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            CoexclLogs.printException(e);
        } finally {
        }
        return lNotifcationList;
    }

    @Override
    public Object getById(String id) {
        return null;
    }
}
