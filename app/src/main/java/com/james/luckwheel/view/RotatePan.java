package com.james.luckwheel.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


import com.james.luckwheel.R;
import com.james.luckwheel.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RotatePan extends View {

    private Context context;

    private int panNum = 6;

    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int InitAngle = 0;
    private int radius = 0;
    private int verPanRadius;
    private int diffRadius;
    String[] items = new String[6];

    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private Integer[] images;
    private String[] strs;

    private List<Bitmap> bitmaps = new ArrayList<>();
    private GestureDetectorCompat mDetector;
    private ScrollerCompat scroller;

    public RotatePan(Context context) {
        this(context, null);
    }

    public RotatePan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatePan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        mDetector = new GestureDetectorCompat(context, new RotatePanGestureListener());
        scroller = ScrollerCompat.create(context);

        checkPanState(context, attrs);
        InitAngle = 360 / panNum;
        verPanRadius = 360 / panNum;
        diffRadius = verPanRadius / 2;
        dPaint.setColor(Color.rgb(255, 133, 132));
        sPaint.setColor(Color.rgb(254, 104, 105));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Util.dip2px(context, 16));
        setClickable(true);

        for (int i = 0; i < panNum; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[i]);
            bitmaps.add(bitmap);
        }
    }

    private void checkPanState(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.luckpan);
        panNum = typedArray.getInteger(R.styleable.luckpan_pannum, 0);
        if (360 % panNum != 0)
            throw new RuntimeException("can't split pan for all icon.");
        int nameArray = typedArray.getResourceId(R.styleable.luckpan_names, -1);
        if (nameArray == -1) throw new RuntimeException("Can't find pan name.");
        strs = context.getResources().getStringArray(nameArray);
        int iconArray = typedArray.getResourceId(R.styleable.luckpan_icons, -1);
        if (iconArray == -1) throw new RuntimeException("Can't find pan icon.");

        String[] iconStrs = context.getResources().getStringArray(iconArray);   //get ArrayList
        List<Integer> iconLists = new ArrayList<>();
        for (int i = 0; i < iconStrs.length; i++) {
            iconLists.add(context.getResources().getIdentifier(iconStrs[i], "mipmap", context.getPackageName()));
        }

        images = iconLists.toArray(new Integer[iconLists.size()]);
        //Log.e("images", Arrays.toString(images));
        typedArray.recycle();
        if (strs == null || images == null)
            throw new RuntimeException("Can't find string or icon resources.");
        if (strs.length != panNum || images.length != panNum)
            throw new RuntimeException("The string length or icon length  isn't equals panNum.");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //wrap_content value
        int mHeight = Util.dip2px(context, 300);
        int mWidth = Util.dip2px(context, 300);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int MinValue = Math.min(width, height);

        radius = MinValue / 2;

        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), width, height);

        int angle = (panNum % 4 == 0) ? InitAngle : InitAngle - diffRadius;
        //Log.e("angle", String.valueOf(angle));

        for (int i = 0; i < panNum; i++) {
            if (i % 2 == 0) {
                canvas.drawArc(rectF, angle, verPanRadius, true, dPaint);
            } else {
                canvas.drawArc(rectF, angle, verPanRadius, true, sPaint);
            }
            angle += verPanRadius;
        }

        for (int i = 0; i < panNum; i++) { //Draw icon
            drawIcon(width / 2, height / 2, radius, (panNum % 4 == 0) ? InitAngle + diffRadius : InitAngle, i, canvas);
            InitAngle += verPanRadius;
        }

        for (int i = 0; i < panNum; i++) { //Draw Text
            drawText((panNum % 4 == 0) ? InitAngle + diffRadius + (diffRadius / 2) : InitAngle + diffRadius, strs[i], 2 * radius, textPaint, canvas, rectF);
            InitAngle += verPanRadius;
        }
    }

    private void drawText(float startAngle, String string, int mRadius, Paint mTextPaint, Canvas mCanvas, RectF mRange) {
        Path path = new Path();
        path.addArc(mRange, startAngle, verPanRadius);
        float textWidth = mTextPaint.measureText(string);

        float hOffset = (float) (mRadius * Math.PI / 6 / 2 - textWidth / 2);
        float vOffset = mRadius / 2 / 4;
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

    private void drawIcon(int xx, int yy, int mRadius, float startAngle, int i, Canvas mCanvas) {

        int imgWidth = mRadius / 4;

        float angle = (float) Math.toRadians(verPanRadius + startAngle);

        float x = (float) (xx + mRadius / 2 * Math.cos(angle));
        float y = (float) (yy + mRadius / 2 * Math.sin(angle));
        RectF rect = new RectF(x - imgWidth * 3 / 4, y - imgWidth * 3 / 4, x + imgWidth
                * 3 / 4, y + imgWidth * 3 / 4);

        Bitmap bitmap = bitmaps.get(i);

        mCanvas.drawBitmap(bitmap, null, rect, null);
    }


    public void setImages(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        this.invalidate();
    }

    public void setStr(String... strs) {
        this.strs = strs;
        this.invalidate();
    }

    private static final long ONE_WHEEL_TIME = 800;

    public void startRotate(int pos) {

        int lap = (int) (Math.random() * 12) + 4;

        int angle = 0;
        if (pos < 0) {
            angle = (int) (Math.random() * 360);
        } else {
            int initPos = queryPosition();
            if (pos > initPos) {
                angle = (pos - initPos) * verPanRadius;
                lap -= 1;
                angle = 360 - angle;
            } else if (pos < initPos) {
                angle = (initPos - pos) * verPanRadius;
            } else {
                //nothing to do.
            }
        }


        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        int DesRotate = increaseDegree + InitAngle;

        int offRotate = DesRotate % 360 % verPanRadius;
        DesRotate -= offRotate;
        DesRotate += diffRadius;

        ValueAnimator animtor = ValueAnimator.ofInt(InitAngle, DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updateValue = (int) animation.getAnimatedValue();
                InitAngle = (updateValue % 360 + 360) % 360;
                ViewCompat.postInvalidateOnAnimation(RotatePan.this);
            }
        });

        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (l != null)
                    l.endAnimation(queryPosition());
            }
        });
        animtor.start();
    }


    private int queryPosition() {
        InitAngle = (InitAngle % 360 + 360) % 360;
        int pos = InitAngle / verPanRadius;
        return calcumAngle(pos);
    }

    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= panNum / 2) {
            pos = panNum / 2 - pos;
        } else {
            pos = (panNum - pos) + panNum / 2;
        }
        return pos;
    }


    public interface AnimationEndListener {
        void endAnimation(int position);
    }

    private AnimationEndListener l;

    public void setAnimationEndListener(AnimationEndListener l) {
        this.l = l;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean consume = mDetector.onTouchEvent(event);
        if (consume) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }

        return super.onTouchEvent(event);
    }


    public void setRotate(int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        InitAngle = rotation;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            setRotate(scroller.getCurrY());
        }

        super.computeScroll();
    }

    private class RotatePanGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float centerX = (RotatePan.this.getLeft() + RotatePan.this.getRight()) * 0.5f;
            float centerY = (RotatePan.this.getTop() + RotatePan.this.getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(distanceX, distanceY, e2.getX() - centerX, e2.getY() -
                    centerY);
            int rotate = InitAngle - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE;
            setRotate(rotate);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float centerX = (RotatePan.this.getLeft() + RotatePan.this.getRight()) * 0.5f;
            float centerY = (RotatePan.this.getTop() + RotatePan.this.getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(velocityX, velocityY, e2.getX() - centerX, e2.getY() -
                    centerY);

            scroller.abortAnimation();
            scroller.fling(0, InitAngle, 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                    0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return true;
        }
    }

    private float vectorToScalarScroll(float dx, float dy, float x, float y) {

        float l = (float) Math.sqrt(dx * dx + dy * dy);

        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }
}
