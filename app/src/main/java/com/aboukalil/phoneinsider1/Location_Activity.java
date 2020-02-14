package com.aboukalil.phoneinsider1;

import android.support.v7.app.AppCompatActivity;
/*
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
*/


public class Location_Activity extends AppCompatActivity
{
    /*
    SharedPreferences prefs;
    private Context context;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    public static final int LOCATION_REQUEST = 22;
    public static final int GPS_REQUEST = 23;
    private Location location;
    private double wayLatitude = 0.0;
    private double wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String _url;

    public Location_Activity() {
    }

    public Location_Activity(Context context) {
        location = null;
        _url = "";
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
        builder.setAlwaysShow(true); //this is the key ingredient
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

    }

    // method for turn on GPS
    public void turnGPSOn() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        }
                    })
                    .addOnFailureListener((Activity) context, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult((Activity) context, GPS_REQUEST);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public String getCurrentLocation() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnGPSOn();
            return "GPS is Disabled ";
        } else {
            getLocation(context);
            String result = "";
            getLocation(context);
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //prefs = getSharedPreferences("Location", MODE_PRIVATE);
            String lat = prefs.getString("Latitude", null);
            String land = prefs.getString("Longitude", null);
            String url = prefs.getString("url", null);

            if (lat != null && land != null && url != null) {
                result = "latitude : " + lat + " , longitude : " + land + "\n";
                result += "URL : " + url;
            }
            return result;
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation(final Context context)
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location _location : locationResult.getLocations()) {
                    if (_location != null) {
                        location = _location;
                        wayLatitude = _location.getLatitude();
                        wayLongitude = _location.getLongitude();
                        setURL(wayLatitude, wayLongitude);
                        _url = getURL();

                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
                        editor.putString("Latitude", wayLatitude + "");
                        editor.putString("Longitude", wayLongitude + "");
                        editor.putString("url", _url);
                        editor.apply();


                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
            mFusedLocationClient.getLastLocation().addOnSuccessListener(Location_Activity.this, new OnSuccessListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(Location _location) {
                    if (_location != null) {
                        location = _location;
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        setURL(wayLatitude, wayLongitude);
                        _url = getURL();
                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
                        editor.putString("Latitude", wayLatitude + "");
                        editor.putString("Longitude", wayLongitude + "");
                        editor.putString("url", _url);
                        editor.apply();
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    else {

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Location_Activity.this);
                        locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(10 * 1000); // 10 seconds
                        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                for (Location _location : locationResult.getLocations()) {
                                    if (_location != null) {
                                        location = _location;
                                        wayLatitude = _location.getLatitude();
                                        wayLongitude = _location.getLongitude();
                                        setURL(wayLatitude, wayLongitude);
                                        _url = getURL();

                                        SharedPreferences sharedPreferences = PreferenceManager
                                                .getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
                                        editor.putString("Latitude", wayLatitude + "");
                                        editor.putString("Longitude", wayLongitude + "");
                                        editor.putString("url", _url);
                                        editor.apply();


                                        if (mFusedLocationClient != null) {
                                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                                        }
                                    }
                                }
                            }
                        };
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                                Location_Activity.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location _location) {
                                        if (location != null) {
                                            location = _location;
                                            wayLatitude = location.getLatitude();
                                            wayLongitude = location.getLongitude();
                                            setURL(wayLatitude, wayLongitude);
                                            _url = getURL();

                                            SharedPreferences sharedPreferences = PreferenceManager
                                                    .getDefaultSharedPreferences(context);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            //SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
                                            editor.putString("Latitude", wayLatitude + "");
                                            editor.putString("Longitude", wayLongitude + "");
                                            editor.putString("url", _url);
                                            editor.apply();


                                        } else {
                                            if (ActivityCompat.checkSelfPermission(Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Location_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            } else
                                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
            }
        }
    }
    public void setURL(double lat , double lnd)
    {
        _url = "https://www.google.com/maps/search/?api=1&query="+lat+","+lnd;
    }
    public String getURL()
    {
        return _url;
    }
    */
}
