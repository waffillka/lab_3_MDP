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
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements FragmentList.OnAddButtonListener,
        FragmentPick.OnButtonClickListener,
        FragmentLogIn.onClickListener,
        FragmentSignUp.onClickListener
{

    private final int PERMISSION_REQUEST_CODE = 1;

    private Button buttonAdd;

    SharedPreferences sp; // хранилище

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

    public void AddOnClick(View view){
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    //pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void onAddButtonClicked(Button add) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentPick frag = new FragmentPick();

        ft.replace(R.id.pick_layout, frag, "fragment_pick");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commit();

        buttonAdd = add;
        buttonAdd.setClickable(false);
        buttonAdd.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rect));
        buttonAdd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreyLight));
    }

    private void buttonSetActive(){
        buttonAdd.setClickable(true);
        buttonAdd.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_button));
        buttonAdd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
    }

    @Override
    public void onCloseButtonClicked() {
        buttonSetActive();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onConfirmButtonClicked(Uri imageUri, Uri musicUri) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentList frag = (FragmentList) fm.findFragmentById(R.id.list_layout);
        frag.setImageMusic(imageUri, musicUri);

        buttonSetActive();
        fm.popBackStack();
        Toast.makeText(this,"Posted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogInClicked(String name) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentList frag = new FragmentList();

        ft.replace(R.id.list_layout, frag, "fragment_list");
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onSignUpClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentSignUp frag = new FragmentSignUp();

        ft.replace(R.id.list_layout, frag, "fragment_sign");
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
        else if (fm.findFragmentById(R.id.list_layout) instanceof FragmentSignUp)
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

