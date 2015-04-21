package ar.com.post.termproject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ColourNamesAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public ColourNamesAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.item_colour_name, parent, false);
        new ViewHolder(row);
        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.mColourName = new ColourName(cursor.getLong(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4));

        viewHolder.mColourPreview.setBackgroundColor(Color.HSVToColor(new float[]{
                viewHolder.mColourName.mHue, viewHolder.mColourName.mSaturation, viewHolder.mColourName.mValue
        }));
        viewHolder.mColourNameText.setText(viewHolder.mColourName.mName);
    }

    public ColourName getColourName(View view) {
        return ((ViewHolder) view.getTag()).mColourName;
    }

    private class ViewHolder {
        private final View mColourPreview;
        private final TextView mColourNameText;
        private ColourName mColourName;

        private ViewHolder(View container) {
            mColourPreview = container.findViewById(R.id.colour_preview);
            mColourNameText = (TextView) container.findViewById(R.id.colour_name);
            container.setTag(this);
        }
    }
}
