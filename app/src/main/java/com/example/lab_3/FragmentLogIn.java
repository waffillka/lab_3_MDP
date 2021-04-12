package com.example.lab_3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class FragmentLogIn extends Fragment implements View.OnClickListener {
    static final private int LOGIN_PASS = 0;
    //static final private int RC_SIGN_IN = 1;

    private EditText username;
    private EditText password;
    private Button confirm;
    private TextView signUp;

    private TextView error;

    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);

        username = getView().findViewById(R.id.login_username);
        password = getView().findViewById(R.id.login_password);
        error = getView().findViewById(R.id.err);
        confirm = getView().findViewById(R.id.buttonConfirm);
        signUp = getView().findViewById(R.id.textView3);

        confirm.setOnClickListener(this);
        signUp.setOnClickListener(this);

        error.setVisibility(View.INVISIBLE);

        sp.edit().putBoolean("isTablet", true).apply();

        if (!isTablet()) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            sp.edit().putBoolean("isTablet", false).apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonConfirm:
                onClickLogIn();
                break;
            case R.id.textView3:
                onClickSignUp();
                break;
        }

    }

    public void onClickSignUp () {
        onClickListener listener = (onClickListener) getActivity();
        listener.onSignUpClicked();

    }

    public void onClickLogIn () {
        closeKeyboard();

        if (checkData()) {
            sp.edit().putString("current_user", username.getText().toString()).apply();
            logIn();
        }
        else {
            error.setVisibility(View.VISIBLE);
        }

    }

    public void logIn() {
        onClickListener listener = (onClickListener) getActivity();
        sp.edit().putBoolean("logged", true).apply();
        error.setVisibility(View.INVISIBLE);
        listener.onLogInClicked(username.getText().toString());
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkData() {
        if ((username.getText().toString().equals(sp.getString("username", "admin"))
                || username.getText().toString().equals("admin"))
                && (password.getText().toString().equals(sp.getString("password", "admin"))
                || password.getText().toString().equals("admin")))
            return true;
        return false;
    }

    public boolean isTablet() {
        return false;
    }

    public void setUsernamePass (){
        this.username.setText(sp.getString("username", "admin"));
        this.password.setText(sp.getString("password", "admin"));
    }

    public interface onClickListener {
        void onLogInClicked(String name);
        void onSignUpClicked();
    }
}

