package ar.com.post.termproject;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ar.com.post.termproject.db.ColoursTable;


public class ColourNamesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_names);
        Uri uri = getIntent().getData();
        ListView listView = (ListView) findViewById(R.id.listView);
        TextView empty = (TextView) findViewById(R.id.empty);

        Cursor cursor = getContentResolver().query(uri, ColoursTable.COLUMNS, null, null, null);
        int count = (cursor==null)?0:cursor.getCount();

        if (count == 0 || cursor.getColumnCount() != 5) {
            listView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            cursor.moveToFirst();
            ColourName[] colourNames = new ColourName[count];
            for (int i = 0; i < count; i++) {
                colourNames[i] = new ColourName(cursor.getLong(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4));
                cursor.moveToNext();
            }
            listView.setAdapter(new ColourNamesAdapter(getApplicationContext(), colourNames));
            /* TODO: action on colour click (custom toast) */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ColourNamesAdapter adapter = (ColourNamesAdapter) parent.getAdapter();
                    /* TODO: toast comes here */
                }
            });

        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
