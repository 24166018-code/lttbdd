
package com.example.lttbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UserStore {

    private final SharedPreferences prefs;

    public UserStore(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences("accounts", Context.MODE_PRIVATE);
    }

    private String key(String username) {
        return "user_" + username.trim().toLowerCase();
    }

    public boolean exists(String username) {
        return prefs.contains(key(username));
    }

    // Trả về false nếu username đã tồn tại
    public boolean register(String username, String password) {
        if (exists(username)) return false;

        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        String salt = Base64.encodeToString(saltBytes, Base64.NO_WRAP);

        prefs.edit().putString(key(username), salt + ":" + hash(salt, password)).apply();
        return true;
    }

    public boolean login(String username, String password) {
        String saved = prefs.getString(key(username), null);
        if (saved == null) return false;

        String[] parts = saved.split(":");
        if (parts.length != 2) return false;

        return parts[1].equals(hash(parts[0], password));
    }

    // Mật khẩu được băm (SHA-256 + salt), không lưu dạng chữ thường
    private String hash(String salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
