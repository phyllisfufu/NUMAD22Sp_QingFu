package edu.neu.madcourse.numad22sp_qingfu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.about_button:
                TextView about = findViewById(R.id.about_textView);
                if(about.getVisibility() == View.INVISIBLE) {
                    about.setVisibility(View.VISIBLE);
                    about.postDelayed(() -> about.setVisibility(View.INVISIBLE), 15000);
                }
                break;
            case R.id.clickhere_button:
                Intent ClickyClick_intent = new Intent(this, ClickyActivity.class);
                startActivity(ClickyClick_intent);
                break;
            case R.id.linkcollector_button:
                Intent linkCollector_intent = new Intent(this,LinkCollectorActivity.class);
                startActivity(linkCollector_intent);
                break;
            case R.id.findPrime_button:
                Intent findPrime_intent = new Intent(this,FindPrimeActivity.class);
                startActivity(findPrime_intent);
                break;
            case R.id.locate_button:
                Intent locate_intent = new Intent(this,LocateActivity.class);
                startActivity(locate_intent);
                break;
            case R.id.service_button:
                Intent service_intent = new Intent(this,ServiceActivity.class);
                startActivity(service_intent);
                break;
        }
    }
}