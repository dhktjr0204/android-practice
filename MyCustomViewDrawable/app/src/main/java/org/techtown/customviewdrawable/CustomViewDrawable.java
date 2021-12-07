package org.techtown.customviewdrawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class CustomViewDrawable extends View {
    ShapeDrawable upperDrawable;
    ShapeDrawable lowerDrawable;

    public CustomViewDrawable(Context context) {
        super(context);
        init(context);
    }

    public CustomViewDrawable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int displayWidth=display.getWidth();
        int displayHeight=display.getHeight();

        Resources resourses=getResources();
        int blackColor=resourses.getColor(R.color.color01);
        int grayColor=resourses.getColor(R.color.color02);
        int darkgrayColor=resourses.getColor(R.color.color03);

        upperDrawable=new ShapeDrawable();
        RectShape rectangle=new RectShape();
        rectangle.resize(displayWidth,displayHeight*2/3);
        upperDrawable.setShape(rectangle);
        upperDrawable.setBounds(0,0,displayWidth,displayHeight*2/3);

        LinearGradient gradient=new LinearGradient(0,0,0,displayHeight*2/3,grayColor,blackColor, Shader.TileMode.CLAMP);
        Paint paint=upperDrawable.getPaint();
        paint.setShader(gradient);

        lowerDrawable=new ShapeDrawable();
        RectShape rectangle2=new RectShape();
        rectangle2.resize(displayWidth,displayHeight*1/3);
        lowerDrawable.setShape(rectangle2);
        lowerDrawable.setBounds(0,displayHeight*2/3,displayWidth,displayHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        upperDrawable.draw(canvas);
        lowerDrawable.draw(canvas);
    }
}
