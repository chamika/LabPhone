package lk.ac.mrt.labphone.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import lk.ac.mrt.labphone.R;

/**
 * Created by chamika on 10/16/16.
 */

public class LevelTwoDimen extends View {

    private Drawable imageBubble;
    private Drawable imageBackground;
    private double levelX;
    private double levelY;

    public LevelTwoDimen(Context context) {
        super(context);
    }

    public LevelTwoDimen(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    public LevelTwoDimen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LevelTwoDimen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

            modBounds.top = (int) Math.round ((canvasBounds.height() * levelY) - (imageHeight / 2));
            modBounds.left = (int) Math.round((canvasBounds.width() * levelX) - (imageWidth / 2));

            modBounds.top += canvasBounds.centerY();
            modBounds.left += canvasBounds.centerX();

            modBounds.bottom = modBounds.top + imageHeight;
            modBounds.right = modBounds.left + imageWidth;

            imageBubble.setBounds(modBounds);

            imageBubble.draw(canvas);

            //TODO hide from outside the level circle
//            double distance = Math.sqrt((modBounds.centerX() - canvasBounds.centerX()) ^ 2 + (modBounds.centerY() - canvasBounds.centerY()) ^ 2);
//            if(distance < getWidth()/2) {
//                imageBubble.draw(canvas);
//            }
        }
    }


    public void setLevel(double levelX,double levelY) {
        this.levelX = levelX;
        this.levelY = levelY;
        invalidate();
//        requestLayout();
    }
}
