package com.mipt.esign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.idDrawBallView);

        final DrawBallView drawBallView = new DrawBallView(this);

        drawBallView.setMinimumWidth(500);
        drawBallView.setMinimumHeight(800);

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                drawBallView.setCurrX(motionEvent.getX());
                drawBallView.setCurrY(motionEvent.getY());

                drawBallView.setBallColor(Color.BLUE);
                drawBallView.invalidate();
                return true;
            }
        };

        drawBallView.setOnTouchListener(onTouchListener);

        rootLayout.addView(drawBallView);
    }
}
