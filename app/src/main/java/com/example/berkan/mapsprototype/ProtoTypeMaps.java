package com.example.berkan.mapsprototype;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ProtoTypeMaps extends FragmentActivity {

    public ArrayList<MarkerOptions> markers;
    public Handler handler;
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
     * have been completely destroyed during this process (it is likely that it would only be
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
        addMarkersToMap(); // Causes Choreographer to skip frames, this can be fixed by seperating the addMarkerToMap methods to another class were ASyncTask is extended.

    }

    private void addMarkersToMap() {
        Log.d("Size of markers after join ", "Size of markers inside UI Thread" + markers.size());
        for (MarkerOptions m : markers) {
            mMap.addMarker(m);
        }
    }


}