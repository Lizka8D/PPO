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

public class ExtendedMenu extends AppCompatActivity {

    EditText e1, e2;
    TextView inputResult;
    double num1, num2;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSpecificLayout(this.getResources().getConfiguration().orientation);
        //setContentView(R.layout.portrait_extended);

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

        if (n2.equals("0.0"))
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
            setContentView(R.layout.landscape_extended);
        }
        else if(orientation== Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.portrait_extended);
        }
    }
    public void onClick(View v) {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("Namber1",Double.toString(num1));
        editor.putString("Namber2",Double.toString(num2));
        editor.apply();

        Intent intent = new Intent(this, BaseMenu.class);
        startActivity(intent);
    }
    public boolean getNumbers(boolean checkBoxes) {
        e1 = (EditText) findViewById(R.id.num1);
        e2 = (EditText) findViewById(R.id.num2);

        inputResult = (TextView) findViewById(R.id.result);

        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();

        if (checkBoxes) {
            if (s1.equals("") && s2.equals("")) {

                inputResult.setText(R.string.error);
                return false;

            } else {
                num1 = Double.parseDouble(s1);
                if (s2.contains("/"))
                {
                    String[] text = s2.split("/");
                    int n1 = Integer.parseInt(text[0]);
                    int n2 = Integer.parseInt(text[1]);
                    num2 = (float) ((n1 / (n2 * 0.1)) * 0.1);
                }
                else
                {
                    num2 = Double.parseDouble(s2);
                }
            }
        }
        else
        {
            if (!s1.equals("") && s2.equals("")) {
                num1 = Double.parseDouble(s1);
            }
            else
            {
                inputResult.setText(R.string.error2);
                return false;
            }
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
        String check3 = getResources().getString(R.string.error2);

        if (result.equals(check1) || result.equals(check2) || result.equals(check3))
        {
            e2.setText(null);
        }
        else {
            e2.setText(result);
            num2 = Double.parseDouble(result);
        }
    }
   @SuppressLint("SetTextI18n")
    public void doPow(View v) {
        if (getNumbers(true)) {
            double pow = Math.pow(num1, num2);
            inputResult.setText(Double.toString(pow));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doSin(View v) {
        if (getNumbers(false)) {
            double sin = Math.sin(num1);
            inputResult.setText(Double.toString(sin));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doCos(View v) {
        if (getNumbers(false)) {
            double cos = Math.cos(num1);
            inputResult.setText(Double.toString(cos));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doTan(View v) {
        if (getNumbers(false)) {
            double tan = Math.tan(num1);
            inputResult.setText(Double.toString(tan));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doHyperbola(View v) {
        if (getNumbers(false)) {
            e2 = (EditText) findViewById(R.id.num2);
            e2.setText("1/");
        }
    }
    @SuppressLint("SetTextI18n")
    public void doFactorial(View v) {
        int result = 1;

        if (getNumbers(false)) {
            for (int i = 1; i <= num1; i++) {
                result = result * i;
            }
            inputResult.setText(Double.toString(result));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doSqrt(View v){
        if (getNumbers(false)) {
            double result = Math.sqrt(num1);
            inputResult.setText(Double.toString(result));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doLog(View v){
        if (getNumbers(false)) {
            double result = Math.log10(num1);
            inputResult.setText(Double.toString(result));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doRad(View v){
        if (getNumbers(false)) {
            double result = Math.toRadians(num1);
            inputResult.setText(Double.toString(result));
        }
    }
    @SuppressLint("SetTextI18n")
    public void doExp(View v){
        if (getNumbers(false)) {
            double result = Math.exp(num1);
            inputResult.setText(Double.toString(result));
        }
    }
}
