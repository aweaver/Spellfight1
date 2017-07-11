package com.knowware.aw.spellfight1.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by Aaron on 5/12/2017.
 */

public class Spellgesture implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener
{
    static final public int UP=0;
    static final public int DN=1;
    static final public int LT=2;
    static final public int RT=3;

    public interface SpellGestureListener
    {
        void flingMsg(int iDir);
        void tapMsg();
    }

    private ScaleGestureDetector detector;
    private SpellGestureListener spellGestureListener;


    public Spellgesture()
    {

    }

    public void setListener(SpellGestureListener sspellGestureListener)
    {
        spellGestureListener=sspellGestureListener;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent)
    {

        spellGestureListener.tapMsg();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1)
    {
        float velx, vely;

        if(spellGestureListener==null)
            return(false);

        velx=v;
        vely=v1;

        //ignore low values
        if(Math.abs(velx) <300 && Math.abs(vely)<300)
            return(true);

       // motionEvent.

        if(Math.abs(velx)>Math.abs(vely))
        {
            if(velx>0)
            {
                spellGestureListener.flingMsg(RT);


            }
            else
            {
                //charge up spell

                spellGestureListener.flingMsg(LT);

            }
        }else
        {
            if(vely>0)
            {


                spellGestureListener.flingMsg(DN);

            }
            else
            {
                spellGestureListener.flingMsg(UP);
            }
        }

        return true;
    }

    public void onTouchEvent(MotionEvent event)
    {

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        float onScaleBegin = 0;
        float onScaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
           // scale *= detector.getScaleFactor();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector)
        {

            super.onScaleEnd(detector);
        }
    }
}
