package com.jiusg.dance.bean;

import android.widget.ImageView;

import com.jiusg.dance.MainActivity;
import com.jiusg.dance.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/4/23.
 */
public class Boy extends Man {

    // 当前回合所有状态
    public int[] status = null;

    // 速度，最大为1,最小为10
    // 不可为 0
    public int speed = 10;

    // 当前回合动作数(已完成)
    public int actionNum = 0;

    // 本回合是否结束
    public boolean isStop = false;

    // 计数
    public int count = 0;

    public Boy(ImageView view, MainActivity activity) {
        super(view, activity);
    }

    /**
     * 开始回合
     * 每回合开始前都应该调用在此方法
     * @param num 动作数
     */
    public void start(int num){

        status = new int[num];
        actionNum = 0;
        isStop = false;
    }

    public void count() {

        count++;

        if(speed == 0)
            return;

        if(status == null)
            return;

        if (count % speed == 0) {

            if(currentStatus == 0){
                getRandomStatus();
            }else {
                currentStatus = 0;
            }

            switch (currentStatus){
                case 0:
                    activity.glideView(R.drawable.cready,view);
                    break;
                case 1:
                    activity.glideView(R.drawable.c1,view);
                    setStatus(1);
                    break;
                case 2:
                    activity.glideView(R.drawable.c3,view);
                    setStatus(2);
                    break;
                case 3:
                    activity.glideView(R.drawable.c7,view);
                    setStatus(3);
                    break;
                case 4:
                    activity.glideView(R.drawable.c9,view);
                    setStatus(4);
                    break;
                default:
                    activity.glideView(R.drawable.cready,view);
                    break;
            }

        }

    }

    private void setStatus(int currentStatus){
        if(status == null)
            return;

        if(actionNum < status.length) {
            status[actionNum++] = currentStatus;
        }else{
            isStop = true;
            activity.glideView(R.drawable.cstop,view);
        }


    }

    // 得到一个随机状态
    private void getRandomStatus() {
        currentStatus = 1 + (int) (Math.random() * 4);
    }
}
