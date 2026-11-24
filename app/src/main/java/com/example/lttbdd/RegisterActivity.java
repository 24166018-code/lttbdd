package com.example.lttbdd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText usernameInput = findViewById(R.id.reg_username_input);
        EditText passwordInput = findViewById(R.id.reg_password_input);
        EditText confirmInput = findViewById(R.id.reg_confirm_input);
        MaterialButton registerBtn = findViewById(R.id.register_btn);
        TextView backToLoginTv = findViewById(R.id.back_to_login_tv);

        UserStore userStore = new UserStore(this);

        registerBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirm = confirmInput.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                usernameInput.setError("Vui lòng nhập username");
            } else if (username.length() < 3) {
                usernameInput.setError("Username tối thiểu 3 ký tự");
            } else if (password.length() < 6) {
                passwordInput.setError("Mật khẩu tối thiểu 6 ký tự");
            } else if (!password.equals(confirm)) {
                confirmInput.setError("Mật khẩu nhập lại không khớp");
            } else if (!userStore.register(username, password)) {
                usernameInput.setError("Username đã được sử dụng");
            } else {
                Toast.makeText(this, "Đăng ký thành công, hãy đăng nhập", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        backToLoginTv.setOnClickListener(v -> finish());
    }
}