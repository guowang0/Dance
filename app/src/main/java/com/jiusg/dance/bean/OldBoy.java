package com.jiusg.dance.bean;

import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiusg.dance.MainActivity;
import com.jiusg.dance.R;

import java.util.Timer;

/**
 * Created by Administrator on 2016/4/23.
 */
public class OldBoy extends Man {

    // 最大错误数
    public int maxErrorNum = 3;

    // 当前错误数
    public int currentErrorNum = 0;

    // 记录按下的次数
    public int count = 0;

    public OldBoy(ImageView view, MainActivity activity) {
        super(view, activity);
    }

    public void init(){
        count = 0;
    }

    /**
     * 0:非正常 1:正确 2:错误
     * @param status 按下去的状态
     */
    public int count(int status) {

        if(activity.boy.status.length <= count)
            return 0;

        setViewStatus(status);

        if (activity.boy.status[count++] == status){
            return 1;
        }

        currentErrorNum++;
        return 2;
    }

    public void recover(Handler handler){
        handler.removeMessages(2);
        handler.sendEmptyMessageDelayed(2,1000);
    }

    private void setViewStatus(int status){

        switch (status){
            case 0:
                activity.glideView(R.drawable.oready,view);
                break;
            case 1:
                activity.glideView(R.drawable.o1,view);

                break;
            case 2:
                activity.glideView(R.drawable.o3,view);

                break;
            case 3:
                activity.glideView(R.drawable.o7,view);

                break;
            case 4:
                activity.glideView(R.drawable.o9,view);

                break;
            default:
                activity.glideView(R.drawable.oready,view);
                break;
        }
    }

}
