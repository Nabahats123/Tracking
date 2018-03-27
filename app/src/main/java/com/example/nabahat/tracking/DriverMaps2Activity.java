package com.example.nabahat.tracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.nabahat.tracking.R.id.map;

public class DriverMaps2Activity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, loc;
    int i = 1;
    private List<Polyline> polylines;
    ArrayList<LatLng> MarkerPoints;
    String j = "";
    int PROXIMITY_RADIUS = 10000;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorPrimary,R.color.colorAccent,R.color.primary_dark_material_light};
    LocationRequest mLocationRequest;
    double latitude = 30; double longitude = 50;
    double end_latitude =25;  double end_longitude = 40;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        polylines = new ArrayList<>();
        MarkerPoints = new ArrayList<>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            //  j =(String) b.get("Bus Number");
            // Toast.makeText(DriverMapsActivity.this, j, Toast.LENGTH_SHORT).show();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(userId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        j = dataSnapshot.child("busnumber").getValue(String.class);
                      //  Toast.makeText(DriverMapsActivity.this, j, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

                @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

}

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng exp = new LatLng(33.527478, 73.104906);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (i==1){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Current Position"));
            //getDirection(latLng);
            i = 2;
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriverLocation");
        //ref.child("Bus").setValue(j);

        GeoFire geofire = new GeoFire(ref);
        geofire.setLocation(userId, new GeoLocation(location.getLatitude(),location.getLongitude()));







    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        startActivity(new Intent(DriverMaps2Activity.this, DriverHome.class));
        finish();
        // optional depending on your needs
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude() );
        LatLng exp = new LatLng(33.651925, 73.156604);
        // mMap.addPolyline(new PolylineOptions().add(latLng,exp).width(10).color(R.color.colorPrimaryDark));
        MarkerOptions distance = new MarkerOptions();
        distance.position(exp);
        distance.title("Destination");
        float results[] = new float[5];
        //Location.distanceBetween(latLng.latitude, latLng.longitude, exp.latitude,exp.longitude, results);
        distance.snippet("Distance = " + results[0] + "m");
       //    mMap.addMarker(distance);


        loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        LatLng end = new LatLng(60, 80);
        ;
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(latLng, end)
                .build();
        routing.execute();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {




            }
        });

    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);

        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
         //   Toast.makeText(this, "Error: " +e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }
     @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
         if(polylines.size()>0) {
             for (Polyline poly : polylines) {
                 poly.remove();
             }
         }
         polylines = new ArrayList<>();
         //add route(s) to the map.
         for (int i = 0; i <route.size(); i++) {

             //In case of more than 5 alternative routes
             int colorIndex = i % COLORS.length;

             PolylineOptions polyOptions = new PolylineOptions();
             polyOptions.color(getResources().getColor(COLORS[colorIndex]));
             polyOptions.width(10 + i * 3);
             polyOptions.addAll(route.get(i).getPoints());
             Polyline polyline = mMap.addPolyline(polyOptions);
             polylines.add(polyline);

             Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
         }
    }

    @Override
    public void onRoutingCancelled() {
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
}