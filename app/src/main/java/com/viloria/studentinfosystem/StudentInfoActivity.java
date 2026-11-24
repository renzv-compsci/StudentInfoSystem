package com.viloria.studentinfosystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StudentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_student_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View card = findViewById(R.id.infoCard);
        float offset = 28 * getResources().getDisplayMetrics().density;
        card.setAlpha(0f);
        card.setTranslationY(offset);
        card.animate().alpha(1f).translationY(0f).setDuration(450).setStartDelay(100).start();

        TextInputLayout tilStudentNumber = findViewById(R.id.tilStudentNumber);
        TextInputLayout tilLastName = findViewById(R.id.tilLastName);
        TextInputLayout tilFirstName = findViewById(R.id.tilFirstName);
        TextInputLayout tilMiddleName = findViewById(R.id.tilMiddleName);
        TextInputLayout tilProgram = findViewById(R.id.tilProgram);

        TextInputEditText etStudentNumber = findViewById(R.id.etStudentNumber);
        TextInputEditText etLastName = findViewById(R.id.etLastName);
        TextInputEditText etFirstName = findViewById(R.id.etFirstName);
        TextInputEditText etMiddleName = findViewById(R.id.etMiddleName);
        MaterialAutoCompleteTextView actProgram = findViewById(R.id.actProgram);

        String[] programs = getResources().getStringArray(R.array.programs);
        actProgram.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programs));

        SharedPreferences prefs = getSharedPreferences("student_profile", MODE_PRIVATE);
        etStudentNumber.setText(prefs.getString("number", getString(R.string.default_student_no)));
        etLastName.setText("Viloria");
        etFirstName.setText("Renz Jerik");
        etMiddleName.setText("C.");
        actProgram.setText(prefs.getString("program", getString(R.string.program_bscs_ml)), false);

        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            tilStudentNumber.setError(null);
            tilLastName.setError(null);
            tilFirstName.setError(null);
            tilMiddleName.setError(null);
            tilProgram.setError(null);

            String studentNumber = textOf(etStudentNumber);
            String lastName = textOf(etLastName);
            String firstName = textOf(etFirstName);
            String middleName = textOf(etMiddleName);
            String program = textOf(actProgram);

            boolean valid = true;
            if (TextUtils.isEmpty(studentNumber)) {
                tilStudentNumber.setError(getString(R.string.error_required));
                valid = false;
            }
            if (TextUtils.isEmpty(lastName)) {
                tilLastName.setError(getString(R.string.error_required));
                valid = false;
            }
            if (TextUtils.isEmpty(firstName)) {
                tilFirstName.setError(getString(R.string.error_required));
                valid = false;
            }
            if (TextUtils.isEmpty(middleName)) {
                tilMiddleName.setError(getString(R.string.error_required));
                valid = false;
            }
            if (TextUtils.isEmpty(program)) {
                tilProgram.setError(getString(R.string.error_program));
                valid = false;
            }

            if (valid) {
                startActivity(new Intent(StudentInfoActivity.this, SuccessActivity.class));
            }
        });
    }

    private static String textOf(EditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}
