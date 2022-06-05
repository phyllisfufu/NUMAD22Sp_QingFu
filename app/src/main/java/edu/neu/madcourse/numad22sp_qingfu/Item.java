package edu.neu.madcourse.numad22sp_qingfu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Item {
    private final String name;
    private final String url;

    public Item(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    public String getName() {
        return name;
    }


    public boolean isValid() {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public void onClick(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
