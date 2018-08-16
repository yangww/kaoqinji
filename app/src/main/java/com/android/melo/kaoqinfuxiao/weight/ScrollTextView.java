package com.android.melo.kaoqinfuxiao.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.melo.kaoqinfuxiao.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by melo on 2018/4/26.
 * 自动滚动得textview
 */
public class ScrollTextView extends ScrollView {
    public String text;//TextView文字输入
    private int num;//初始值
    private TextView mTextView;
    private int scrollDelay = 50;//定时频率
    private int scrollDistance = 1;//滚动距离
    private boolean mClick = true;//是否可以点击 true|false 不能点击|可以点击
    private int mTextSize = R.dimen.font_size;//默认字体大小
    private int color = R.color.color_text_desc;//设置文字的颜色

    public void setColor(int color) {
        this.color = color;
    }

    private int getColor() {

        return color;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setClick(boolean click) {
        mClick = !click;
    }

    public boolean getClick() {
        return mClick;
    }

    public int getScrollDelay() {
        return scrollDelay;
    }

    public void setScrollDelay(int scrollDelay) {
        this.scrollDelay = scrollDelay;
    }

    public int getScrollDistance() {
        return scrollDistance;
    }

    public void setScrollDistance(int scrollDistance) {
        this.scrollDistance = scrollDistance;
    }

    Timer timer = new Timer();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        initView();
    }

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void initView() {
        this.removeAllViews();
        mTextView = new TextView(getContext());
        //这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
        ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(layoutParams);
        this.addView(mTextView);
        mTextView.setTextColor(getResources().getColor(getColor()));
        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);//设置字体横向居中
//        mTextView.setTextSize(24);
        mTextView.setTextSize(getContext().getResources().getDimension(getTextSize()));//设置文字大小
        mTextView.setText(getText());
        this.setVerticalScrollBarEnabled(false);//隐藏滚动条
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //点击事件的拦截
                return getClick();
            }
        });
        mTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //点击事件的拦截
                return getClick();
            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                start();
            }
        }, 0, scrollDelay);
    }


    /**
     * 开始滚动
     *
     * @param
     */
    public void start() {
        num += scrollDistance;
        int off = mTextView.getMeasuredHeight() - this.getHeight();
        if (num >= off) {
            num = 0;
            mTextView.scrollTo(0, 0);
        }
        if (off > 0) {
            mTextView.scrollTo(0, num);
        }
    }


    /*
       取消定时器
        */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // AppUtils.e("jim", "断开链接");
        mTextView = null;
        timer.cancel();
    }


}
