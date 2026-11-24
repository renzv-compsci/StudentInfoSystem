package com.viloria.studentinfosystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PortalActivity extends AppCompatActivity {

    private static final String PREFS = "student_profile";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_PROGRAM = "program";
    private static final String KEY_EMAIL = "email";

    private BottomNavigationView bottomNav;
    private View dashboardView;
    private View scheduleView;
    private View gradesView;
    private View profileView;
    private TextView dashName;
    private TextView dashSub;
    private TextView dashProgram;
    private TextView notifBadge;
    private TextInputLayout tilName;
    private TextInputLayout tilNumber;
    private TextInputLayout tilProgram;
    private TextInputLayout tilEmail;
    private TextInputEditText etName;
    private TextInputEditText etNumber;
    private TextInputEditText etEmail;
    private MaterialAutoCompleteTextView actProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_portal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.portalRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        bottomNav = findViewById(R.id.bottomNav);
        dashboardView = findViewById(R.id.dashboardView);
        scheduleView = findViewById(R.id.scheduleView);
        gradesView = findViewById(R.id.gradesView);
        profileView = findViewById(R.id.profileView);

        dashName = findViewById(R.id.dashName);
        dashSub = findViewById(R.id.dashSub);
        dashProgram = findViewById(R.id.dashProgram);
        notifBadge = findViewById(R.id.notifBadge);

        bottomNav.setOnItemSelectedListener(item -> {
            showTab(item.getItemId());
            return true;
        });

        findViewById(R.id.btnStudentInfo).setOnClickListener(v ->
                startActivity(new Intent(this, StudentInfoActivity.class)));
        findViewById(R.id.btnSchedule).setOnClickListener(v ->
                bottomNav.setSelectedItemId(R.id.nav_schedule));
        findViewById(R.id.btnGrades).setOnClickListener(v ->
                bottomNav.setSelectedItemId(R.id.nav_grades));
        findViewById(R.id.btnProfile).setOnClickListener(v ->
                bottomNav.setSelectedItemId(R.id.nav_profile));

        findViewById(R.id.bellBtn).setOnClickListener(v -> {
            notifBadge.setVisibility(View.GONE);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifications_title)
                    .setMessage(R.string.notifications_body)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });

        setupGrades();
        setupProfile();

        if (getIntent().getBooleanExtra("open_profile", false)) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
            showTab(R.id.nav_profile);
        } else {
            showTab(R.id.nav_home);
        }
        loadProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    private void showTab(int itemId) {
        dashboardView.setVisibility(itemId == R.id.nav_home ? View.VISIBLE : View.GONE);
        scheduleView.setVisibility(itemId == R.id.nav_schedule ? View.VISIBLE : View.GONE);
        gradesView.setVisibility(itemId == R.id.nav_grades ? View.VISIBLE : View.GONE);
        profileView.setVisibility(itemId == R.id.nav_profile ? View.VISIBLE : View.GONE);
    }

    private void setupGrades() {
        LinearLayout list = findViewById(R.id.gradeList);
        String[][] rows = {
                {"CTINASSL", "Information Assurance and Security 1", "3", "3.50"},
                {"CCSFEN2L", "Software Engineering 2", "3", "3.75"},
                {"CCADMACL", "Advanced Machine Learning", "3", "3.50"},
                {"CTADEVL", "Applications Development and Emerging Technologies", "3", "3.25"}
        };
        for (String[] row : rows) {
            View item = getLayoutInflater().inflate(R.layout.item_grade_row, list, false);
            ((TextView) item.findViewById(R.id.rowCode)).setText(row[0]);
            ((TextView) item.findViewById(R.id.rowName)).setText(row[1]);
            ((TextView) item.findViewById(R.id.rowUnits)).setText(
                    getString(R.string.units_format, Integer.parseInt(row[2])));
            ((TextView) item.findViewById(R.id.rowGrade)).setText(row[3]);
            list.addView(item);
        }
        ((TextView) findViewById(R.id.gwaValue)).setText("3.50");
    }

    private void setupProfile() {
        tilName = findViewById(R.id.tilProfileName);
        tilNumber = findViewById(R.id.tilProfileNumber);
        tilProgram = findViewById(R.id.tilProfileProgram);
        tilEmail = findViewById(R.id.tilProfileEmail);
        etName = findViewById(R.id.etProfileName);
        etNumber = findViewById(R.id.etProfileNumber);
        etEmail = findViewById(R.id.etProfileEmail);
        actProgram = findViewById(R.id.actProfileProgram);

        String[] programs = getResources().getStringArray(R.array.programs);
        actProgram.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programs));

        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> saveProfile());
        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());
    }

    private void loadProfile() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = prefs.getString(KEY_NAME, getString(R.string.default_name));
        String number = prefs.getString(KEY_NUMBER, getString(R.string.default_student_no));
        String program = prefs.getString(KEY_PROGRAM, getString(R.string.program_bscs_ml));
        String email = prefs.getString(KEY_EMAIL, getString(R.string.default_email));

        dashName.setText(name);
        dashSub.setText(getString(R.string.dash_subtitle_format, number));
        dashProgram.setText(program);
        if (etName != null) {
            etName.setText(name);
            etNumber.setText(number);
            etEmail.setText(email);
            actProgram.setText(program, false);
        }
    }

    private void saveProfile() {
        tilName.setError(null);
        tilNumber.setError(null);
        tilProgram.setError(null);
        tilEmail.setError(null);

        String name = textOf(etName);
        String number = textOf(etNumber);
        String program = actProgram.getText() == null ? "" : actProgram.getText().toString().trim();
        String email = textOf(etEmail);

        boolean valid = true;
        if (name.isEmpty()) {
            tilName.setError(getString(R.string.error_required));
            valid = false;
        }
        if (number.isEmpty()) {
            tilNumber.setError(getString(R.string.error_required));
            valid = false;
        }
        if (program.isEmpty()) {
            tilProgram.setError(getString(R.string.error_program));
            valid = false;
        }
        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.error_required));
            valid = false;
        }
        if (!valid) {
            return;
        }

        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString(KEY_NAME, name)
                .putString(KEY_NUMBER, number)
                .putString(KEY_PROGRAM, program)
                .putString(KEY_EMAIL, email)
                .apply();
        loadProfile();
        Toast.makeText(this, R.string.toast_profile_saved, Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private static String textOf(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        if (bottomNav.getSelectedItemId() != R.id.nav_home) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
}
