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
    View monkey;
    View monkey2;
    RelativeLayout root;
    RelativeLayout menu;
    TextView start;
    TextView score;

    float bananaV;
    int bananaY;
    int monkeyX;
    int monkey2X;
    int monkey2Y;
    int monkeyY;
    boolean check = false;
    int n = 3;
    int sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menu);
        start = findViewById(R.id.start);
        banana = findViewById(R.id.banana);
        monkey = findViewById(R.id.obstacle);
        monkey2 = findViewById(R.id.obstacle2);
        root = findViewById(R.id.root);
        score = findViewById(R.id.score);
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
                monkey.setVisibility(View.VISIBLE);
                bananaV = 0;
                bananaY = 0;
                monkeyX = 500;
                monkeyY = 0;
                check = true;
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
        if (check == true) {
            if (n > 10) {
                monkey2X -= n;
            }
            monkeyX -= n;
            if (monkey.getTranslationX() < -500) {
                sc += 1;
                monkeyX = 500;
                monkeyY = (int) (Math.random() * 700 - 200);
                if (n < 20) {
                    n += 2;
                }
            }
            if (monkey2.getTranslationX() < -500) {
                monkey2X = 500;
                monkey2Y = (int) (Math.random() * 1000 - 200);
            }
            bananaV += 2;
            bananaY += bananaV;
        } else {
            bananaV = 0;
            bananaY = 0;
            monkeyX = 500;
            monkeyY = 0;
        }

    }

    private void onTimerUi() {
        banana.setTranslationY(bananaY);
        monkey.setTranslationX(monkeyX);
        monkey.setTranslationY(monkeyY);
        if (bananaY > 1280 || bananaY < -1280 || bananaY == monkeyY || bananaY == monkey2Y) {
            monkey.setVisibility(View.GONE);
            score.setVisibility(View.VISIBLE);
            score.setText("score :  " + sc);
            check = false;
            startNewGame();
        }
    }
}
