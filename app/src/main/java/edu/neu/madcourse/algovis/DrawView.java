package edu.neu.madcourse.algovis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Arrays;

public class DrawView extends SurfaceView implements Runnable {
    Paint paint = new Paint();
    private SurfaceHolder mOurHolder;
    private static final String TAG = "DrawView";
    private float[] values;
    private Canvas mCanvas;
    private volatile boolean mDrawing;
    private boolean mPaused = true;
    private Thread mThread = null;

    private void init() {
        //paint.setColor(Color.BLUE);
        //paint.setStrokeWidth(20f);
//        mCanvas.drawColor(Color.argb(255, 0, 0, 0));

        // Choose a color to paint with
        paint.setColor(Color.WHITE);

        paint.setStrokeWidth(1f);

    }

    public DrawView(Context context) {
        super(context);
        mOurHolder = getHolder();
        init();
        setup();


    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void draw() {

        if (mOurHolder.getSurface().isValid()) {
            Log.d(TAG, "onDraw: calling valid");
            mCanvas = mOurHolder.lockCanvas();
            // Fill the screen with a solid color

            mCanvas.drawColor(Color.BLUE);

            // pre sorting
            for (int i = 0; i < values.length; i++) {
                mCanvas.drawLine(i, 1536, i, 1536 - values[i], paint);
                Log.d(TAG, "draw: drawing");
                bubbleSort(values);
            }

            // sort and show the change in real time




            mOurHolder.unlockCanvasAndPost(mCanvas);
            mDrawing = false;
        }



    }

    private void bubbleSort(float[] arr){
        // bubble sort
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr.length-i-1; j++){
                // update this each frame
                float a = arr[j];
                float b = arr[j+1];
                if (a > b){
                    swap(arr, j, j+1);
                }
                drawArray(j,j+1);
            }

        }
    }



    private void drawArray(int x, int y){

        for (int i = 0; i < values.length; i++){
            if ( i == x || i == y){
                paint.setColor(Color.BLUE);
            } else {
                Log.d(TAG, "drawArray: calling");
                postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 10000);

                //mCanvas.drawLine(i, 1536, 0, 1536 - values[i], paint);
            }
        }
    }

    private void swap(float[] arr, int a, int b){
        float temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }


    private void setup(){
        values = new float[1080];
        for (int i = 0; i < values.length; i++){
            values[i] = (float) (Math.random()*1536);
        }

        Log.d(TAG, "init: before soring " + Arrays.toString(values));
    }

    @Override
    public void run() {


        while(mDrawing){

            // get the start of the loop
            long frameStartTime = System.currentTimeMillis();

            long timeThisFrame =
                    System.currentTimeMillis() - frameStartTime;

           draw();




        }
    }

    public void resume(){
        mDrawing = true;
        mThread = new Thread(this);

        mThread.start();
    }

    public void pause(){
        mDrawing = false;
        try {
            mThread.join();
        } catch (InterruptedException e){
            Log.e(TAG, "pause: joining thread error");
        }
    }
}