package com.example.lab_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        com.example.gallery.FragmentSignUp.onClickListener{

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (!sp.getBoolean("logged", false)) {
            FragmentLogIn frag = new FragmentLogIn();
            ft.replace(R.id.list_layout, frag, "fragment_log");
        }
        else {
            FragmentList frag = new FragmentList();
            ft.replace(R.id.list_layout, frag, "fragment_list");
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onConfirmSignClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(fm.findFragmentById(R.id.list_layout));

        FragmentLogIn frag = new FragmentLogIn();

        ft.replace(R.id.list_layout, frag, "fragment_log");
        ft.addToBackStack(null);

        ft.commit();

        fm.executePendingTransactions();
        frag.setUsernamePass();
    }

    @Override
    public void onCancelClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentLogIn frag = new FragmentLogIn();

        ft.replace(R.id.list_layout, frag, "fragment_log");
        ft.addToBackStack(null);
        //ft.remove(fm.findFragmentById(R.id.pick_layout));

        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.list_layout) instanceof FragmentList) {
            if (fm.findFragmentById(R.id.pick_layout) instanceof FragmentPick) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fm.findFragmentById(R.id.pick_layout));
                ft.commit();
                //fm.popBackStack();
                onCloseButtonClicked();
                System.out.println("yes!!!!");
            } else
                dialogOnClick();
        }
        else if (fm.findFragmentById(R.id.list_layout) instanceof com.example.gallery.FragmentSignUp)
            onCancelClicked();
        else
            finish();
    }

    public void dialogOnClick (){
        AlertDialog.Builder dial = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        dial.setMessage(getString(R.string.exit_dialog)).setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListAdapter.stopPlay();
                        sp.edit().putBoolean("logged", false).apply();
                        onCancelClicked();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = dial.create();
        alert.setTitle(getString(R.string.log_out));
        alert.show();
    }
}