package com.viloria.studentinfosystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View card = findViewById(R.id.loginCard);
        float offset = 28 * getResources().getDisplayMetrics().density;
        card.setAlpha(0f);
        card.setTranslationY(offset);
        card.animate().alpha(1f).translationY(0f).setDuration(450).setStartDelay(100).start();

        TextInputLayout tilUsername = findViewById(R.id.tilUsername);
        TextInputLayout tilPassword = findViewById(R.id.tilPassword);
        TextInputEditText etUsername = findViewById(R.id.etUsername);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            tilUsername.setError(null);
            tilPassword.setError(null);

            String username = etUsername.getText() == null ? "" : etUsername.getText().toString().trim();
            String password = etPassword.getText() == null ? "" : etPassword.getText().toString();

            boolean valid = true;
            if (TextUtils.isEmpty(username)) {
                tilUsername.setError(getString(R.string.error_username));
                valid = false;
            }
            if (TextUtils.isEmpty(password)) {
                tilPassword.setError(getString(R.string.error_password));
                valid = false;
            }
            if (valid && !(DEMO_USERNAME.equals(username) && DEMO_PASSWORD.equals(password))) {
                tilPassword.setError(getString(R.string.error_invalid_login));
                valid = false;
            }

            if (valid) {
                startActivity(new Intent(MainActivity.this, PortalActivity.class));
            }
        });
    }
}
