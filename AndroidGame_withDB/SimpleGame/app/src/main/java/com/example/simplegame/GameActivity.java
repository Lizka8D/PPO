package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    private int pltX;
    //private int bX, bY;
    private boolean playing;
    RelativeLayout.LayoutParams ballParams;
    ImageView ballImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onStart(MotionEvent event) {
        super.onStart();

        //Связываемся с нашими объектами, определяя изображение через заданный ViewGroup:
        ViewGroup moveLayout = (ViewGroup) findViewById(R.id.game);
        ImageView pltImageView = (ImageView) moveLayout.findViewById(R.id.platform);
        ImageView ballImageView = (ImageView) moveLayout.findViewById(R.id.ball);
        //Создаем программно RelativeLayout с параметрами:
        RelativeLayout.LayoutParams pltParams = new RelativeLayout.LayoutParams(270, 120);
        pltParams.topMargin = 900;
        //Применяем эти параметры к нашему изображению:
        pltImageView.setLayoutParams(pltParams);

        RelativeLayout.LayoutParams ballParams = new RelativeLayout.LayoutParams(50,50);
        ballParams.topMargin = 200;

        //И настраиваем ему слушателя (обработчик) прикосновений:
        pltImageView.setOnTouchListener(this);
        ballImageView.setLayoutParams(ballParams);
        Run(event);
        playing = true;
    }
    protected void onResume(View view){
        super.onResume();
        //playing = false;
    }

    public void Run(MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        ballParams.topMargin = ballParams.topMargin - 400;
        ballImageView.setLayoutParams(ballParams);
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, MotionEvent event) {

        //Определение координат через getRawX() и getRawY() дает
        //координаты по отношению к размерам экрана устройства:
        final int X = (int) event.getRawX();

        if (playing) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                //ACTION_DOWN срабатывает при прикосновении к экрану,
                //здесь определяется начальное стартовое положение объекта:
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    pltX = X - lParams.leftMargin;
                    break;

                //ACTION_MOVE обрабатывает случившиеся в процессе прикосновения изменения, здесь
                //содержится информация о последней точке, где находится объект после окончания действия прикосновения ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams pltParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    pltParams.leftMargin = X - pltX;
                    //layoutParams.topMargin = Y - pltY;
                    pltParams.rightMargin = -250;
                    //layoutParams.bottomMargin = -250;

                    if (pltParams.leftMargin >= -40 && pltParams.leftMargin <= 490) {
                        view.setLayoutParams(pltParams);
                    }
                    break;
            }
        }

        return true;
    }
}