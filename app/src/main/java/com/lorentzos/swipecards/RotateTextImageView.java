package com.lorentzos.swipecards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.math.BigDecimal;

public class RotateTextImageView extends ImageView {
    PaintFlagsDrawFilter pfdf;
    Paint paint;
    Matrix matrix;
    Bitmap bitmap;
    int index = -1;
    private int oriHeight;
    private int oriWidth;
    private int newHeight;
    private int newWidth;
    private int angle = 5;
    protected Path path = new Path();
    private float[] f = new float[8];
    private int shawHeight = 20;
    private int borderSize = 8;
    Bitmap oriBitmap;
    private String text = "";

    public RotateTextImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCanvasInfo();
    }

    public RotateTextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCanvasInfo();
    }

    public RotateTextImageView(Context context) {
        super(context);
        initCanvasInfo();
    }

    /**
     * 初始化Paint
     */
    protected void initCanvasInfo() {
        pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setAntiAlias(true);
        matrix = new Matrix();
        matrix.setRotate(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap==null)
            return;
        paint.reset();
        // 消除锯齿
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.setDrawFilter(pfdf);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        newHeight = bitmap.getHeight();
        newWidth = bitmap.getWidth();
        calculatePoints();
        // 添加阴影
        path.reset();
        path.moveTo(f[0], f[1]);
        path.lineTo(f[2], f[3]);
        path.lineTo(f[4], f[5]);
        path.lineTo(f[6], f[7]);
        path.close();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#96ffffff"));
        canvas.drawPath(path, paint);
        // 添加字符
        if (text != null && !text.equals("")) {
            path.reset();
            paint.setTextSize(18);
            float width = paint.measureText(text);
            path.moveTo((f[0] + f[2]) / 2, (f[1] + f[3]) / 2);
            path.lineTo((f[4] + f[6]) / 2, (f[5] + f[7]) / 2);
            paint.setColor(Color.parseColor("#2b2b2b"));
            canvas.drawTextOnPath(text, path, (oriWidth - width) / 2, 3, paint);
        }
        layout(0, 0, newWidth, newHeight);
    }

    /**
     * 计算坐标值
     */
    private void calculatePoints() {
        double a = angle * Math.PI / 180;
        BigDecimal height = new BigDecimal(oriHeight);
        BigDecimal width = new BigDecimal(oriWidth);
        BigDecimal cos = new BigDecimal(Math.cos(a));
        BigDecimal tan = new BigDecimal(Math.tan(a));
        f[0] = 0;
        f[1] = height.multiply(cos).floatValue();
        f[2] = tan.multiply(new BigDecimal(shawHeight)).floatValue();
        f[3] = (new BigDecimal(f[1])).subtract(new BigDecimal(shawHeight))
                .floatValue();
        f[4] = width.multiply(cos).add(new BigDecimal(f[2])).floatValue();
        f[5] = new BigDecimal(newHeight - shawHeight).floatValue();
        f[6] = width.multiply(cos).floatValue();
        f[7] = new BigDecimal(newHeight).floatValue();
    }
    /**
     * 设置图片
     * 
     * @param bmp
     */
    public void setBitmap(Bitmap bmp) {
        oriBitmap = bmp;
        matrix.reset();
        matrix.setRotate(angle);
        Bitmap bitmapF = addFrame(bmp);
        oriHeight = bitmapF.getHeight();
        oriWidth = bitmapF.getWidth();
        bitmap = Bitmap.createBitmap(bitmapF, 0, 0, bitmapF.getWidth(),
                bitmapF.getHeight(), matrix, true);
        postInvalidate();
    }

    /**
     * 旋转角度
     * 
     * @param angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        setBitmap(oriBitmap);
    }

    /**
     * 设置底部阴影高度
     * 
     * @param shawHeight
     */
    public void setShawHeight(int shawHeight) {
        this.shawHeight = shawHeight;
        postInvalidate();
    }

    /**
     * 生成添加了白色边缘的图
     * 
     * @param bmp
     * @return
     */
    protected Bitmap addFrame(Bitmap bmp) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize
                * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    /**
     * 设置字符串
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    /**
     * 获取字体高度
     */
    protected int getFontHeight() {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }
}