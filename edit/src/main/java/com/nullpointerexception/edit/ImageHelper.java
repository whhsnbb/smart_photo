package com.nullpointerexception.edit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageHelper {

    public static Bitmap handleImageEffect(Bitmap bm, float hue,float saturation,float lum){


        ColorMatrix hueMatrix = new ColorMatrix();
        //第一个参数，系统分别使用0、1、2来代表Red、Green、Blue三种颜色的处理；而第二个参数，就是需要处理的值
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);

        //设置颜色的饱和度
        ColorMatrix saturationMatrix = new ColorMatrix();
        //saturation参数即代表设置颜色的饱和度的值，当饱和度为0时，图像就变成灰度图像了
        saturationMatrix.setSaturation(saturation);


        //设置颜色的亮度
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        //将矩阵的作用效果混合，从而叠加处理效果
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));

        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bm, 0, 0 ,paint);
        return bitmap;
    }

}
