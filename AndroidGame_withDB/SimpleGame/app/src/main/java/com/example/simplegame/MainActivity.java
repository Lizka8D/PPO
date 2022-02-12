package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onPause(View view) {
        super.onPause();
        //playing = !playing;
    }
    /*@SuppressLint("ClickableViewAccessibility")
    public void Mb(View view, MotionEvent event){
        final float X = (float) event.getRawX();
        final float Y = (float) event.getRawY();

        RelativeLayout.LayoutParams ballParams = (RelativeLayout.LayoutParams) view
                .getLayoutParams();

        ballParams.leftMargin = (int)X / 2;
        ballParams.topMargin = (int)Y - 20;
        ballParams.rightMargin = (int)(X / 2 + 50);
        ballParams.bottomMargin = (int)(Y - 20  - 50);
    }*/
    //Обрабатываем прикосновения к объекту:

    public void onBackPressed(View view) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", (dialog, which) -> {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }).setNegativeButton("no", null).show();
    }
}
