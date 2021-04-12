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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab_3.R;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSignUp extends Fragment implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button confirm;
    private ImageButton cross;

    SharedPreferences sp;

    public final static String USERNAME = "com.example.calc_log_sign.USERNAME";
    public final static String PASSWORD = "com.example.calc_log_sign.PASSWORD";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);

        username = getView().findViewById(R.id.signup_username);
        password = getView().findViewById(R.id.signup_password);
        confirmPassword = getView().findViewById(R.id.confirmPassword);
        confirm = getView().findViewById(R.id.buttonConfirmSign);
        cross = getView().findViewById(R.id.buttonCross);

        confirm.setOnClickListener(this);
        cross.setOnClickListener(this);

        if (!sp.getBoolean("isTablet", true))
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonConfirmSign:
                onClickConfirm();
                break;
            case R.id.buttonCross:
                onClickCross();
                break;
        }

    }

    public void onClickCross(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        onClickListener listener = (onClickListener) getActivity();
        listener.onCancelClicked();
    }

    public void onClickConfirm () {
        //...
        boolean isCorrect = true;
        closeKeyboard();

        if (!checkUsername()) {
            username.setError(getString(R.string.username_error));
            isCorrect = false;
        }
        if (!checkPassword()) {
            password.setError(getString(R.string.password_error));
            isCorrect = false;
        }

        if (!checkConfirmPassword()) {
            confirmPassword.setError(getString(R.string.confirm_password_error));
            isCorrect = false;
        }

        if (isCorrect) {
            sp.edit().putString("username", username.getText().toString()).apply();
            sp.edit().putString("password", password.getText().toString()).apply();

            onClickListener listener = (onClickListener) getActivity();
            listener.onConfirmSignClicked();
        }
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkPassword() {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$";
        //String pattern = "^.{8,}$";
        if (password.getText().toString().matches(pattern))
            return true;
        else
            return false;
    }
    private boolean checkConfirmPassword() {
        if (confirmPassword.getText().toString().equals(password.getText().toString()))
            return true;
        else
            return false;
    }
    private boolean checkUsername() {
        String pattern = "^(?=.*[a-z])(?=\\S+$).{4,16}$";
        if (username.getText().toString().matches(pattern))
            return true;
        else
            return false;

    }

    public interface onClickListener {
        void onConfirmSignClicked();
        void onCancelClicked();
    }

}
