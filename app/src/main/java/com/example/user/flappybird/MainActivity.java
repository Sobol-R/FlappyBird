package com.example.user.flappybird;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView banana;
    ImageView monkey;
    RelativeLayout root;
    RelativeLayout menu;
    TextView start;

    float bananaV;
    int bananaY;
    int monkeyX;
    int monkeyY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menu);
        start = findViewById(R.id.start);
        banana = findViewById(R.id.banana);
        monkey = findViewById(R.id.obstacle);
        root = findViewById(R.id.root);
        startNewGame();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bananaV = -30f;
            }
        });
    }
    private void startNewGame() {
        menu.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.GONE);
                bananaV = 0;
                bananaY = 0;
                monkeyX = 500;
                monkeyY = 0;
                initializeTimer();
            }
        });
    }
    private void initializeTimer() {
        final Handler handler = new Handler();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onTimer();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onTimerUi();
                    }
                });
            }
        }, 25, 25);
    }

    private void onTimer() {
        monkeyX -= 3;
        if (monkey.getTranslationX() < -500) {
            monkeyX = 500;
            monkeyY = (int) (Math.random() * 400 - 200);
        }
        bananaV += 2;
        bananaY += bananaV;
    }

    private void onTimerUi() {
        banana.setTranslationY(bananaY);
        monkey.setTranslationX(monkeyX);
        monkey.setTranslationY(monkeyY);
        if (bananaY > 1280 || bananaY < -1280) {
            startNewGame();
        }
    }
}
