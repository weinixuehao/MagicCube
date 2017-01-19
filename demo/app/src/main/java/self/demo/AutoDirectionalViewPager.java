package self.demo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class AutoDirectionalViewPager extends InfiniteViewPager {
    private static final String TAG = AutoDirectionalViewPager.class.getSimpleName();
    private int mTouchSlop;

    public AutoDirectionalViewPager(Context context) {
        super(context);
        init();
    }

    public AutoDirectionalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
        // The majority of the magic happens here
//        setPageTransformer(false, new CubeOutTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }


    /**
     * disable left swipe when currentItem is 0, because it to be crash on some device.(the issue has been fixed)
     */
//    private float initialY;
//    private boolean isSwipeAllowed(MotionEvent event) {
//        int currentItem = getCurrentItem();
////        Log.i(TAG, "currentItem:" + currentItem);
//        if (currentItem != 0) {
//            return true;
//        }
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            initialY = event.getY();
//            return true;
//        }
//
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            if (event.getY() > initialY) {
//                return false;
//            }
//        }
//
//        return true;
//    }

    private int direction = Direction.UNKNOW;
    static class Direction {
        public static final int VERTICAL = 0;
        public static final int HORIZONTAL = 1;
        public static final int UNKNOW = -1;
    }

    /**
     * detect swipe event and direction, once detected, start to slide(call the onInterceptTouchEvent func and reset to ACTION_DOWN of MotionEvent).
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.i(TAG, "dispatchTouchEvent-->direction:" + direction);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (direction == Direction.UNKNOW) {
                    originalX = ev.getX();
                    originalY = ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (direction == Direction.UNKNOW) {
                    float absX = Math.abs(originalX - ev.getX());
                    float absY = Math.abs(originalY - ev.getY());
                    Log.i(TAG, "dispatchTouchEvent-->absX:" + absX +
                            " absY:" + absY + " mTouchSlop:" + mTouchSlop);
                    if (absX > mTouchSlop || absY > mTouchSlop) {
                        direction = absX > absY ? Direction.HORIZONTAL : Direction.VERTICAL;
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        return onInterceptTouchEvent(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                direction = Direction.UNKNOW;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * set the transformer according to the swipe direction that has been detected by dispatchTouchEvent func.
     * since we know the viewpager that only achieve a horizontal swipe, so we need to implement vertical swipe(
     * refer to the implemention of swapXY() method for detail，it is very simple).
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
//        Log.i(TAG, "onInterceptTouchEvent-->direction:" + direction);
        boolean interceptHandle;
        clearAnimation();
        if (direction == Direction.VERTICAL) {
            setPageTransformer(false, new VerticalCubeTransform());
            interceptHandle = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev);
        } else {
            setPageTransformer(false, new HorizontalCubeTransform());
            interceptHandle = super.onInterceptTouchEvent(ev);
        }

        return interceptHandle;
//        boolean intercepted = super.onInterceptTouchEvent(
//                direction == Direction.HORIZONTAL ? ev : swapXY(ev));
//        Log.i(TAG, "intercepted:" + intercepted);
//        if (direction == Direction.VERTICAL) {
//            swapXY(ev); // return touch coordinates to original reference frame for any child views
//        }
//        return intercepted;
    }

    private float originalX;
    private float originalY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "onTouchEvent");
//        if (!isSwipeAllowed(ev)) {
//            return true;
//        }
//        Log.i(TAG, "onTouchEvent-->direction:" + direction);
        return super.onTouchEvent(direction == Direction.HORIZONTAL? ev : swapXY(ev));
    }

    /**
     * the implemention of vertical cube transformer
     */
    static class VerticalCubeTransform implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
//            Log.i(TAG, "position:" + position);
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            view.setAlpha(normalizedposition);
            view.setTranslationX(view.getWidth() * -position);
            view.setTranslationY(position * view.getHeight());

            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(position < 0f ? view.getHeight() : 0f);
            view.setRotationX(90f * -position);

            float rotationY = view.getRotationY();
            if (rotationY != 0) {
                view.setRotationY(0);
            }

        }
    }


    /**
     * the implemention of horizontal cube transformer
     */
    private class HorizontalCubeTransform implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            view.setAlpha(normalizedposition);
            view.setPivotX(position < 0f ? view.getWidth() : 0f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
            if (view.getTranslationX() != 0) {
                view.setTranslationX(0);
            }

            if (view.getTranslationY() != 0) {
                view.setTranslationY(0);
            }


            float rotationX = view.getRotationX();
            Log.i(TAG, "horizontal --> rotationX:"+rotationX);
            if (rotationX != 0) {
                view.setRotationX(0);
            }
        }
    }

}
