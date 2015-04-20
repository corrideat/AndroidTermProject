package ar.com.post.termproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ColourHelper extends SQLiteOpenHelper {
    static private final String DB_NAME = "colours.db";
    static private final int DB_VERSION = 1;

    static private Context mContext;

    public ColourHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ColoursTable.onCreate(mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ColoursTable.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
