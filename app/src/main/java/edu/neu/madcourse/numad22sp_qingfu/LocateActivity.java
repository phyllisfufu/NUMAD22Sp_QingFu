package edu.neu.madcourse.numad22sp_qingfu;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.Manifest;
import android.location.Location;
import android.content.pm.PackageManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.DecimalFormat;

public class LocateActivity extends AppCompatActivity{
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double totalDistance;
    private double prevLatitude;
    private double prevLongitudes;
    private static final String STATE_DIST = "STATE_DIST";
    private static final String STATE_LA = "STATE_LA";
    private static final String STATE_LO = "STATE_LO";
    private final DecimalFormat df = new DecimalFormat("#.####");
    private final String stringBuilder = "%s\n %s";
    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 1;
    private static final int PERMISSIONS_LOCATION = 99;

    private TextView value_currentCoordinates;
    private TextView value_totalDistance;
    private Switch simpleSwitch;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        value_totalDistance = findViewById(R.id.distance_textview);
        value_currentCoordinates = findViewById(R.id.latitudeLongtitude_textview);
        simpleSwitch = findViewById(R.id.switch_precision);
        TextView value_currentCoordinates = findViewById(R.id.latitudeLongtitude_textview);
        totalDistance = 0.0F;
        lock = true;

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_DIST)) {
            totalDistance = savedInstanceState.getDouble(STATE_DIST);
            prevLatitude = savedInstanceState.getDouble(STATE_LA);
            prevLongitudes = savedInstanceState.getDouble(STATE_LO);
            String textTotalDistance;
            textTotalDistance = df.format(totalDistance);
            value_totalDistance.setText(textTotalDistance + " M");
            lock = false;
        }

        locationRequest = LocationRequest.create()
                .setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
                .setFastestInterval(1000 * FAST_UPDATE_INTERVAL)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                getCoordinates(locationResult.getLastLocation());
            }
        };

        String loading = "GPS service is starting...";
        value_currentCoordinates.setText(loading);

        simpleSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(simpleSwitch.isChecked()){
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    Snackbar.make(findViewById(android.R.id.content), "Locator has set to High Accuracy.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    Snackbar.make(findViewById(android.R.id.content), "Locator has set to Balanced Power Accuracy.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        updateLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                updateLocation();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Please grant location permission", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        }
    }
    private void updateLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(LocateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(findViewById(android.R.id.content), "error_access_denied", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            ActivityCompat.requestPermissions(LocateActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        } else if (ActivityCompat.checkSelfPermission(LocateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(findViewById(android.R.id.content), "error_access_denied", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            ActivityCompat.requestPermissions(LocateActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_LOCATION);
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getCoordinates(location);
                }
            });
        }
    }


    private void getCoordinates(Location location){
        float[] results = new float[1];
        String textTotalDistance;
        value_currentCoordinates.setText(String.format(stringBuilder, location.getLatitude(), location.getLongitude()));
        if(lock){
            textTotalDistance = "0.0000";
            lock = false;
        } else {
            location.distanceBetween(location.getLatitude(),
                    location.getLongitude(), prevLatitude, prevLongitudes, results);
            totalDistance += results[0];
            textTotalDistance = df.format(totalDistance);
        }
        value_totalDistance.setText(textTotalDistance + " M");
        prevLatitude = location.getLatitude();
        prevLongitudes = location.getLongitude();
    }

    public void resetTotalDistance(View view){
        lock = true;
        totalDistance = 0.0F;
        value_totalDistance.setText("0.0000 M");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putDouble(STATE_DIST, totalDistance);
        savedInstanceState.putDouble(STATE_LA, prevLatitude);
        savedInstanceState.putDouble(STATE_LO, prevLongitudes);
        super.onSaveInstanceState(savedInstanceState);
    }
}