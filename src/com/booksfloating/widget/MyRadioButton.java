package com.booksfloating.widget;

import com.xd.booksfloating.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class MyRadioButton extends RadioButton {
	public static final String TAG = "MyRadioButton";
	public Drawable drawable;
	
	public MyRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
		drawable = a.getDrawable(R.styleable.MyRadioButton_image);
	}
	public void seDrawable(Drawable drawable) {
		this.drawable = drawable;
//		drawable.setCallback(this);
	}
	// Drawable转换成Bitmap
	private Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap image = drawable2Bitmap(drawable);
		if (image != null) {
			Paint pt = new Paint();
			pt.setARGB(255, 66, 66, 66);
			// 消除锯齿
			pt.setAntiAlias(true);
			// 居中显示图片
			int imageX = (int) (this.getWidth() - image.getWidth()) / 2;

			canvas.drawBitmap(image, imageX, 2, pt);
			pt.setARGB(255, 255, 255, 255);
		}
	}
}

