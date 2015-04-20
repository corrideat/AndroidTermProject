package ar.com.post.termproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ColourNamesAdapter extends ArrayAdapter<ColourName> {
    private final ColourName[] mColourNames;

    public ColourNamesAdapter(Context context, ColourName[] colourNames) {
        super(context, R.layout.gradient_item, colourNames);
        mColourNames = colourNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.colour_name_item, parent, false);
            new ViewContainer(convertView);
        }

        ViewContainer viewContainer = (ViewContainer)convertView.getTag();

        viewContainer.mColourPreview.setBackgroundColor(Color.HSVToColor(new float[] {mColourNames[position].mHue, mColourNames[position].mSaturation, mColourNames[position].mValue}));
        viewContainer.mColourName.setText(mColourNames[position].mName);

        return convertView;
    }

    private class ViewContainer {
        private final TextView mColourPreview;
        private final TextView mColourName;

        private ViewContainer(View container) {
            mColourPreview = (TextView)container.findViewById(R.id.colour_preview);
            mColourName = (TextView)container.findViewById(R.id.colour_name);
            container.setTag(this);
        }
    }
}
