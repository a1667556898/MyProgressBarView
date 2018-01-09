package com.example.administrator.myspeedprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MySpeedProgressView extends View {
    private final int EMPTY_MESSAGE = 1;

    /*XML中自定义属性*/
    private int levelTextChooseColor;
    private int levelTextUnChooseColor;
    private int levelTextSize;
    private int progressStartColor;
    private int progressEndColor;
    private int progressBgColor;
    private int progressHeight;

    /*代码中需要设置的属性*/
    private Paint textPaint;
    private Paint linePaint;
    private int currentProgress;
    private int totalWidth;
    private int targetProgress;
    private int totalProgress = 100;
    private int currentLevel;//当前等级
    private int levels;//等级总数
    private String[] levelTexts;
    private int animInterval = 10;//时间间隔 默认为10
    int textHeight;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (currentProgress < targetProgress) {
                currentProgress++;
                handler.sendEmptyMessageDelayed(EMPTY_MESSAGE, animInterval);
                invalidate();
            } else if (currentProgress > targetProgress) {
                currentProgress--;
                handler.sendEmptyMessageDelayed(EMPTY_MESSAGE, animInterval);
                invalidate();
            } else {
                handler.removeMessages(EMPTY_MESSAGE);
            }
        }
    };

    public MySpeedProgressView(Context context) {
        super(context);
    }

    public MySpeedProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySpeedProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LevelProgressBar);
        levelTextChooseColor = a.getColor(R.styleable.LevelProgressBar_levelTextChooseColor, Color.BLACK);
        levelTextUnChooseColor = a.getColor(R.styleable.LevelProgressBar_levelTextUnChooseColor, 0x333333);
        levelTextSize = (int) a.getDimension(R.styleable.LevelProgressBar_levelTextSize, dpTopx(15));
        progressStartColor = a.getColor(R.styleable.LevelProgressBar_progressStartColor, 0xCCFFCC);
        progressEndColor = a.getColor(R.styleable.LevelProgressBar_progressEndColor, 0x00FF00);
        progressBgColor = a.getColor(R.styleable.LevelProgressBar_progressBgColor, 0x000000);
        progressHeight = (int) a.getDimension(R.styleable.LevelProgressBar_progressHeight, dpTopx(20));
        a.recycle();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(levelTextSize);
        textPaint.setColor(levelTextUnChooseColor);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(progressBgColor);
    }

    private int dpTopx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //设置等级总数
    public void setLevels(int levels) {
        this.levels = levels;
    }

    //设置当前等级数
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        targetProgress = (int) (currentLevel * 1.0f / levels * 100);
        Log.i("123", "currentProgress:" + currentProgress + "targetProgress" + targetProgress);
        handler.sendEmptyMessageDelayed(EMPTY_MESSAGE, animInterval);
    }

    //设置等级集合
    public void setLevelTexts(String[] texts) {
        this.levelTexts = texts;
    }

    //设置时间间隔
    public void setAnimInterval(int animInterval) {
        this.animInterval = animInterval;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getPaddingTop() + getPaddingBottom() + textHeight + progressHeight + dpTopx(10);

        setMeasuredDimension(width, height);
        this.totalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getPaddingLeft(), getPaddingTop());
        //绘制等级文字
        for (int i = 0; i < levelTexts.length; i++) {
            int textWidth = (int) textPaint.measureText(levelTexts[i]);

            textHeight = (int) (textPaint.descent() - textPaint.ascent());
            textPaint.setColor(levelTextUnChooseColor);
            if (currentProgress == targetProgress && i == currentLevel - 1) {
                textPaint.setColor(levelTextChooseColor);
            }
            canvas.drawText(levelTexts[i], totalWidth / levels * (1 + i) * 1.0f - textWidth, textHeight, textPaint);
        }
        //绘制进度条底部
        linePaint.setColor(progressBgColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStyle(Paint.Style.STROKE);

        linePaint.setStrokeWidth(progressHeight);
        canvas.drawLine(0 + progressHeight / 2, textHeight + dpTopx(10) + progressHeight / 2, totalWidth - progressHeight / 2, textHeight + dpTopx(10) + progressHeight / 2, linePaint);
        //绘制进度条
        int reachedPartEnd = (int) (currentProgress * 1.0f / totalProgress * totalWidth);
        if (reachedPartEnd > 0) {
            linePaint.setStrokeCap(Paint.Cap.ROUND);
            Shader shader = new LinearGradient(0, textHeight + dpTopx(10) + progressHeight / 2, getWidth(), textHeight + dpTopx(10) + progressHeight / 2,
                    progressStartColor, progressEndColor, Shader.TileMode.REPEAT);
            linePaint.setShader(shader);
            canvas.drawLine(progressHeight / 2, textHeight + dpTopx(10) + progressHeight / 2, reachedPartEnd - progressHeight / 2, textHeight + dpTopx(10) + progressHeight / 2, linePaint);
            linePaint.setShader(null);
        }
    }
}
