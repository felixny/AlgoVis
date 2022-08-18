package edu.neu.madcourse.algovis;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BubbleSort extends AsyncTask<Void,Integer,Void>{

    private static final String TAG = "BubbleSort";

    @Override
    protected Void doInBackground(Void... voids) {
        int size = MainActivity.data.size();

        for (int i = 0; i < size - 1; i++){
            for (int j = 0 ; j < size - i - 1; j++){
                if (MainActivity.data.get(j) > MainActivity.data.get(j+1)){
                    int temp = MainActivity.data.get(j);
                    MainActivity.data.set(j,MainActivity.data.get(j+1));
                    MainActivity.data.set(j+1,temp);

                    //Log.d(TAG, "doInBackground: " + MainActivity.data);
                    publishProgress();

                    try {
                        Thread.sleep(MainActivity.speed/10);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate: " + Arrays.deepToString(values));
        MainActivity.draw(MainActivity.mImageView);

    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        MainActivity.sorted = true;
        MainActivity.draw(MainActivity.mImageView);
        MainActivity.mImageView.setClickable(true);
    }


}
