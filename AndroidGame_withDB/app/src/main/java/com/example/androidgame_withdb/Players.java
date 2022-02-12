package com.example.androidgame_withdb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class Players extends Activity {

    ListView list;
    UseDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu2);

        db = new UseDatabase(getApplicationContext());
        list = (ListView) findViewById(R.id.user_inf);

        db.SortData();
        loadListData();
    }

    @SuppressLint("SetTextI18n")
    public void SavePlayer(View view) {

        final EditText name = findViewById(R.id.name);
        String label_name = name.getText().toString();

        name.setText(null);

        if (label_name.trim().length() > 0 && !label_name.equals("Please, enter the name")) {
            db.InsertData(label_name, "0", "yes");
            loadListData();
        }
        else {
            name.setText("Please, enter the name");
        }
    }

    @SuppressLint("SetTextI18n")
    public void SelectPlayer(View view) {

        final EditText name = findViewById(R.id.name);
        String label_name = name.getText().toString();

        name.setText(null);
        if (label_name.trim().length() > 0 && !label_name.equals("Please, enter the name")) {
            db.UpDataActivate(label_name);
            loadListData();
        }
        else {
            name.setText("Please, enter the name");
        }
    }

    @SuppressLint("SetTextI18n")
    public void DeletePlayer(View view) {

        final EditText name = findViewById(R.id.name);
        String label_name = name.getText().toString();

        name.setText(null);
        if (label_name.trim().length() > 0 && !label_name.equals("Please, enter the name")) {
            db.DropData(label_name);
            db.SortData();
            loadListData();
        }
        else {
            name.setText("Please, enter the name");
        }
    }

    private void loadListData() {

        List<String> labels = db.GetData();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        list.setAdapter(dataAdapter);
    }
}
