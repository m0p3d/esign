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

        // Get the root Linearlayout object.
        LinearLayout rootLayout = (LinearLayout)findViewById(R.id.idDrawBallView);

        // Create the DrawBallView custom view object.
        final DrawBallView drawBallView = new DrawBallView(this);

        //set min width and height.
        drawBallView.setMinimumWidth(500);
        drawBallView.setMinimumHeight(800);

        // Create a ontouch listener object.
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                // Set drawBallView currX and currY value to user finger x y ordinate value..
                drawBallView.setCurrX(motionEvent.getX());
                drawBallView.setCurrY(motionEvent.getY());

                // Set ball color to blue.
                drawBallView.setBallColor(Color.BLUE);

                // Notify drawBallView to redraw. This will invoke DrawBallView's onDraw() method.
                drawBallView.invalidate();

                // Return true means this listener has complete process this event successfully.
                return true;
            }
        };

        // Register onTouchListener object to drawBallView.
        drawBallView.setOnTouchListener(onTouchListener);

        // Add drawBallView object in root LinearLayout object.
        rootLayout.addView(drawBallView);
    }
}
