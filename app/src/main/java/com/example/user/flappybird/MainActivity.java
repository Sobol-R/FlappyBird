package com.example.user.flappybird;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView bird;
    ImageView obstacle;
    RelativeLayout root;

    float birdV = 0;
    int birdY = 0;
    int obstY = 0;
    int obstX = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bird = findViewById(R.id.bird);
        obstacle = findViewById(R.id.obstacle);
        root = findViewById(R.id.root);

        initializeTimer();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birdV = -100f;
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
        obstX -= 3;
        if (obstacle.getTranslationX() < -500) {
            obstX = 500;
            obstY = (int) (Math.random() * 400 - 200);
        }
        birdV += 0.7;
        birdY += birdV;
    }

    private void onTimerUi() {
        bird.setTranslationY(birdY);
        obstacle.setTranslationX(obstX);
        obstacle.setTranslationY(obstY);
    }
}
