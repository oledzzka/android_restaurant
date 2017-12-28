package com.example.oleg.restaurants.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.oleg.restaurants.R;

import java.util.Locale;

/**
 * Created by oleg on 23.12.17.
 */

public class ImageWIthRatingView extends android.support.v7.widget.AppCompatImageView {

    private static final float MAX_RATIG = 5f;
    private static final String MAX_RATING_STRING = "5.0";
    private final Paint textRatingPaint;
    private final RectF backgroundRatingRect;
    private final float cornerRadius;
    private Paint backgroundRatingPaint;
    private String displayedRating;
    private int paddingRating;

    public ImageWIthRatingView(Context context) {
        this(context, null);
    }

    public ImageWIthRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundRatingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textRatingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        backgroundRatingRect = new RectF();

        final TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.ImageWIthRatingView, 0, 0);

        // Get the background color from the attributes.
        final int backgroundColor = typedArray.getColor(R.styleable.ImageWIthRatingView_backgroundRatingColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark));
        backgroundRatingPaint.setColor(backgroundColor);

        final int textColor = typedArray.getColor(R.styleable.ImageWIthRatingView_android_textColor,
                ContextCompat.getColor(context, R.color.textOnPrimary));
        final float textSize = Math.round(
                typedArray.getDimensionPixelSize(R.styleable.ImageWIthRatingView_android_textSize,
                        Math.round(40f * getResources().getDisplayMetrics().scaledDensity)));
        textRatingPaint.setColor(textColor);
        textRatingPaint.setTextSize(textSize);

        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.ImageWIthRatingView_cornerRadius,
                Math.round(2f * getResources().getDisplayMetrics().density));

        paddingRating = typedArray.getDimensionPixelSize(R.styleable.ImageWIthRatingView_cornerRadius,
                Math.round(5f * getResources().getDisplayMetrics().density));

        typedArray.recycle();
        setRating(0);
    }

    public void setRating(float rating) {
        rating = Math.min(rating, MAX_RATIG);
        this.displayedRating = String.format(Locale.getDefault(), "%.1f", rating);
        invalidate();
    }

    public void setColorRating(int colourId) {
        backgroundRatingPaint.setColor(colourId);
        invalidate();
    }

    public void setPaddingRating(int paddingRating) {
        this.paddingRating = paddingRating;
        invalidate();
    }

    private int reconcileSize(int contentSize, int measureSpec) {
        final int mode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                if (contentSize < specSize) {
                    return contentSize;
                } else {
                    return specSize;
                }
            case MeasureSpec.UNSPECIFIED:
            default:
                return contentSize;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final Paint.FontMetrics fontMetrics = textRatingPaint.getFontMetrics();

        final float maxTextWidth = textRatingPaint.measureText(MAX_RATING_STRING);
        final float maxTextHeight = -fontMetrics.top + fontMetrics.bottom;


        final int desiredWidthRating = Math.round(maxTextWidth + getPaddingLeft() + getPaddingRight());
        final int desiredHeightRating = Math.round(maxTextHeight * 2f + getPaddingTop() +
                getPaddingBottom());

        final int desiredWidth = Math.max(desiredWidthRating, getMaxWidth());
        final int desiredHeight = Math.max(desiredHeightRating, getMaxHeight());

        final int measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec);
        final int measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int getTextHeight() {
        Rect rect = new Rect();
        textRatingPaint.getTextBounds(displayedRating, 0, displayedRating.length(), rect);
        return rect.height();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int canvasWidth = canvas.getWidth();
        final int canvasHeight = canvas.getHeight();

        final float textWidth = textRatingPaint.measureText(displayedRating);
        final float textHeight = getTextHeight();
        final int widthRating = Math.round(textWidth + paddingRating * 2);
        final int heightRating = Math.round(textHeight + paddingRating * 2);

        backgroundRatingRect.set(canvasWidth - widthRating, canvasHeight - heightRating, canvasWidth, canvasHeight);
        canvas.drawRoundRect(backgroundRatingRect, cornerRadius, cornerRadius, backgroundRatingPaint);

        final float textX = Math.round(canvasWidth - widthRating * 0.5f - textWidth * 0.5f);
        final float textY = Math.round(canvasHeight - heightRating * 0.5f + textHeight * 0.4f);
        canvas.drawText(displayedRating, textX, textY, textRatingPaint);
    }

}