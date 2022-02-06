package com.example.androidlistemployees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String nameSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner_name);
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();
    }

    public void SaveData(View arg0) {

        final EditText name = findViewById(R.id.name);
        final EditText age = findViewById(R.id.age);
        final EditText post = findViewById(R.id.post);
        final EditText salary = findViewById(R.id.salary);
        final EditText experience = findViewById(R.id.experience);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);

        String label_name = name.getText().toString();
        String label_age = age.getText().toString();
        String label_post = post.getText().toString();
        String label_salary = salary.getText().toString();
        String label_experience = experience.getText().toString();
        String label_phoneNumber = phoneNumber.getText().toString();

        if (label_name.trim().length() > 0 && label_age.trim().length() > 0 &&
                label_post.trim().length() > 0 && label_salary.trim().length() > 0 &&
                label_experience.trim().length() > 0 && label_phoneNumber.trim().length() > 0) {

            UseDatabase db = new UseDatabase(getApplicationContext());
            db.InsertData(label_name, label_age, label_post, label_salary, label_experience, label_phoneNumber);

            // making input filed text to blank
            name.setText(null);
            age.setText(null);
            post.setText(null);
            salary.setText(null);
            experience.setText(null);
            phoneNumber.setText(null);

            // loading spinner with newly added data
            loadSpinnerData();
        } else {
            Toast.makeText(getApplicationContext(), "Please, enter the data.",
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void EditData(View view) {

        final EditText age = findViewById(R.id.age);
        final EditText post = findViewById(R.id.post);
        final EditText salary = findViewById(R.id.salary);
        final EditText experience = findViewById(R.id.experience);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);

        String label_age = age.getText().toString();
        String label_post = post.getText().toString();
        String label_salary = salary.getText().toString();
        String label_experience = experience.getText().toString();
        String label_phoneNumber = phoneNumber.getText().toString();

        if (label_age.trim().length() > 0 && label_post.trim().length() > 0 &&
                label_salary.trim().length() > 0 && label_experience.trim().length() > 0 &&
                label_phoneNumber.trim().length() > 0) {

            UseDatabase db = new UseDatabase(getApplicationContext());
            db.UpdataData(nameSelect, label_age, label_post, label_salary, label_experience, label_phoneNumber);

            // making input filed text to blank
            age.setText(null);
            post.setText(null);
            salary.setText(null);
            experience.setText(null);
            phoneNumber.setText(null);

            // loading spinner with newly added data
            loadSpinnerData();

            Toast.makeText(getApplicationContext(), "Data changed.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please, enter the data.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void DeleteData(View view) {

        UseDatabase db = new UseDatabase(getApplicationContext());
        db.DropData(nameSelect);

        final EditText age = findViewById(R.id.age);
        final EditText post = findViewById(R.id.post);
        final EditText salary = findViewById(R.id.salary);
        final EditText experience = findViewById(R.id.experience);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);

        age.setText(null);
        post.setText(null);
        salary.setText(null);
        experience.setText(null);
        phoneNumber.setText(null);

        loadSpinnerData();

        Toast.makeText(getApplicationContext(), "Deleted.", Toast.LENGTH_SHORT).show();
    }

    private void loadSpinnerData() {
        UseDatabase db = new UseDatabase(getApplicationContext());
        List<String> labels = db.GetNames();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        UseDatabase db = new UseDatabase(getApplicationContext());

        final EditText age = findViewById(R.id.age);
        final EditText post = findViewById(R.id.post);
        final EditText salary = findViewById(R.id.salary);
        final EditText experience = findViewById(R.id.experience);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);

        // On selecting a spinner item
        nameSelect = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + nameSelect, Toast.LENGTH_LONG).show();

        String[] data = db.GetDataForName(nameSelect);

        age.setText(data[0]);
        post.setText(data[1]);
        salary.setText(data[2]);
        experience.setText(data[3]);
        phoneNumber.setText(data[4]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}