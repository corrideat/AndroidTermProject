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
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class SortingOrderFragment extends DialogFragment {
    public SortingOrderFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.fragment_sorting_order, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.configure_sorting_order_radio_group);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        ((SortingOrder.SortingOrderActivity) getActivity()).setSortingOrder(SortingOrder.findByRadioId(checkedRadioButtonId));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SortingOrderFragment.this.getDialog().cancel();
                    }
                });
        radioGroup.check(
                ((SortingOrder.SortingOrderActivity) getActivity()).getSortingOrder().mRadioId
        );
        return builder.create();
    }
}
