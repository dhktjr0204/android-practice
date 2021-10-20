package org.techtown.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class PaintBoard extends View {
    Paint paint;
    Canvas mCanvas;
    Bitmap mBitmap;

    int lastX;
    int lastY;

    public PaintBoard(Context context) {
        super(context);
        init(context);
    }

    public PaintBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        paint=new Paint();
        paint.setColor(Color.BLACK);
        lastX=-1;
        lastY=-1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas();
        mCanvas.setBitmap(mBitmap);

        mCanvas.drawColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBitmap!=null){
            canvas.drawBitmap(mBitmap,0,0,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x=(int) event.getX();
        int y=(int) event.getY();

        switch(action){
            case MotionEvent.ACTION_UP:
                lastX=-1;
                lastY=-1;
                break;
            case MotionEvent.ACTION_DOWN:
                if(lastX!=-1){
                    if(x!=lastX||y!=lastY){
                        mCanvas.drawLine(lastX,lastY,x,y,paint);//이전에 터치했던 점과 현재 터치점을 이어줌
                    }
                }
                lastX=x;
                lastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(lastX!=-1){
                    mCanvas.drawLine(lastX,lastY,x,y,paint);
                }
                lastX=x;
                lastY=y;
                break;
        }
        invalidate();//화면 전체가 다시 그려짐
        return true;
    }
}
