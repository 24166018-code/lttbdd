package com.example.lttbdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "user_session";
    public static final String KEY_LOGGED_IN = "is_logged_in";
    public static final String KEY_USERNAME = "username";

    private EditText usernameInput, passwordInput;
    private MaterialButton loginBtn;
    private TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_LOGGED_IN, false)) {
            goToHome();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        registerTv = findViewById(R.id.register_tv);

        UserStore userStore = new UserStore(this);

        loginBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                usernameInput.setError("Vui lòng nhập username");
            } else if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Vui lòng nhập password");
            } else if (!userStore.exists(username)) {
                usernameInput.setError("Tài khoản chưa tồn tại");
            } else if (!userStore.login(username, password)) {
                passwordInput.setError("Sai mật khẩu");
            } else {
                prefs.edit()
                        .putBoolean(KEY_LOGGED_IN, true)
                        .putString(KEY_USERNAME, username)
                        .apply();
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                goToHome();
            }
        });

        registerTv.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}