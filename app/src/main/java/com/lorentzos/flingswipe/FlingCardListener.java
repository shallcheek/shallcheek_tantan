//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lorentzos.flingswipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.lorentzos.flingswipe.LinearRegression;

public class FlingCardListener implements OnTouchListener {
    private static final String TAG = FlingCardListener.class.getSimpleName();
    private static final int INVALID_POINTER_ID = -1;
    private final float objectX;
    private final float objectY;
    private final int objectH;
    private final int objectW;
    private final int parentWidth;
    private final FlingCardListener.FlingListener mFlingListener;
    private final Object dataObject;
    private final float halfWidth;
    private float BASE_ROTATION_DEGREES;
    private float aPosX;
    private float aPosY;
    private float aDownTouchX;
    private float aDownTouchY;
    private int mActivePointerId;
    private View frame;
    private final int TOUCH_ABOVE;
    private final int TOUCH_BELOW;
    private int touchPosition;
    private final Object obj;
    private boolean isAnimationRunning;
    private float MAX_COS;

    public FlingCardListener(View frame, Object itemAtPosition, FlingCardListener.FlingListener flingListener) {
        this(frame, itemAtPosition, 15.0F, flingListener);
    }

    public FlingCardListener(View frame, Object itemAtPosition, float rotation_degrees, FlingCardListener.FlingListener flingListener) {
        this.mActivePointerId = -1;
        this.frame = null;
        this.TOUCH_ABOVE = 0;
        this.TOUCH_BELOW = 1;
        this.obj = new Object();
        this.isAnimationRunning = false;
        this.MAX_COS = (float) Math.cos(Math.toRadians(45.0D));
        this.frame = frame;
        this.objectX = frame.getX();
        this.objectY = frame.getY();
        this.objectH = frame.getHeight();
        this.objectW = frame.getWidth();
        this.halfWidth = (float) this.objectW / 2.0F;
        this.dataObject = itemAtPosition;
        this.parentWidth = ((ViewGroup) frame.getParent()).getWidth();
        this.BASE_ROTATION_DEGREES = rotation_degrees;
        this.mFlingListener = flingListener;
    }

    public boolean onTouch(View view, MotionEvent event) {
        int pointerIndexMove;
        switch (event.getAction() & 255) {
            case 0:
                this.mActivePointerId = event.getPointerId(0);
                float x = 0.0F;
                float y = 0.0F;
                boolean success = false;

                try {
                    x = event.getX(this.mActivePointerId);
                    y = event.getY(this.mActivePointerId);
                    success = true;
                } catch (IllegalArgumentException var15) {
                    Log.w(TAG, "Exception in onTouch(view, event) : " + this.mActivePointerId, var15);
                }

                if (success) {
                    this.aDownTouchX = x;
                    this.aDownTouchY = y;
                    if (this.aPosX == 0.0F) {
                        this.aPosX = this.frame.getX();
                    }

                    if (this.aPosY == 0.0F) {
                        this.aPosY = this.frame.getY();
                    }

                    if (y < (float) (this.objectH / 2)) {
                        this.touchPosition = 0;
                    } else {
                        this.touchPosition = 1;
                    }
                }

                view.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case 1:
                this.mActivePointerId = -1;
                this.resetCardViewOnStack();
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case 2:
                pointerIndexMove = event.findPointerIndex(this.mActivePointerId);
                float xMove = event.getX(pointerIndexMove);
                float yMove = event.getY(pointerIndexMove);
                float dx = xMove - this.aDownTouchX;
                float dy = yMove - this.aDownTouchY;
                this.aPosX += dx;
                this.aPosY += dy;
                float distobjectX = this.aPosX - this.objectX;
                float rotation = this.BASE_ROTATION_DEGREES * 2.0F * distobjectX / (float) this.parentWidth;
                if (this.touchPosition == 1) {
                    rotation = -rotation;
                }

                this.frame.setX(this.aPosX);
                this.frame.setY(this.aPosY);
                this.frame.setRotation(rotation);
                this.frame.postInvalidate();
                this.mFlingListener.onScroll(this.getScrollProgressPercent());
                break;
            case 3:
                this.mActivePointerId = -1;
                view.getParent().requestDisallowInterceptTouchEvent(false);
            case 4:
            case 5:
            default:
                break;
            case 6:
                int pointerIndex = (event.getAction() & '\uff00') >> 8;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == this.mActivePointerId) {
                    pointerIndexMove = pointerIndex == 0 ? 1 : 0;
                    this.mActivePointerId = event.getPointerId(pointerIndexMove);
                }
        }

        return true;
    }

    private float getScrollProgressPercent() {
        if (this.movedBeyondLeftBorder()) {
            return -1.0F;
        } else if (this.movedBeyondRightBorder()) {
            return 1.0F;
        } else {
            float zeroToOneValue = (this.aPosX + this.halfWidth - this.leftBorder()) / (this.rightBorder() - this.leftBorder());
            return zeroToOneValue * 2.0F - 1.0F;
        }
    }

    private boolean resetCardViewOnStack() {
        if (this.movedBeyondLeftBorder()) {
            this.onSelected(true, this.getExitPoint(-this.objectW), 100L);
            this.mFlingListener.onScroll(-1.0F);
        } else if (this.movedBeyondRightBorder()) {
            this.onSelected(false, this.getExitPoint(this.parentWidth), 100L);
            this.mFlingListener.onScroll(1.0F);
        } else {
            float abslMoveDistance = Math.abs(this.aPosX - this.objectX);
            this.aPosX = 0.0F;
            this.aPosY = 0.0F;
            this.aDownTouchX = 0.0F;
            this.aDownTouchY = 0.0F;
            this.frame.animate().setDuration(200L).setInterpolator(new OvershootInterpolator(1.5F)).x(this.objectX).y(this.objectY).rotation(0.0F);
            this.mFlingListener.onScroll(0.0F);
            if ((double) abslMoveDistance < 4.0D) {
                this.mFlingListener.onClick(this.dataObject);
            }
        }

        return false;
    }

    private boolean movedBeyondLeftBorder() {
        return this.aPosX + this.halfWidth < this.leftBorder();
    }

    private boolean movedBeyondRightBorder() {
        return this.aPosX + this.halfWidth > this.rightBorder();
    }

    public float leftBorder() {
        return (float) this.parentWidth / 4.0F;
    }

    public float rightBorder() {
        return (float) (3 * this.parentWidth) / 4.0F;
    }

    public void onSelected(final boolean isLeft, float exitY, long duration) {
        this.isAnimationRunning = true;
        float exitX;
        if (isLeft) {
            exitX = (float) (-this.objectW) - this.getRotationWidthOffset();
        } else {
            exitX = (float) this.parentWidth + this.getRotationWidthOffset();
        }

        this.frame.animate().setDuration(duration).setInterpolator(new AccelerateInterpolator()).x(exitX).y(exitY).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (isLeft) {
                    FlingCardListener.this.mFlingListener.onCardExited();
                    FlingCardListener.this.mFlingListener.leftExit(FlingCardListener.this.dataObject);
                } else {
                    FlingCardListener.this.mFlingListener.onCardExited();
                    FlingCardListener.this.mFlingListener.rightExit(FlingCardListener.this.dataObject);
                }

                FlingCardListener.this.isAnimationRunning = false;
            }
        }).rotation(this.getExitRotation(isLeft));
    }

    public void selectLeft() {
        if (!this.isAnimationRunning) {
            this.onSelected(true, this.objectY, 200L);
        }

    }

    public void selectRight() {
        if (!this.isAnimationRunning) {
            this.onSelected(false, this.objectY, 200L);
        }

    }

    private float getExitPoint(int exitXPoint) {
        float[] x = new float[]{this.objectX, this.aPosX};
        float[] y = new float[]{this.objectY, this.aPosY};
        LinearRegression regression = new LinearRegression(x, y);
        return (float) regression.slope() * (float) exitXPoint + (float) regression.intercept();
    }

    private float getExitRotation(boolean isLeft) {
        float rotation = this.BASE_ROTATION_DEGREES * 2.0F * ((float) this.parentWidth - this.objectX) / (float) this.parentWidth;
        if (this.touchPosition == 1) {
            rotation = -rotation;
        }

        if (isLeft) {
            rotation = -rotation;
        }

        return rotation;
    }

    private float getRotationWidthOffset() {
        return (float) this.objectW / this.MAX_COS - (float) this.objectW;
    }

    public void setRotationDegrees(float degrees) {
        this.BASE_ROTATION_DEGREES = degrees;
    }

    public boolean isTouching() {
        return this.mActivePointerId != -1;
    }

    public PointF getLastPoint() {
        return new PointF(this.aPosX, this.aPosY);
    }

    protected interface FlingListener {
        void onCardExited();

        void leftExit(Object var1);

        void rightExit(Object var1);

        void onClick(Object var1);

        void onScroll(float var1);
    }
}
