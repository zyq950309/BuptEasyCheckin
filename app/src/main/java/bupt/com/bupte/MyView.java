package bupt.com.bupte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    private Paint paint;
    private float angle,angle1;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public void setangle(float angle,float angle1){
        this.angle=angle;
        this.angle1=angle1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_jiantou);
//        Matrix m = new Matrix();
//        m.preRotate(360-angle,bitmap.getWidth()/2, bitmap.getHeight()/2);
//        m.preRotate(360,bitmap.getWidth()/2, bitmap.getHeight()/2);
//        m.preScale(0.3f,0.3f);
        Matrix m1 = new Matrix();
        m1.preRotate(360-angle+angle1,bitmap.getWidth()/2, bitmap.getHeight()/2);
//        m1.preRotate(-angle,bitmap.getWidth()/2, bitmap.getHeight()/2);
        m1.preScale(0.8f,0.8f);
        Bitmap bitmap21 = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(),m1,true);
//        Bitmap bitmap22 = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(),m,true);
        int left=canvas.getWidth()/2-bitmap21.getWidth()/2;
        int top=canvas.getHeight()/2-bitmap21.getHeight()/2;
        canvas.drawBitmap(bitmap21,left,top,paint);
//        canvas.drawBitmap(bitmap22,0,0,paint);
    }
}
