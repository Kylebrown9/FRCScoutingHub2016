package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by Admin on 3/1/2016.
 */
public class PasscodeDialog {
    private Dialog dialog;
    final EditText input;

    public PasscodeDialog(Context context, final PasscodeSelectionListener pSL, String hubName) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alert.setView(input);
        alert.setTitle(hubName);
        alert.setMessage("Please enter passcode");

        DialogInterface.OnClickListener doneListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pSL.onPasscodeSelect(input.getText().toString());
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pSL.onPasscodeSelect(null);
            }
        };

        alert.setPositiveButton("Submit", doneListener);
        alert.setNegativeButton("Cancel", cancelListener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }

    public interface PasscodeSelectionListener {
        void onPasscodeSelect(String passcode);
    }
}
