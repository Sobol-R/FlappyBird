package com.example.user.flappybird;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView banana;
    ImageView bg;
    View monkey;
    View monkey2;
    RelativeLayout root;
    RelativeLayout menu;
    TextView start;
    TextView viewScore;
    TextView viewRecord;

    float bananaV;
    int bananaY;
    int monkeyX;
    int monkey2X;
    int monkey2Y;
    int monkeyY;
    boolean check = false;
    int n = 3;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg = findViewById(R.id.bg);
        menu = findViewById(R.id.menu);
        start = findViewById(R.id.start);
        banana = findViewById(R.id.banana);
        monkey = findViewById(R.id.obstacle);
        monkey2 = findViewById(R.id.obstacle2);
        root = findViewById(R.id.root);
        viewScore = findViewById(R.id.score);
        viewRecord = findViewById(R.id.record);

        initializeTimer();
        startNewGame();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bananaV = -30f;
            }
        });
        AnimationDrawable bd = (AnimationDrawable) banana.getBackground();
        bd.start();
        if (savedInstanceState != null) {
            viewScore.setText("score:" + score);
        }

        String url = "https://images.fastcompany.net/image/upload/w_1280,f_auto,q_auto,fl_lossy/fc/3062252-poster-p-1-how-skyscrapers-work-for-kids.jpg";

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(this)
                .load(url)
                .apply(options)
                .into(bg);
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
                score = 0;
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
                score += 1;
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
        if (bananaY > 1280 || bananaY < -1280 || areIntersect(banana, monkey)) {
            menu.setVisibility(View.VISIBLE);
            monkey.setVisibility(View.GONE);
            check = false;
            n = 3;
            showRecord();
            startNewGame();
        }
    }
    /**
     * Метод проверяет, пересекаются ли две вьюшки (viewA и viewB).
     * Возвращает true, если пересекаются.
     * Важно: не поддерживает повернутые вьюшки.
     */
    private boolean areIntersect(View viewA, View viewB) {
        Rect rectA = getViewRect(viewA); // находим границы области, занимаемой viewA
        Rect rectB = getViewRect(viewB); // находим границы области, занимаемой viewB
        return rectA.intersect(rectB); // проверяем, пересекаются ли эти области
    }

    /**
     * Метод возвращает для любой вьюхи объект типа Rect (прямоугольник),
     * содержащий границы этой вьюхи.
     */
    private Rect getViewRect(View view) {
        int[] location = new int[2]; // создаём массив для координат левого верхнего угла
        view.getLocationOnScreen(location); // находим эти координаты
        // создаём прямоугольник и возвращаем его
        return new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
    }
    private void showRecord() {
        viewScore.setText("score: " + score);
        viewScore.setVisibility(View.VISIBLE);
        if (score > getRecord()) {
            viewRecord.setText("record: " + score);
            viewRecord.setVisibility(View.VISIBLE);
            saveRecord(score);
        } else {
            viewRecord.setText("record: " + getRecord());
            viewRecord.setVisibility(View.VISIBLE);
        }
    }
    public static final String record = "РЕКОРД";
    /**
     * Метод сохраняет рекорд в виде числа типа int
     * с идентификатором "РЕКОРД".
     */
    private void saveRecord(int rec) {
        // получаем штуку для сохранения данны
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // открываем её для записи
        SharedPreferences.Editor editor = preferences.edit();
        // записываем число
        editor.putInt(record, rec);
        // сохраняем изменения
        editor.apply();
    }
    /**
     * Метод получает сохранённый рекорд (с идентификатором "РЕКОРД"),
     * если же ничего не было сохранено - возвращает 0.
     */
    private int getRecord() {
        // получаем штуку для сохранения данных
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // получаем сохранённое число и возвращаем его
        return preferences.getInt(record, 0); // 0 - значение по умолчанию
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putInt("record", getRecord());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getInt("score");
        //rec = savedInstanceState.getInt("record");
    }
}
