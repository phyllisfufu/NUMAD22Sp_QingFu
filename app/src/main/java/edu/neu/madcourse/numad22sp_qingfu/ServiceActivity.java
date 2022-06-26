package edu.neu.madcourse.numad22sp_qingfu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServiceActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    EditText editText;
    ProgressBar progressBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webservice);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(this::onLoadDataButtonClick);
        progressBar = findViewById(R.id.progressBar);
        handler = new Handler();
    }

    public void onLoadDataButtonClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        RunnableThread runnableThread = new RunnableThread();
        Thread thread = new Thread(runnableThread);
        thread.start();
    }


    class RunnableThread implements Runnable {
        @Override
        public void run() {
            String url = "https://http.cat/";
            String code = editText.getText().toString();

            final Bitmap bitmap;
            try {
                URL httpStatusURL = new URL(url + code + ".jpg");
                HttpURLConnection conn = (HttpURLConnection) httpStatusURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.connect();

                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setImageBitmap(bitmap);
                        if (bitmap == null) {
                            Snackbar.make(findViewById(R.id.layout), "Please enter a valid http status code.", Snackbar.LENGTH_LONG).show();
                        }
                        imageView.setVisibility(View.VISIBLE);
                    }
                });
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

