package ar.com.post.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ColourSwatchesSVFragment extends DialogFragment {
    public static final String ARG_VARY = "vary";

    GradientAdapter.Vary mVary;
    SwatchFragment mFragment;

    public ColourSwatchesSVFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mVary = (GradientAdapter.Vary) getArguments().getSerializable(ARG_VARY);
        super.onCreate(savedInstanceState);
    }

    public void setFragment(SwatchFragment fragment) {
        mFragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.fragment_colour_swatches_sv, null);
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.configure_colour_swatches_seek_bar);
        final SwatchFragment.SwatchActivity activity = (SwatchFragment.SwatchActivity) getActivity();
        final int min = activity.getMinNumberOfSwatches(mVary);
        seekBar.setMax(activity.getMaxNumberOfSwatches(mVary) - min);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int numberOfSwatches = seekBar.getProgress() + min;
                        mFragment.resetStep(numberOfSwatches);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ColourSwatchesSVFragment.this.getDialog().cancel();
                    }
                });
        seekBar.setProgress(
                activity.getNumberOfSwatches(mVary) - min
        );
        return builder.create();
    }
}
