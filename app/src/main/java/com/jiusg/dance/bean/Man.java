package com.jiusg.dance.bean;

import android.content.Context;
import android.widget.ImageView;

import com.jiusg.dance.MainActivity;

/**
 * Created by Administrator on 2016/4/23.
 */
public class Man {
    //当前的状态
    // 0:准备状态 1:左上 2:右上 3:左下 4:右下
    public int currentStatus = 0;

    protected ImageView view;
    protected MainActivity activity;

    public Man(ImageView view,MainActivity activity){
        this.view = view;
        this.activity = activity;
    }

}
