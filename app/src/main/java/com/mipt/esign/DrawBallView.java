package com.mipt.esign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

public class DrawBallView extends View {

    Paint paint;

    private float currX = 100;

    private float currY = 100;

    private int ballColor = Color.GREEN;

    public int getBallColor() {
        return ballColor;
    }

    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
    }

    public float getCurrX() {
        return currX;
    }

    public float getCurrY() {
        return currY;
    }

    public void setCurrX(float currX) {
        this.currX = currX;
    }

    public void setCurrY(float currY) {
        this.currY = currY;
    }

    // DrawBallView constructor.
    public DrawBallView(Context context) {
        super(context);

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(this.getBallColor());

        for (int i = 0; i < Single.instance.getCoords().size(); i++) {
            List<float[]> points = Single.instance.getCoords();
            canvas.drawCircle(points.get(i)[0], points.get(i)[1], 15, paint);
        }

        List<Curve> slice = Single.instance.getSlice();
        if (slice != null) {
            for (int i = 0; i < slice.size(); i++) {
                for (int j = 0; j < slice.get(i).dots.size(); j++) {
                    if (i % 2 == 0) {
                        paint.setColor(Color.GREEN);
                    } else {
                        paint.setColor(Color.BLUE);
                    }
                    canvas.drawCircle((float) slice.get(i).dots.get(j).x(), (float) slice.get(i).dots.get(j).y(), 15, paint);
                }
            }
        }
    }
}