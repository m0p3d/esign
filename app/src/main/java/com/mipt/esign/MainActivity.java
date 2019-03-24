package com.mipt.esign;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import math.geom2d.Vector2D;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    List<Curve> curves;
    Curve etalon = new Curve();
    SignChecker checker;
    String number = "";

    OkHttpClient client = new OkHttpClient();

    int mode = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String runApi(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String runPost(String url) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("number", number)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.idDrawBallView);
        textView = (TextView) findViewById(R.id.xy);

        curves = new ArrayList<>();
        checker = new SignChecker();

        final DrawBallView drawBallView = new DrawBallView(this);

        drawBallView.setMinimumWidth(500);
        drawBallView.setMinimumHeight(800);

        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }

                    try {
                        String result = runApi("http://134.0.113.208/mytry");
                        if (result.equals("reg")) {
                            curves = new ArrayList<>();
                            mode = 1;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Начните регистрацию", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (result.equals("log")) {
                            curves = new ArrayList<>();
                            mode = 2;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Начните авторизацию", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                List<float[]> coords = Single.instance.getCoords();

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (curves.size() < 2) {
                        curves.add(new Curve());
                        for (int i = 0; i < coords.size(); i++) {
                            curves.get(curves.size() - 1).dots.add(new Vector2D(coords.get(i)[0], coords.get(i)[1]));
                        }
                        if (mode == 2) {
                            boolean same = checker.check(curves.get(curves.size()-1));
                            Toast.makeText(MainActivity.this,
                                    String.valueOf(same),
                                    Toast.LENGTH_SHORT).show();

                            if (same) {
                                Thread thread = new Thread() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void run() {
                                        try {
                                            String result = runApi("http://134.0.113.208/accepted");
                                            int a = 3 * 4;

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            }

                            mode = 0;
                            curves = new ArrayList<>();
                        }
                    }
                    // Only reg mode is available
                    if (curves.size() == 2) {
                        boolean same = checker.create(curves);
                        Toast.makeText(MainActivity.this, String.valueOf(same
                                        + "   " + String.valueOf(checker.dbgReadSlice(curves.get(0)).size())
                                        + "   " + String.valueOf(checker.dbgReadSlice(curves.get(1)).size())),
                                Toast.LENGTH_SHORT).show();

                        if (mode == 1 && same) {
                            etalon = curves.get(0);
                            mode = 0;
                        }
                        curves = new ArrayList<>();
                    }
                    Single.instance.flushCoords();
                }

                textView.setText("X: " + String.valueOf(motionEvent.getX()) + "; Y: " + String.valueOf(motionEvent.getY()));
                Single.instance.getCoords().add(new float[] {motionEvent.getX(), motionEvent.getY()});

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
