package edu.neu.madcourse.numad22sp_qingfu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FindPrimeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Handler textHandler = new Handler();
    TextView statusText;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_prime);
        statusText = findViewById(R.id.runStatusText);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog dialog = new AlertDialog.Builder(FindPrimeActivity.this).create();
            dialog.setTitle("EXIT");
            dialog.setMessage("Are you really want to exit?");
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }






    Thread thread;
    public void runOnRunnableThread(View view) {
        runnableThread runnableThread = new runnableThread();
        thread = new Thread(runnableThread);
        thread.start();
    }

    public void stopOnRunnableThread(View view) {
       running = false;
    }

    public boolean isPrimeNum(int num) {
        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    //Class which implements the Runnable interface.
    class runnableThread implements Runnable {
        @Override
        public void run() {
            int lastPrimeNum = 2;
            for (int i = 2; i <= 1000; i++) {
                if (!running){
                    return;
                }
                if (thread.isInterrupted()){
                    return;
                }
                if (isPrimeNum(i)){
                    lastPrimeNum = i;
                }
                int finalI = i;
                int finalLastPrimeNum = lastPrimeNum;

                //The handler changes the TextView running in the UI thread.
                textHandler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        statusText.setText("The latest prime found is  " + finalLastPrimeNum + ", and the current number " + finalI +  " being checked");
                        if (finalI == 1000) {
                            statusText.setText("");
                        }
                    }
                });
                Log.d(TAG, "Running on a different thread using Runnable Interface: " + i);
                try {
                    Thread.sleep(100); //Makes the thread sleep or be inactive for 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
