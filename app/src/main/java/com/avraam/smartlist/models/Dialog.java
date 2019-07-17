package com.avraam.smartlist.models;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {

    private Intent intent;
    private String title;
    private String massege;


    @Override
    public  android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(massege).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(intent);

            }
        });
        return builder.create();
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


    public String getTitle() {
        return title;
    }

    public String getMassege() {
        return massege;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMassege(String massege) {
        this.massege = massege;
    }




}
