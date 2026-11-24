package com.viloria.studentinfosystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_success);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        float dp = getResources().getDisplayMetrics().density;
        View badge = findViewById(R.id.successBadge);
        View text = findViewById(R.id.successText);

        badge.setAlpha(0f);
        badge.setScaleX(0.7f);
        badge.setScaleY(0.7f);
        badge.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(450).start();

        text.setAlpha(0f);
        text.setTranslationY(16 * dp);
        text.animate().alpha(1f).translationY(0f).setDuration(450).setStartDelay(180).start();

        findViewById(R.id.btnBackDashboard).setOnClickListener(v ->
                startActivity(new Intent(SuccessActivity.this, PortalActivity.class)));
        findViewById(R.id.btnViewProfile).setOnClickListener(v -> {
            Intent intent = new Intent(SuccessActivity.this, PortalActivity.class);
            intent.putExtra("open_profile", true);
            startActivity(intent);
        });
    }
}
