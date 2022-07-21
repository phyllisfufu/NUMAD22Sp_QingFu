package edu.neu.madcourse.numad22sp_qingfu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;




public class LocateActivity_old extends AppCompatActivity{
    public TextView mLatitude;
    public TextView mLongitude;
    public TextView mLocationError;
    public TextView distance_textview;
    private LocationManager locationManager;
    private Location lastLocation;
    private double distanceTraveled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_locate_old);
        mLatitude = findViewById( R.id.latitude_textview );
        mLongitude = findViewById( R.id.longitude_textview );
        distance_textview = findViewById( R.id.distance_textview );
        mLocationError = findViewById( R.id.location_denied_textview );
        mLatitude.setVisibility( View.INVISIBLE );
        mLongitude.setVisibility( View.INVISIBLE );
        mLocationError.setVisibility( View.INVISIBLE );
        distance_textview.setVisibility(View.INVISIBLE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            String permission[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(LocateActivity_old.this,
                    permission, 1);
        }
        else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        super.onRequestPermissionsResult( requestCode,permissions,grantResults );
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(LocateActivity_old.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        initLocation();
                    }
                }else{
                    permissionDenied();
                }
                return;
            }
            default:
                permissionDenied();
                break;
        }
    }

    private void permissionDenied()
    {
        mLatitude.setVisibility( View.INVISIBLE );
        mLongitude.setVisibility( View.INVISIBLE );
        mLocationError.setVisibility( View.VISIBLE );
        distance_textview.setVisibility(View.VISIBLE);
    }
    private void initLocation() {
        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String locProvider = locationManager.getBestProvider(criteria, true);
            if (locProvider != null) {
                locationManager.requestLocationUpdates(locProvider,30000, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(locProvider);
                lastLocation = location;
                if (location != null){
                    showLocation(location);
                }
            }
        } catch (SecurityException ignored) {}
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle
                extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            distanceTraveled = GetDistanceFromLatLonInKm(lastLocation.getLatitude(),lastLocation.getLongitude(),location.getLatitude(),location.getLongitude());
            showLocation(location);
            lastLocation = location;
        }
    };

    private void showLocation(Location location) {
        mLatitude.setText( "Latitude : " + String.valueOf(location.getLatitude()) );
        mLongitude.setText("Longitude : " + String.valueOf(location.getLongitude()) );
        distance_textview.setText("DistanceTraveled : " + String.valueOf(distanceTraveled));
        mLatitude.setVisibility( View.VISIBLE );
        mLongitude.setVisibility( View.VISIBLE );
        distance_textview.setVisibility(View.VISIBLE);
    }

    public void resetDistance(View view){
        distanceTraveled = 0;
        distance_textview.setText("DistanceTraveled : " + String.valueOf(distanceTraveled));
        Toast.makeText(this, "Reset Success", Toast.LENGTH_SHORT).show();
    }


    public double GetDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2)
    {
        final int R = 6371;
        // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        // Distance in km
        return d;
    }
    private double deg2rad(double deg)
    {
        return deg * (Math.PI / 180);
    }
}