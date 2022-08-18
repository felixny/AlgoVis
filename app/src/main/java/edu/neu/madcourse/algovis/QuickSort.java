package edu.neu.madcourse.algovis;

import android.os.AsyncTask;

import java.util.ArrayList;

public class QuickSort extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        sort(MainActivity.data, 0, MainActivity.data.size() - 1);

        return null;
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
            publishProgress();
            try {
                Thread.sleep(MainActivity.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int temp = myArray.get(firstIdx + 1);
        myArray.set(firstIdx + 1, myArray.get(high));
        myArray.set(high, temp);
        publishProgress();
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
            publishProgress();

            sort(myArray, low, pivot - 1);
            sort(myArray, pivot + 1, high);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        MainActivity.draw(MainActivity.mImageView);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.sorted = true;
        MainActivity.draw(MainActivity.mImageView);
        MainActivity.mImageView.setClickable(true);
    }
}
