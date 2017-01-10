package com.strozh.emailclient.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.strozh.emailclient.R;
import com.strozh.emailclient.navdraw.NavDrawActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    // UI references.
    private EditText e_t_email;
    private EditText e_t_password;
    private Button signInButton;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoginForm();
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getPresenter().isLogin()) {
                    getPresenter().validationEmailPass();
                    toMainMenu(getPresenter().isLogin());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        toMainMenu(getPresenter().isLogin());
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenterImpl(getApplicationContext());
    }

    @Override
    public String getEmail() {
        return e_t_email.getText().toString();
    }

    @Override
    public String getPassword() {
        return e_t_password.getText().toString();
    }

    @Override
    public void showLoginForm() {
        setContentView(R.layout.activity_login);
        // Set up the login form.
        e_t_email = (EditText) findViewById(R.id.email);
        e_t_password = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        e_t_email.setText(null);
        e_t_password.setText(null);
    }

    @Override
    public void showEmailError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_email), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPasswordError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_password), Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccessful(String email) {
        Toast.makeText(getApplicationContext(), getString(R.string.LoginAs) + email, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void toMainMenu(boolean flag) {
        if (flag) {
            finish();
            Intent intent = new Intent(LoginActivity.this, NavDrawActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

