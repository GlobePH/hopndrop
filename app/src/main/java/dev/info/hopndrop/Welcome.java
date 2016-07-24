package dev.info.hopndrop;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.ProgressBar;


public class Welcome extends Activity {
    ProgressBar progressbar;
    boolean isRunning;
    Handler handler;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);

         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressbar.incrementProgressBy(5);
                if (progressbar.getProgress() == 100) {


                    intent = new Intent(Welcome.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    public void onStart() {
        super.onStart();
        progressbar.setProgress(0);
        Thread backgroundThread = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 20 && isRunning; i++) {
                        Thread.sleep(300);
                        handler.sendMessage(handler.obtainMessage());
                    }
                }
                catch (Throwable t) {

                }
            }
        });
        isRunning = true;
        backgroundThread.start();
    }

    public void onStop() {
        super.onStop();
        isRunning = false;
    }
}
