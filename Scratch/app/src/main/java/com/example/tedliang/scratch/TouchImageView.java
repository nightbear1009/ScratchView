package com.example.tedliang.scratch;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tedliang on 2014/10/23.
 */
public class TouchImageView extends View {
    private Map<Point, Object> map = new HashMap<Point, Object>();
    private int StokeWidth =40;
    @TargetApi(Build.VERSION_CODES.L)
    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TouchImageView(Context context) {
        super(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Path mPath = new Path();
    Paint mPaint = new Paint();

    Canvas mCanvas ;
    Bitmap mBitmap;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private float mStartX,mStartY;
    private Path circlePath = new Path();
    Paint mCirclePaint = new Paint();

    private int Maximum;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        mCanvas.drawRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight(), paint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(StokeWidth);

        mPaint.setColor(getResources().getColor(android.R.color.transparent));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(getResources().getColor(android.R.color.transparent));
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Maximum = mCanvas.getWidth() * mCanvas.getHeight();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, new Paint());
    }



    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        mStartX = x;
        mStartY = y;


    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.lineTo(mX, mY);
            mX = x;
            mY = y;

        }


    }
    private void touch_up(float x, float y) {
        if(mStartX == x && mStartY == y){
            circlePath.addCircle(x,y,StokeWidth-10,Path.Direction.CW);
            mCanvas.drawPath(circlePath,  mCirclePaint);

        }else {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath,  mPaint);
        }

        getDrawingCache(true);
        int count =0;
        for(int i=0;i<mCanvas.getWidth();i+=10){
            for(int j=0;j<mCanvas.getHeight();j+=10){
                if(mBitmap.getPixel(i,j) == Color.TRANSPARENT){
                    count ++;
                }
            }
        }

        Log.d("Ted","count "+count);
        Log.d("Ted","count "+mCanvas.getHeight()*mCanvas.getWidth()/100);
        Log.d("Ted","Percent "+(float)count/(float)(mCanvas.getHeight()*mCanvas.getWidth()/100)*100+"%");
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x,y);
                invalidate();
                break;
        }
        return true;
    }

}
