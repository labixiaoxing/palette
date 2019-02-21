package xiaoxing.com.paletteview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class PaletteView extends View {

    float preX;
    float preY;
    private Path path;
    private Paint paint = null;
    //定义一个内存中图片，将他作为缓冲区
    Bitmap cacheBitmap = null;
    //定义缓冲区Cache的Canvas对象
    Canvas cacheCanvas = null;

    public PaletteView(Context context) {
        this(context,null);
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //创建一个与该VIew相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        //创建缓冲区Cache的Canvas对象
        cacheCanvas = new Canvas();
        path = new Path();
        //设置cacheCanvas将会绘制到内存的bitmap上
        cacheCanvas.setBitmap(cacheBitmap);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        //将cacheBitmap绘制到该View
        canvas.drawBitmap(cacheBitmap,0,0,p);
        //canvas.drawPath(path,paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取拖动时间的发生位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(preX,preY,x,y);
                preX = x;
                preY = y;
                cacheCanvas.drawPath(path,paint);
                break;
            case MotionEvent.ACTION_UP:
                //这是是调用了cacheBitmap的Canvas在绘制
                cacheCanvas.drawPath(path,paint);
                path.reset();
                break;
        }
        invalidate();//在UI线程刷新VIew
        return true;
    }
}
