package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dbicustomer.com.agicon.dbi.dbicutomer.MainActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class SplashScreenActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    public static final int SPLASH_TIME_OUT = 2000;
    public static final int REQUEST_LOCATION = 001;

    boolean isLoggedin;
    String user_id;
    private Intent intent;

    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location mlocation;
    LocationSettingsRequest.Builder locationSettingsRequest;
    Context context;
    PendingResult<LocationSettingsResult> pendingResult;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String locality, subLocality, adminArea, countryName;
    private String registered_access_token, login_access_token, store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
    }

    private void init() {
        context = this;
        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

//        isLoggedin = Boolean.parseBoolean(sharedPreferences.getString(SharedPrefernceValue.IS_LOGGED_IN, ""));

        if (!registered_access_token.equalsIgnoreCase("")) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            /*Check gps is enabled through isProviderEnabled*/
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
            else {
                mEnableGps();
            }
        } else if (!login_access_token.equalsIgnoreCase("")) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            /*Check gps is enabled through isProviderEnabled*/
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
            else {
                mEnableGps();
            }

        } else {
            initDataAndSplashLogic();
        }
    }

    private void initDataAndSplashLogic() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationSettingsRequest = new LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest);

        /*Read this below, why we use fused api*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        mResult();
    }

    @Override
    public void onLocationChanged(Location location) {

        mlocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
//        LatLng latLng = new LatLng(latitude, longitude);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addressList.get(0);

            locality = addressList.get(0).getLocality();
            subLocality = addressList.get(0).getSubLocality();
            adminArea = addressList.get(0).getAdminArea();
            countryName = addressList.get(0).getCountryName();

            double second_latitude = address.getLatitude();
            double second_longitude = address.getLongitude();

            Log.i("second_latitude", String.valueOf(second_latitude));
            Log.i("second_longitude", String.valueOf(second_longitude));

        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.putString(SharedPrefernceValue.LATITUDE, String.valueOf(latitude));
        editor.putString(SharedPrefernceValue.LONGITUDE, String.valueOf(longitude));
        editor.putString(SharedPrefernceValue.CURRENT_CITY, String.valueOf(locality));
        editor.commit();

    }


    private void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(SplashScreenActivity.this, REQUEST_LOCATION);


                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show();

                        init();

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(context, "Gps cancelled", Toast.LENGTH_SHORT).show();
                        mEnableGps();
                }
                break;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
