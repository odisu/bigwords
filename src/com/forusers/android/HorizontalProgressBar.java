/**
 * 
 */
package com.forusers.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.forusers.android.bigwords.R;

/**
 * A widget that gives feedback that a tilt was registered.
 * 
 * @author scottkirkwood
 */
public class HorizontalProgressBar extends View {

	private int backgroundColor;
	private int foregroundColor;
	private int textColor;
	private Paint paint;
	private int max;
	private int min;
	private int pos;
	private float textY;
	private float textX;
	private String formatText;

	public HorizontalProgressBar(Context context) {
		super(context);
		initProgressBar();
	}

	public HorizontalProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initProgressBar();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBar);

        setTextColor(a.getColor(R.styleable.HorizontalProgressBar_textColor, 0xFF000000));

        setBackgroundColor(a.getColor(R.styleable.HorizontalProgressBar_backgroundColor, 0xFF000000));

        setForegroundColor(a.getColor(R.styleable.HorizontalProgressBar_foregroundColor, 0xFF000000));

        String formatText = a.getString(R.styleable.HorizontalProgressBar_textFormat);
        if (formatText != null) {
        	setFormatText(formatText);
        }

        setMin(a.getInt(R.styleable.HorizontalProgressBar_min, 0));
        
        int newMax = a.getInt(R.styleable.HorizontalProgressBar_max, -1);
        if (newMax != -1) {
        	setMax(newMax);
        }
        
        int textSize = a.getDimensionPixelOffset(R.styleable.HorizontalProgressBar_textSize, 0);
        if (textSize > 0) {
            setTextSize(textSize);
        }

        a.recycle();
	}

	private void setFormatText(String newFormatText) {
		formatText = newFormatText;
		requestLayout();
		invalidate();
	}

	private void setTextSize(int textSize) {
		paint.setTextSize(textSize);
		requestLayout();
		invalidate();
	}

	private void setTextColor(int color) {
		paint.setColor(color);
		invalidate();		
	}
	
	private void initProgressBar() {
		paint = new Paint();
		paint.setTextSize(12);
		paint.setColor(0xFF668800);
		paint.setStyle(Paint.Style.FILL);
		setPadding(2, 2, 2, 2);
		min = 0;
		max = 100;
		backgroundColor = 0xFF668800;
		foregroundColor = 0xFF9977FF;
		textColor = 0xFFFFFFFF;
		pos = 0;
		textX = 0;
		textY = 0;
		formatText = "%d%% done";
	}

	/**
	 * Try not to call too often since it calls invalidate.
	 * 
	 * @param pos
	 */
	public void setPosition(int newPos) {
		if (newPos > max) {
			newPos = max;
		}
		if (newPos < min) {
			newPos = min;
		}
		pos = newPos;
		invalidate();
	}

	public void setMin(int newMin) {
		if (newMin > max) {
			newMin = 0;
		}
		min = newMin;
		
		// May move position if it's out of bounds.
		setPosition(pos);
	}
	
	public void setMax(int newMax) {
		if (newMax > min) {
			newMax = min + 1;
		}
		max = newMax;
		
		// May move position if it's out of bounds.
		setPosition(pos);
	}
	
	public String getText() {
		return String.format(formatText, pos);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(backgroundColor);
		
        paint.setColor(foregroundColor);        
        int truepos = (int) (((float) pos / (max - min)) * getWidth());
        canvas.drawRect(0, 0, truepos, getHeight(), paint);		
        
        paint.setColor(textColor);
        String text = getText();
        canvas.drawText(text, textX, textY, paint);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int ascent = (int) paint.ascent();
        int descent = (int) paint.descent();
        
        if (specMode == MeasureSpec.EXACTLY) {
        	result = specSize;
        } else {
		    int heightText = descent- ascent + getPaddingTop() + getPaddingBottom() + 5;
		    result = Math.min(specSize, heightText);
        }
	    textX = (result - ascent) / 2;
        return result;
	}
	
	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        
		String text = getText();
		int textWidth = (int) paint.measureText(text);
    	if (specMode == MeasureSpec.EXACTLY) {
    		result = specSize;
    	} else {
	    	int widthText = textWidth + getPaddingLeft() + getPaddingRight() + 20;
	    	result = Math.min(widthText, specSize);
    	}
    	textX = (result - textWidth) / 2;
    	return result;
 	}
	
	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		backgroundColor = color;
	}
	
	private void setForegroundColor(int color) {
		foregroundColor = color;
		invalidate();
	}


}
