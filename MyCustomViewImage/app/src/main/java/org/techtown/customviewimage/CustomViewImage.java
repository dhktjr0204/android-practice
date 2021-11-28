package org.techtown.customviewimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomViewImage extends View {
    Paint paint;
    Bitmap cacheBitmap;
    Canvas cacheCanvas;
    public CustomViewImage(Context context) {
        super(context);
        init(context);
    }

    public CustomViewImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        paint=new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createCacheBitmap(w,h);
        testDrawing();
    }

    public void createCacheBitmap(int w,int h){
        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//비트맵에다가 그리는 것임
        cacheCanvas=new Canvas();//캐쉬라는 말은 메모리에 그릴 것이라는 말
        cacheCanvas.setBitmap(cacheBitmap);//메모리에다가 미리 그려놓는다

    }

    public void testDrawing(){
        Bitmap srcImg = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        cacheCanvas.drawBitmap(srcImg,100,100,paint);

        Matrix matrix=new Matrix();
        matrix.setScale(1,-1);
        Bitmap inverseBitmap=Bitmap.createBitmap(srcImg,0,0,srcImg.getWidth(),srcImg.getHeight(),matrix,false);
        cacheCanvas.drawBitmap(inverseBitmap,300,300,paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(cacheBitmap!=null){
            canvas.drawBitmap(cacheBitmap,0,0,null);
            //메모리에 미리 그려놓은 비트맵을 0,0에 뿌려준다.
        }
    }
}
