package com.example.rosa.kickstart.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosa.kickstart.R;

import org.w3c.dom.Text;

public class GPS_DataActivity extends AppCompatActivity {
    public static final String TAG = "GPS_DataActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView longitudeTV, latitudeTV, altitudeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_data);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("GPS Data");
        }

        longitudeTV = findViewById(R.id.longitude);
        latitudeTV = findViewById(R.id.latitude);
        altitudeTV = findViewById(R.id.altitude);

        Toast.makeText(this, R.string.wait_message, Toast.LENGTH_SHORT)
                .show();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               float latitude = (float) (location.getLatitude());
               float longitude = (float) (location.getLongitude());
               float altitude = (float) (location.getAltitude());

                longitudeTV.setText("Longitude: " + longitude);
                latitudeTV.setText("Latitude: " + latitude);
                altitudeTV.setText("Altitude: " + altitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
}
