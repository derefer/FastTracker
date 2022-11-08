package com.blogspot.derefer.fasttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class SaveFastDialogFragment extends DialogFragment {
    public interface SaveFastDialogListener {
        public void onSaveFastDialogPositiveClick(DialogFragment dialog);
        public void onSaveFastDialogNegativeClick(DialogFragment dialog);
    }

    SaveFastDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            listener = (SaveFastDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SaveFastDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.save_fast)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSaveFastDialogPositiveClick(SaveFastDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSaveFastDialogNegativeClick(SaveFastDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
