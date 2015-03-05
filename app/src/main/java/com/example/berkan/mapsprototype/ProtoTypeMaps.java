package com.example.berkan.mapsprototype;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class ProtoTypeMaps extends FragmentActivity {


    public ArrayList<MarkerOptions> markers;
    public Handler handler;


    public LatLng currentLocation;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proto_type_maps);
        markers = new ArrayList<MarkerOptions>();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been
     *
     * completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            mMap.setMyLocationEnabled(true);
            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    double minDist = 1E38;
                    int minIndex = -1;
                    for (int i = 0; i < markers.size(); i++) {
                        Location kaas = new Location("Marker");
                        kaas.setLatitude(markers.get(i).getPosition().latitude);
                        kaas.setLongitude(markers.get(i).getPosition().longitude);
                        kaas.setTime(new Date().getTime());
                        float test = location.distanceTo(kaas);
                        if (test < minDist) {
                            minDist = test;
                            minIndex = i;
                        }
                    }
                    if (minIndex >= 0) {
                        MarkerOptions test2 = markers.get(minIndex);
                        test2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mMap.addMarker(test2);
                        Log.d("Test2", "Test2 " + test2.getTitle());

                    }

/*
                    for (int i = 0; i < markers.size(); i++) {
                        float[] distance = new float[1];
                        Location.distanceBetween(loc.latitude, loc.longitude, markers.get(i).getPosition().latitude, markers.get(i).getPosition().longitude, distance);
                        //Log.d("Distance", "Distance of marker" + distance[0]);
                        if(distance[0] < 100.0) {
                            Log.d("Distance below 100 m", "Distance below 100 m" + distance[0]);
                        }
                    }*/


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.0f));
                    Log.d("Latitude", "Current Latitude " + location.getLatitude());
                    Log.d("Longitude", "Current Longitude " + location.getLongitude());
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                setUpMap();
            }
        }
    }


    private void setUpMap() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                PositionDAOimpl positionDao = new PositionDAOimpl();
                markers = positionDao.getMarkers();
                Log.d("Size of markers inside position thread", "Size of markers" + markers.size());
            }
        });
        t1.start();
        try {
            t1.join();
            Log.d("Joined UI thread and position thread", "Joined UI & position thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("UI Thread and position thread merge failed", "Merging threads failed");
        }

        addMarkersToMap();
        // Causes Choreographer to skip frames, this can be fixed by seperating the addMarkerToMap methods to another class were ASyncTask is extended.

    }


    public void addMarkersToMap() {


        Log.d("Size of markers after join ", "Size of markers inside UI Thread" + markers.size());
        for (MarkerOptions m : markers) {
            mMap.addMarker(m);
        }
    }

    public void CalculateRadius() {

    }

    public ArrayList<MarkerOptions> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<MarkerOptions> markers) {
        this.markers = markers;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }
}