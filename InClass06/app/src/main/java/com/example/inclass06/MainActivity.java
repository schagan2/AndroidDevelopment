package com.example.inclass06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Assignment: Inclass06[InClass06.app]
 * File name: MainActivity.java
 * Names: Aakansha Chauhan, Sindhura Chaganti
 */
public class MainActivity extends AppCompatActivity {

    //Global variables
    private static final String TAG = "TAG";
    private static final String RAND_NUM = "RAND_NUM";
    ExecutorService threadPool;
    Handler handler;
    ProgressDialog progressDialog;
    SeekBar seekBar;
    ListView tasksList;
    ArrayAdapter listAdapter;
    TextView complexityValue;
    TextView average;
    ProgressBar progressBar;
    TextView runningTasks;
    Button threadButton;
    Button asyncTask;
    List<Double> list = new ArrayList<Double>();
    double sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.main_activity);

        seekBar = findViewById(R.id.seekBar);
        complexityValue = findViewById(R.id.seekBarValue);
        tasksList = findViewById(R.id.tasksList);
        progressDialog = new ProgressDialog(this);
        progressBar = findViewById(R.id.progressBar);
        average = findViewById(R.id.average);
        runningTasks = findViewById(R.id.runningTasks);

        threadButton = findViewById(R.id.threadTask);
        asyncTask = findViewById(R.id.asyncTaskButton);

        listAdapter = new ArrayAdapter<Double>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        tasksList.setAdapter(listAdapter);

        progressBar.setVisibility(View.GONE);

        //Thread using Runnable interface
        threadPool = Executors.newFixedThreadPool(2);
        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPool.execute(new ThreadWork());
            }
        });

        //Thread using AsyncTask
        asyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncWork().execute(seekBar.getProgress());
                }
        });

        //Seekbar to display the value of the complexity
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                complexityValue.setText(progress + " " + getResources().getString(R.string.times));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: "+seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: "+seekBar.getProgress());
            }
        });

        //Handler
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int progress = msg.getData() != null ? msg.getData().getInt(RAND_NUM) : 0;

                switch (msg.what){
                    case ThreadWork.STATUS_START:
                        Log.d(TAG, "handleMessage: "+ "Thread starting");
                        sum = 0;
                        list.clear();

                        if(seekBar.getProgress() == 0){
                            //If no progress in complexity, disable progressbar, listview, average display and progress.
                            progressBar.setVisibility(View.GONE);
                            runningTasks.setText("");
                            average.setText("");
                            listAdapter.notifyDataSetChanged();
                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setMax(seekBar.getProgress());
                        }
                        threadButton.setEnabled(false);
                        seekBar.setEnabled(false);
                        asyncTask.setEnabled(false);
                        break;
                    case ThreadWork.STATUS_PROGRESS:
                        Log.d(TAG, "handleMessage: "+ "Thread executing");
                        listAdapter.notifyDataSetChanged();

                        progressBar.setProgress(progress+1);

                        runningTasks.setText(progress+1 +"/"+seekBar.getProgress());

                        sum += (Double) msg.obj;

                        average.setText(getResources().getText(R.string.average)+" "+ (sum/(progress+1)));
                        break;
                    case ThreadWork.STATUS_END:
                        Log.d(TAG, "handleMessage: "+ "Thread ended");
                        //Enabling all the buttons to normal after thread execution.
                        threadButton.setEnabled(true);
                        seekBar.setEnabled(true);
                        asyncTask.setEnabled(true);
                        break;
                }

                return false;
            }
        });
    }

    /*
    * ThreadWork class implements Runnable interface. The random numbers are
    * retrieved in run() method and accordingly statuses are set.
    */
    public class ThreadWork implements Runnable {
        static final int STATUS_START = 0x00;
        static final int STATUS_PROGRESS = 0x01;
        static final int STATUS_END = 0x02;
        @Override
        public void run() {
            Message startMessage = new Message();
            startMessage.what = STATUS_START;
            handler.sendMessage(startMessage);

            int times = seekBar.getProgress();
            for (int i = 0; i < times; i++) {
                Message message = new Message();
                message.what = STATUS_PROGRESS;
                Bundle bundle = new Bundle();
                bundle.putInt(RAND_NUM, i);
                Double num = HeavyWork.getNumber();
                list.add(num);
                message.setData(bundle);
                message.obj = num;
                handler.sendMessage(message);
            }
            Message stopMessage = new Message();
            stopMessage.what = STATUS_END;
            handler.sendMessage(stopMessage);
        }
    }

    /*
     * AsyncWork class extends AsyncTask<Integer, Integer, Double>.
     * In doInBackground(), random numbers are retrieved from HeavyWork.java
     * doPostExecute(), the buttons and seekbar are enabled.
     * In onPreExecute(), the buttons and seekbar are disabled and progressbar,
     * average TextView, progress TextView are enabled.
     */
    class AsyncWork extends AsyncTask<Integer, Integer, Double> {
        Double number;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sum = 0;
            list.clear();
            if(seekBar.getProgress() == 0){
                progressBar.setVisibility(View.GONE);
                runningTasks.setText("");
                average.setText("");
                listAdapter.notifyDataSetChanged();
            }else{
                progressBar.setMax(seekBar.getProgress());
                progressBar.setVisibility(View.VISIBLE);
            }
            threadButton.setEnabled(false);
            seekBar.setEnabled(false);
            asyncTask.setEnabled(false);
        }

        @Override
        protected Double doInBackground(Integer... doubles) {
            for (int i = 0; i < doubles[0]; i++) {
                number = HeavyWork.getNumber();
                list.add(number);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];

            listAdapter.notifyDataSetChanged();
            progressBar.setProgress(progress+1);
            runningTasks.setText(progress+1 +"/"+seekBar.getProgress());

            sum += number;

            average.setText(getResources().getText(R.string.average)+" "+(sum/(progress+1)));

        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            threadButton.setEnabled(true);
            seekBar.setEnabled(true);
            asyncTask.setEnabled(true);
        }
    }
}