package com.jiusg.dance;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiusg.dance.bean.Boy;
import com.jiusg.dance.bean.OldBoy;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView background;
    private ImageView boyView;
    private ImageView oldBoyView;
    private TextView button;
    private TextView click;
    private TextView scoreView;
    private LinearLayout pass;
    private LinearLayout layoutLive;
    private TextView levelView;
    private TextView liveView;

    private ArrayList<ImageView> listPass = new ArrayList<>();
    private ArrayList<ImageView> listLive = new ArrayList<>();

    // 相对于原图片的放大倍数
    private float multipleX = 0;
    private float multipleY = 0;

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public Boy boy;
    public OldBoy oldBoy;

    private Timer timer;
    private MainHandler handler;

    public int gameStatus = 0;
    public int score = 0;
    public int level = 0;
    public int live = 3;
    public int actionNum = 1;

    public final int START = 0;
    public final int RUNNING_BOY = 1;
    public final int RUNNING_OLD_BOY = 2;
    public final int STOP = 3;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();

        initData();
        initView();
        initPosition();

        boy = new Boy(boyView, this);
        oldBoy = new OldBoy(oldBoyView, this);

        handler = new MainHandler();

        startTimer();

    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 200);

        pass.setVisibility(View.GONE);
        click.setVisibility(View.GONE);
        level++;
        levelView.setText("第"+level+"关");
        if (actionNum < 6)
            actionNum++;
    }

    private void stopTimer() {
        timer.cancel();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        background = (ImageView) findViewById(R.id.background);
        boyView = (ImageView) findViewById(R.id.boy);
        oldBoyView = (ImageView) findViewById(R.id.oldBoy);
        button = (TextView) findViewById(R.id.button);
        click = (TextView) findViewById(R.id.click);
        pass = (LinearLayout) findViewById(R.id.pass);
        layoutLive = (LinearLayout) findViewById(R.id.live_linear);
        levelView = (TextView) findViewById(R.id.level);
        liveView = (TextView) findViewById(R.id.live);
        scoreView = (TextView) findViewById(R.id.score);

        click.setVisibility(View.GONE);

        glideView(R.drawable.bg, background);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameStatus) {
                    case START:
                        actionNum = 1;
                        live = 3;
                        level = 0;
                        score = 0;
                        levelView.setText("第1关");
                        liveView.setText(" "+live+" ");
                        scoreView.setText("得分："+score);
                        initLive(live);
                        button.setClickable(false);
                        button.setText("READY");
                        handler.sendEmptyMessageDelayed(1, 1000);
                        break;
                    case STOP:
                        button.setText("继续游戏");
                        gameStatus = START;
                        break;
                }
            }
        });

        click.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:

                        if (oldBoy.count >= boy.status.length)
                            return true;

                        float x = event.getX();
                        float y = event.getY();

                        if (x < v.getWidth() / 2 && y < v.getHeight() / 2) {
                            Log.i(TAG, "左上");
                            disposePass(oldBoy.count, oldBoy.count(1));
                            oldBoy.recover(handler);
                        } else if (x > v.getWidth() / 2 && y < v.getHeight() / 2) {
                            Log.i(TAG, "右上");
                            disposePass(oldBoy.count, oldBoy.count(2));
                            oldBoy.recover(handler);
                        } else if (x < v.getWidth() / 2 && y > v.getHeight() / 2) {
                            Log.i(TAG, "左下");
                            disposePass(oldBoy.count, oldBoy.count(3));
                            oldBoy.recover(handler);
                        } else if (x > v.getWidth() / 2 && y > v.getHeight() / 2) {
                            Log.i(TAG, "右下");
                            disposePass(oldBoy.count, oldBoy.count(4));
                            oldBoy.recover(handler);
                        }

                        break;
                }
                return true;
            }
        });
    }

    /**
     * 初始化必要的数据
     */
    private void initData() {
        float x = 176;
        float y = 220;

        multipleX = screenWidth / x;
        multipleY = screenHeight / y;
    }

    /**
     * 初始化控件位置
     */
    private void initPosition() {

        boyView.setX(0);
        boyView.setY(70 * multipleY);
        boyView.setMinimumWidth((int) (90 * multipleX));
        boyView.setMinimumHeight((int) (121 * multipleY));
        glideView(R.drawable.cstop, boyView);

        oldBoyView.setX((90 * multipleX));
        oldBoyView.setY(34 * multipleY);
        oldBoyView.setMinimumWidth((int) (86 * multipleX));
        oldBoyView.setMinimumHeight((int) (169 * multipleY));
        glideView(R.drawable.oready, oldBoyView);
    }


    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.i(TAG, "screenWidth=" + dm.widthPixels);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度（不除去状态栏）
     */
    public int getScreenHeight() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.i(TAG, "screenHeight=" + dm.heightPixels);
        return dm.heightPixels;
    }

    private void disposePass(int i, int isPass) {

        if (i >= listPass.size())
            return;

        if (isPass == 1) {
            listPass.get(i).setImageResource(R.mipmap.pass);
            score++;
            scoreView.setText("得分："+score);
        } else if (isPass == 2) {
            listPass.get(i).setImageResource(R.mipmap.notpass);
            live--;
            liveView.setText(" "+live+" ");
            initLive(live);
            if (live <= 0) {
                gameStatus = STOP;
                button.setText("游戏结束");
                button.setVisibility(View.VISIBLE);
                button.setClickable(true);
                click.setVisibility(View.GONE);
            }
        }
    }

    private void initPass(int num) {
        pass.removeAllViews();
        listPass.clear();
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.passbg);
            imageView.setMinimumWidth(35);
            imageView.setMinimumHeight(35);
            listPass.add(imageView);
            pass.addView(imageView);
        }
        pass.setVisibility(View.VISIBLE);
    }

    private void initLive(int num){
        layoutLive.removeAllViews();
        listLive.clear();
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.live);
            imageView.setMinimumWidth(35);
            imageView.setMinimumHeight(35);
            imageView.setPadding(5,5,5,5);
            listLive.add(imageView);
            layoutLive.addView(imageView);
        }

    }

    public void glideView(int res, ImageView view) {
        Glide.with(this)
                .load(res)
                .asGif()
                .into(view);
    }

    class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    if (gameStatus == RUNNING_BOY) {
                        if (boy.isStop) {
                            gameStatus = RUNNING_OLD_BOY;
                            oldBoy.init();
                            initPass(actionNum-1);
                            click.setVisibility(View.VISIBLE);
                            stopTimer();
                        } else {
                            boy.count();
                        }
                    }
                    break;
                case 1:
                    boy.start(actionNum);
                    button.setVisibility(View.GONE);
                    gameStatus = RUNNING_BOY;
                    startTimer();
                    break;
                case 2:
                    glideView(R.drawable.oready, oldBoyView);
                    if (oldBoy.count >= boy.status.length) {
                        button.setText("READY");
                        button.setVisibility(View.VISIBLE);
                        sendEmptyMessageDelayed(1, 1000);
                        removeMessages(2);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
