package com.sig.scrawl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private ImageView mImageView = null;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    private Paint mPaint = null;
    private float dX = 0.0f;
    private float dY = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mImageView = (ImageView) this.findViewById(R.id.image_view);
        mImageView.setOnTouchListener(mOnTouchListener);
        this.findViewById(R.id.btn_save).setOnClickListener(mOnClickListener);
        this.findViewById(R.id.btn_clear).setOnClickListener(mOnClickListener);
    }

    /**
     * 屏幕触摸监听器
     */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initCanvas();
                    dX = event.getX();
                    dY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float mX = event.getX();
                    float mY = event.getY();
                    mCanvas.drawLine(dX, dY, mX, mY, mPaint);
                    mImageView.invalidate();
                    dX = mX;
                    dY = mY;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * 按钮点击监听器
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_save:
                    save();
                    break;
                case R.id.btn_clear:
                    clear();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化画板
     */
    private void initCanvas() {
        if (mCanvas == null) {
            mBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(10.0f);
            mImageView.setImageBitmap(mBitmap);
        }
    }

    /**
     * 保存图片
     */
    private void save() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "找不到SD卡！", Toast.LENGTH_SHORT).show();
            return;
        }
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, "scrawl.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean mark = mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (mark)
                Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "保存失败！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }

    /**
     * 清除图片
     */
    private void clear() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mImageView.setImageBitmap(mBitmap);
    }
}
