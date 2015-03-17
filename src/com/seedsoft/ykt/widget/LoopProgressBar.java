package com.seedsoft.ykt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 *自定义循环进度条 
 */
public class LoopProgressBar extends View {

	private Paint paint;

	private int measureWidth, measureHeight;

	private int width = 60;

	private int pace;

	private int initial;

	private int end;

	private boolean isClose = false;

	public LoopProgressBar(Context context) {
		super(context);
		init();
	}

	public LoopProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LoopProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		measureWidth = getMeasuredWidth();
		measureHeight = getMeasuredHeight();
		width = measureWidth / 4;
		pace = measureWidth / 20;
	}

	public void setColor(int color) {
		paint.setColor(color);
		end = width;
	}

	public void stopBar() {
		isClose = false;
		initial = 0;
		end = 0;
	}

	public void start() {
		isClose = true;
		new Thread() {
			public void run() {
				initial += pace;
				end = initial + width;
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (end >= measureWidth) {
					initial = 0;
					end = width;
				}
				postInvalidate();
			};
		}.start();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isClose)
			start();
		Rect r = new Rect(initial, 0, end, measureHeight);
		canvas.drawRect(r, paint);
	}
}
