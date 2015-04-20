package ar.com.post.termproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;

import ar.com.post.termproject.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ColoursTable {
    static public final String TABLE = "colours";
    static public final String COLUMN_ID = "_ID";
    static public final String COLUMN_NAME = "name";
    static public final String COLUMN_HUE = "hue";
    static public final String COLUMN_SATURATION = "saturation";
    static public final String COLUMN_VALUE = "value";
    static public final String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_HUE, COLUMN_VALUE, COLUMN_SATURATION, COLUMN_VALUE};

    static private final String SQL_CREATE = "CREATE TABLE "
            + TABLE + " ("
            + COLUMN_ID + " INTEGER PRIMARY NOT NULL,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_HUE + " REAL NOT NULL,"
            + COLUMN_SATURATION + " REAL NOT NULL,"
            + COLUMN_VALUE + " REAL NOT NULL"
            + ");";

    static public void onCreate(Context context, SQLiteDatabase db) {
        String line;
        db.execSQL(SQL_CREATE);

        XmlResourceParser colours = context.getResources().getXml(R.xml.colours);
        try {
            int eventType = colours.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if (colours.getName().equals("colour")) {
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_NAME, colours.getAttributeValue(null, "name"));
                        values.put(COLUMN_HUE, colours.getAttributeValue(null, "hue"));
                        values.put(COLUMN_SATURATION, colours.getAttributeValue(null, "saturation"));
                        values.put(COLUMN_VALUE, colours.getAttributeValue(null, "value"));
                        db.insert(TABLE, null, values);
                    }
                }
                eventType = colours.next();
            }
            colours.close();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE + ";");
        onCreate(context, db);
    }
}
