package com.iwintrue.customercamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.ImageView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
    }

    private void initView() {
        ImageView iv_show_pic = (ImageView) findViewById(R.id.iv_show_pic);
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpeg");
        //获取水印图片，如果图片过大，应对图片采样
        Bitmap waterBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.water2);
        Bitmap bitmap2 = watermarkBitmap(bitmap,waterBitmap,"ZHOUKAI");
        iv_show_pic.setImageBitmap(bitmap2);
    }

    // 加水印 也可以加文字
    public  Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
                                         String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint=new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
//            paint.setAlpha(80);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
        }
        //加入文字
        if(title!=null)
        {
            String familyName ="黑体";
            Typeface font = Typeface.create(familyName,Typeface.NORMAL);
            TextPaint textPaint=new TextPaint();
            textPaint.setColor(Color.RED);
            textPaint.setTypeface(font);
            textPaint.setTextSize(8);
            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title,textPaint,w, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
            layout.draw(cv);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
}
