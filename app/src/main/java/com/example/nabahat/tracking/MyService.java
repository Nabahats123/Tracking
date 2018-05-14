package com.example.nabahat.tracking;
import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service
{

    final static String MY_ACTION = "MY_ACTION";
    long difference, diffTimeInSecs, diffTimeInMinutes, diffTimeInHours;
    double mileage = 2500,fuelPrice = 65.0,fuelConsumed, totalFuel,avgSpeed;
    double latfromactivity,longfromactivity,latti,longi;
    long startmilisfromactivity;
    double dist;
    double distanceInKM;
    double totaldistance;
    double distanceInMeters;
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 10;
    private static final int REQUEST_LOCATION = 1;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;



        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {/*STEP#5 ALTERS VALUES IN LOCATION if changed*/
            double lat = location.getLatitude();
            double longitude = location.getLongitude();
/*CALCULATE DISTANCE*/
            getDistanceInMeters(latti,longi,lat,longitude);
            //distanceInKM = dist/1000;
            //distanceInKM = (dist *(1.6));
            //distanceInMeters = (distanceInKM *1000);
            Log.d("DISTANCE",String.valueOf(distanceInMeters));
            //distanceInKM = distanceInKM+ distanceInKM;
           //Total distnce mein pehlay sirf aik value jaey gi, baad mein increment hoti rahay gi,
            totaldistance = +distanceInMeters;
            //distanceInMeters = distanceInMeters + distanceInMeters;
            latti = lat;
            longi = longitude;
            //  mLastLocation.set(location);



        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
/*On LAUNCHING SERVICE, Service started
*
* */

        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_LONG).show();
//SERVICE STARTS, Timer starts
        startmilisfromactivity = System.currentTimeMillis();
        //latfromactivity = intent.getDoubleExtra("Latitude", latfromactivity);
        //longfromactivity = intent.getDoubleExtra("Longitude", longfromactivity);

        //CHECK IF GPS OR NET IS AVIALABLE SO IT CAN GET COORDINATES
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();

            } else if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();

            } else if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
            } else {

                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
//SERVICE KEEPS ON RUNNING IN THE BACKGROUND
        return START_STICKY;

    }

    @Override
    public void onCreate() {
//STEP#3
        /*RUNS AFTER onStart METHOD*/
        Log.e(TAG, "onCreate");
        initializeLocationManager();


        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            /*REQUEST UPDATES FOR COORDINATES
            */
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    @Override
    public void onDestroy()
    {/*to stop the service and destroy it*/
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        /*values calculated during alive time*/
        difference = System.currentTimeMillis() - startmilisfromactivity;
        diffTimeInSecs =difference/1000;
        diffTimeInMinutes = Math.round(diffTimeInSecs/60);
        diffTimeInHours = Math.round(diffTimeInMinutes/60);
        avgSpeed = totaldistance/diffTimeInSecs;
        fuelConsumed = (1/mileage) *(totaldistance);
        totalFuel =fuelPrice * fuelConsumed;
       /*Pass in intent
       */
        Intent intent = new Intent("GPSLocationUpdates");
        intent.putExtra("fuelconsumed", fuelConsumed);
        intent.putExtra("seconds", diffTimeInSecs);
        intent.putExtra("totalfuel", totalFuel);
        intent.putExtra("distance", totaldistance);
        intent.putExtra("speed", avgSpeed);

//moving to activty , broadcasting values , no more updates because service destroyed
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);
        }
    }
    public double getDistanceInMeters(double currentLatitude, double currentLongitude, double newLatitude, double newLongitude) {
        /*FOR CALCULATION*/

        double latitude = newLatitude;
        double longitude = newLongitude;
        double distance = 0;
        Location crntLocation = new Location("crntlocation");
        crntLocation.setLatitude(currentLatitude);
        crntLocation.setLongitude(currentLongitude);
        Location newLocation = new Location("newlocation");
        newLocation.setLatitude(newLatitude);
        newLocation.setLongitude(newLongitude);
        /*CALCULATES DISNTACE B/W TWO LOCATION */
        distance = crntLocation.distanceTo(newLocation);
        /*distanceInMeters pdated whatever the distance will be it will be updated here*/
        distanceInMeters = crntLocation.distanceTo(newLocation);
       // String distace = String.valueOf(distance);

        //distance =crntLocation.distanceTo(newLocation) / 1000; // in km
        //Toast.makeText(getApplicationContext(),distace,Toast.LENGTH_LONG).show();
        return distance;
    }

}


