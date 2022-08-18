package edu.neu.madcourse.algovis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GLSurfaceView glView;
    private DrawView drawView;
    private static Canvas mCanvas;
    private static Paint mPaint = new Paint();

    private static Bitmap mBitmap;

    public static ImageView mImageView;
    public static TextView mTextView;
    public static Button mButton;

    private static Rect[] mRect;

    private static int mColorBackground;
    private static int mColorFinal;

    private static int size;
    public static ArrayList<Integer> data = new ArrayList<>();
    static Random random =  new Random();
    private static int maxWidth;
    private static int maxHeight;
    public static int speed;

    public static boolean sorted = false;
    private AsyncTask task;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mColorBackground = ResourcesCompat.getColor(getResources(), R.color.teal_700,null);
        mColorFinal = ResourcesCompat.getColor(getResources(), R.color.purple_200,null);

        mPaint.setColor(mColorBackground);

        mImageView = (ImageView) findViewById(R.id.myimageview);
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.VISIBLE);
                draw(mImageView);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setVisibility(View.VISIBLE);
                initializeArray();
                draw(mImageView);
            }
        });
//            drawView = new DrawView(this);
//            //drawView.setBackgroundColor(Color.BLUE);
//
//            //glView = new MyGLSurfaceView(this);
//            setContentView(drawView);
        speed = 5;
        size = maxWidth;
        //mButton.setVisibility(View.INVISIBLE);
        if (data.size() != 0) {
            initializeArray();
        }
    }

    private static void initializeArray() {
        data.clear();
        for (int i = 0; i < size; i++){
            int value = random.nextInt(maxHeight);
            data.add(value);
            Log.d("TAG","value is: "+String.valueOf(value));
        }
    }

    public static void draw(View view) {
        int width = view.getWidth()/6;
        int height = view.getHeight()/6;

        size = width;
        Log.d(TAG, "draw: " + size);
        if (sorted){
            mPaint.setColor(mColorFinal);
            mImageView.setClickable(true);
        } else {
            mPaint.setColor(Color.BLUE);

        }

        maxHeight = height;
        maxWidth = width;

        mRect = new Rect[size];
        if (data.size() == 0){
            initializeArray();
        }

        int currentWd = maxWidth / size;

        mBitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_8888);

        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        //int currentWidth = width / size;

        for (int i = 0; i < data.size(); i++){
           // mRect[i] = new Rect();
            //mRect[i].set(currentWidth*i, height-data.get(i), currentWidth*(i+1),height);
            mPaint.setStrokeWidth(10f);
            mCanvas.drawLine(i, height, i, height - data.get(i), mPaint);
           // mRect[i].set(i, height-data.get(i), i+1,height);
            //mCanvas.drawRect(mRect[i],mPaint);
        }

        view.invalidate();
        if (sorted){
            speed = 3;
            mButton.setText("click to sort");
            sorted = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //drawView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //.pause();
    }


    public void sort(View view){
       mImageView.setClickable(false);

//task = new BubbleSort().execute();
        //update();
        quickSort();
    }

    public void update(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // for updating UI
        handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            background();

            handler.post(() -> {
                // UI Thread
                postExecute();
            });
        });
    }

    public void background(){
        int size = MainActivity.data.size();

        for (int i = 0; i < size - 1; i++){
            for (int j = 0 ; j < size - i - 1; j++){
                if (MainActivity.data.get(j) > MainActivity.data.get(j+1)){
                    int temp = MainActivity.data.get(j);
                    MainActivity.data.set(j,MainActivity.data.get(j+1));
                    MainActivity.data.set(j+1,temp);



                   progressUpdate(data);

                    try {
                        Thread.sleep(MainActivity.speed);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void progressUpdate(ArrayList<Integer> x){

        data = x;
        handler = new Handler(Looper.getMainLooper());
        handler.post(() -> MainActivity.draw(MainActivity.mImageView));


    }

    public void postExecute(){
        MainActivity.sorted = true;
       MainActivity.draw(MainActivity.mImageView);
       MainActivity.mImageView.setClickable(true);
    }


    public void quickSort(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // for updating UI
        handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            QuickSortBackground();

            handler.post(() -> {
                // UI Thread
                QuickSortPostExecute();
            });
        });
    }

    public void QuickSortBackground(){
        quickSort(MainActivity.data, 0, MainActivity.data.size() - 1);
    }

    public void QuickSortPostExecute(){
        MainActivity.sorted = true;
        MainActivity.draw(MainActivity.mImageView);
        MainActivity.mImageView.setClickable(true);
    }

    private int partition(ArrayList<Integer> myArray, int low, int high) {
        int pivot = myArray.get(high);
        int firstIdx = (low - 1);
        for (int secondIdx = low; secondIdx < high; secondIdx++) {
            if (myArray.get(secondIdx) < pivot) {
                firstIdx++;

                int temp = myArray.get(firstIdx);
                myArray.set(firstIdx, myArray.get(secondIdx));
                myArray.set(secondIdx, temp);

            }
            QuickSortProgressUpdate(myArray);
            try {
                Thread.sleep(MainActivity.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int temp = myArray.get(firstIdx + 1);
        myArray.set(firstIdx + 1, myArray.get(high));
        myArray.set(high, temp);
        QuickSortProgressUpdate(myArray);
        try {
            Thread.sleep(MainActivity.speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return firstIdx + 1;
    }


    private void sort(ArrayList<Integer> myArray, int low, int high) {
        if (low < high) {
            int pivot = partition(myArray, low, high);
            QuickSortProgressUpdate(myArray);

            sort(myArray, low, pivot - 1);
            sort(myArray, pivot + 1, high);
        }
    }
    public void QuickSortProgressUpdate (ArrayList<Integer> x){
        data = x;
        handler = new Handler(Looper.getMainLooper());
        handler.post(() -> MainActivity.draw(MainActivity.mImageView));
    }

    private void quickSort(ArrayList<Integer> array, int lowIndex, int highIndex){

        if (lowIndex >= highIndex) {
            return;
        }
        int pivot = array.get(highIndex);

        int leftPointer = partition1(array, lowIndex, highIndex, pivot);
        QuickSortProgressUpdate(array);

        quickSort(array, lowIndex, leftPointer-1);
        quickSort(array, leftPointer+1, highIndex);
    }

    private void swap(ArrayList<Integer> array, int i, int j){
        int temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    private int partition1(ArrayList<Integer> array, int lowIndex, int highIndex, int pivot){
        int leftPointer = lowIndex;
        int rightPointer = highIndex-1;

        while (leftPointer < rightPointer){
            // left pointer num is less than pivot
            while(array.get(leftPointer) <= pivot && leftPointer < rightPointer){
                leftPointer++;
            }

            while(array.get(rightPointer) >= pivot && leftPointer < rightPointer){
                rightPointer--;
            }

            swap(array, leftPointer, rightPointer);

            QuickSortProgressUpdate(array);
            try {
                Thread.sleep(MainActivity.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (array.get(leftPointer) > array.get(highIndex)){
            swap(array, leftPointer, highIndex);
            QuickSortProgressUpdate(array);
            try {
                Thread.sleep(MainActivity.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            leftPointer = highIndex;
        }

        return leftPointer;
    }



//    class MyGLSurfaceView extends GLSurfaceView {
//
//        private final MyGLRenderer renderer;
//
//        public MyGLSurfaceView(Context context){
//            super(context);
//
//            // Create an OpenGL ES 2.0 context
//            setEGLContextClientVersion(2);
//
//            renderer = new MyGLRenderer();
//
//            // Set the Renderer for drawing on the GLSurfaceView
//            setRenderer(renderer);
//        }
//    }
}


