package com.example.michael.bluetoothcontroll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Michael on 5/29/2015.
 * This class contains all alert dialog boxes for when something goes wrong and the user needs to
 * be notified
 */
public class Alerts extends DialogFragment
{
    private EditText confirm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //use the Builder class for convenient dialog construction
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bluetooth not supported")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // close app
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void display()
    {

    }

}
