package ar.com.post.termproject.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

public class ColourContentProvider extends ContentProvider {
    static public final String AUTHORITY = "ar.com.post.termproject.colourcontentprovider";
    static public final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/colours";
    static public final String CONTENT_TYPE_COLOUR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/colour";
    static public final String BASE_PATH = "colour";
    static private final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static private final int COLOUR_ID = 1;
    static private final int COLOUR_NAME = 2;
    static private final int COLOUR_PARAMS = 3;

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COLOUR_ID);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/name/*", COLOUR_NAME);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/query", COLOUR_PARAMS);
    }

    private SQLiteOpenHelper db;
    private HashSet<String> mAvailable = new HashSet<>(Arrays.asList(ColoursTable.COLUMNS));

    @Override
    public boolean onCreate() {
        db = new ColourHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(ColoursTable.TABLE);
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case COLOUR_ID:
                queryBuilder.appendWhere(ColoursTable.COLUMN_ID + "=");
                queryBuilder.appendWhereEscapeString(uri.getLastPathSegment());
                break;
            case COLOUR_NAME:
                queryBuilder.appendWhere(ColoursTable.COLUMN_NAME + " LIKE ");
                queryBuilder.appendWhereEscapeString("%" + uri.getLastPathSegment() + "%");
                break;
            case COLOUR_PARAMS:
                String sLowerHue = uri.getQueryParameter("lower_hue");
                String sUpperHue = uri.getQueryParameter("upper_hue");
                String sLowerSaturation = uri.getQueryParameter("lower_saturation");
                String sUpperSaturation = uri.getQueryParameter("upper_saturation");
                String sLowerValue = uri.getQueryParameter("lower_value");
                String sUpperValue = uri.getQueryParameter("upper_value");
                boolean strictInequalityLowerHue = (sLowerHue != null) && uri.getBooleanQueryParameter("lower_hue_strict", false);
                boolean strictInequalityUpperHue = (sUpperHue != null) && uri.getBooleanQueryParameter("upper_hue_strict", false);
                boolean strictInequalityLowerSaturation = (sLowerSaturation != null) && uri.getBooleanQueryParameter("lower_saturation_strict", false);
                boolean strictInequalityUpperSaturation = (sUpperSaturation != null) && uri.getBooleanQueryParameter("upper_saturation_strict", false);
                boolean strictInequalityLowerValue = (sLowerValue != null) && uri.getBooleanQueryParameter("lower_value_strict", false);
                boolean strictInequalityUpperValue = (sUpperValue != null) && uri.getBooleanQueryParameter("upper_value_strict", false);
                float lowerHue = ((sLowerHue == null) ? 0.0f : Float.valueOf(sLowerHue)) % 360.0f;
                float upperHue = ((sUpperHue == null) ? 360.0f : Float.valueOf(sUpperHue)) % 360.0f;
                float lowerSaturation = (sLowerSaturation == null) ? 0.0f : Float.valueOf(sLowerSaturation);
                float upperSaturation = (sUpperSaturation == null) ? 1.0f : Float.valueOf(sUpperSaturation);
                float lowerValue = (sLowerValue == null) ? 0.0f : Float.valueOf(sLowerValue);
                float upperValue = (sUpperValue == null) ? 1.0f : Float.valueOf(sUpperValue);
                if (lowerHue < 0.0f) {
                    lowerHue += 360.0f;
                }
                if (upperHue < 0.0f) {
                    upperHue += 360.0f;
                }
                queryBuilder.appendWhere("(");
                queryBuilder.appendWhere(ColoursTable.COLUMN_HUE + (strictInequalityLowerHue ? ">" : ">=") + lowerHue);
                queryBuilder.appendWhere(lowerHue > upperHue ? " OR " : " AND ");
                queryBuilder.appendWhere(ColoursTable.COLUMN_HUE + (strictInequalityUpperHue ? "<" : "<=") + upperHue);
                queryBuilder.appendWhere(") AND (");
                queryBuilder.appendWhere(ColoursTable.COLUMN_SATURATION + (strictInequalityLowerSaturation ? ">" : ">=") + lowerSaturation);
                queryBuilder.appendWhere(lowerSaturation > upperSaturation ? " OR " : " AND ");
                queryBuilder.appendWhere(ColoursTable.COLUMN_SATURATION + (strictInequalityUpperSaturation ? "<" : "<=") + upperSaturation);
                queryBuilder.appendWhere(") AND (");
                queryBuilder.appendWhere(ColoursTable.COLUMN_VALUE + (strictInequalityLowerValue ? ">" : ">=") + lowerValue);
                queryBuilder.appendWhere(lowerValue > upperValue ? " OR " : " AND ");
                queryBuilder.appendWhere(ColoursTable.COLUMN_VALUE + (strictInequalityUpperValue ? "<" : "<=") + upperValue);
                queryBuilder.appendWhere(")");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase rdb = db.getReadableDatabase();
        return queryBuilder.query(rdb, projection, selection, selectionArgs, null, null, sortOrder);
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> requested = new HashSet<>(Arrays.asList(projection));
            if (!mAvailable.containsAll(requested)) {
                throw new IllegalArgumentException("Invalid projection");
            }
        }
    }
}
