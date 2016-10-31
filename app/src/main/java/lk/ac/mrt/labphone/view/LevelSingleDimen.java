package lk.ac.mrt.labphone.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import lk.ac.mrt.labphone.R;

/**
 * Created by chamika on 10/16/16.
 */

public class LevelSingleDimen extends View {

    private Drawable imageBubble;
    private Drawable imageBackground;
    private double level;
    private int orientation;//0=vertial, 1= horizontal

    public LevelSingleDimen(Context context) {
        super(context);
    }

    public LevelSingleDimen(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    public LevelSingleDimen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LevelSingleDimen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttributes(context, attrs);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LevelDimen,
                0, 0);

        try {
            int resBubble = a.getResourceId(R.styleable.LevelDimen_bubble_image, 0);
            int resBackground = a.getResourceId(R.styleable.LevelDimen_background_image, 0);
            orientation = a.getInteger(R.styleable.LevelDimen_orientation, 0);

            if (resBubble != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageBubble = context.getDrawable(resBubble);
                    imageBackground = context.getDrawable(resBackground);
                } else {
                    imageBubble = context.getResources().getDrawable(resBubble);
                    imageBackground = context.getResources().getDrawable(resBackground);
                }
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (imageBackground != null) {
            Rect imageBounds = canvas.getClipBounds();
            imageBackground.setBounds(imageBounds);
            imageBackground.draw(canvas);
        }
        if (imageBubble != null) {
            int imageWidth = imageBubble.getIntrinsicWidth();
            int imageHeight = imageBubble.getIntrinsicHeight();
            Rect canvasBounds = canvas.getClipBounds();
            Rect modBounds = new Rect();

            if(orientation == 0) {//vertical
                modBounds.top = (int) Math.round ((canvasBounds.height() * level) - (imageHeight / 2));
            }else{
                modBounds.top = canvasBounds.centerY() - (imageHeight*3/2 );
            }
            if(orientation == 0) {
                modBounds.left = canvasBounds.centerX() - (imageWidth*3/2 );
            }else{
                modBounds.left = (int) Math.round((canvasBounds.width() * level) - (imageWidth / 2));
            }
            modBounds.top += canvasBounds.centerY();
            modBounds.left += canvasBounds.centerX();

            modBounds.bottom = modBounds.top + imageHeight;
            modBounds.right = modBounds.left + imageWidth;

            imageBubble.setBounds(modBounds);
            imageBubble.draw(canvas);
        }
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
        invalidate();
//        requestLayout();
    }
}
