package com.example.androidcalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseMenu extends AppCompatActivity {

    EditText e1, e2;
    TextView inputResult;
    double num1, num2;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSpecificLayout(this.getResources().getConfiguration().orientation);
        //setContentView(R.layout.portrait_base);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String n1 = preferences.getString("Namber1", "");
        String n2 = preferences.getString("Namber2", "");

        e1 = (EditText) findViewById(R.id.num1);
        e2 = (EditText) findViewById(R.id.num2);

        if (n1.equals("0.0"))
        {
            e1.setText(null);
        }
        else
        {
            e1.setText(n1);
        }

        if (n1.equals("0.0"))
        {
            e2.setText(null);
        }
        else
        {
            e2.setText(n2);
        }
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        setSpecificLayout(newConfig.orientation);
    }
    public void setSpecificLayout(int orientation){
        if(orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.landscape_base);
        }
        else if(orientation== Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.portrait_base);
        }
    }
    public void onClick(View v) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("Namber1",Double.toString(num1));
        editor.putString("Namber2",Double.toString(num2));
        editor.apply();

        Intent intent = new Intent(this, ExtendedMenu.class);
        startActivity(intent);
    }
    public boolean getNumbers() {
        e1 = (EditText) findViewById(R.id.num1);
        e2 = (EditText) findViewById(R.id.num2);

        inputResult = (TextView) findViewById(R.id.result);

        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();

        if (s1.equals("") && s2.equals("")) {

            inputResult.setText(R.string.error);

            return false;
        } else {
            num1 = Double.parseDouble(s1);
            num2 = Double.parseDouble(s2);
        }

        return true;
    }
    @SuppressLint("SetTextI18n")
    public void clearAll(View v) {
        e1 = (EditText) findViewById(R.id.num1);
        e2 = (EditText) findViewById(R.id.num2);
        inputResult = (TextView) findViewById(R.id.result);
        e1.setText(null);
        e2.setText(null);
        inputResult.setText(R.string.result);
    }
    @SuppressLint("SetTextI18n")
    public void clearNum1(View v) {
        e1 = (EditText) findViewById(R.id.num1);
        e1.setText(null);
    }
    @SuppressLint("SetTextI18n")
    public void clearNum2(View v) {
        e2 = (EditText) findViewById(R.id.num2);
        e2.setText(null);
    }
    @SuppressLint("SetTextI18n")
    public void takeNumber(View v) {
        e2 = (EditText) findViewById(R.id.num2);
        inputResult = (TextView) findViewById(R.id.result);
        String result = inputResult.getText().toString();
        String check1 = getResources().getString(R.string.result);
        String check2 = getResources().getString(R.string.error);

        if (result.equals(check1) || result.equals(check2))
        {
            e2.setText(null);
        }
        else {
            e2.setText(result);
            num2 = Double.parseDouble(result);
        }
    }
    @SuppressLint("SetTextI18n")
    public void doSum(View v) {
        if (getNumbers()) {
            double sum = num1 + num2;
            inputResult.setText(Double.toString(sum));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doSub(View v) {
        if (getNumbers()) {
            double sub = num1 - num2;
            inputResult.setText(Double.toString(sub));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doMul(View v) {
        if (getNumbers()) {
            double mul = num1 * num2;
            inputResult.setText(Double.toString(mul));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doDiv(View v) {
        if (getNumbers()) {
            double div = num1 / num2;
            inputResult.setText(Double.toString(div));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doMod(View v) {
        if (getNumbers()) {
            double mod = num1 * num2 / 100;
            inputResult.setText(Double.toString(mod));
        }
    }
}
