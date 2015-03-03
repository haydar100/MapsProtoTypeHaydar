package com.example.berkan.mapsprototype;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Berkan on 3-3-2015.
 */
public interface PositionDAO {
    public ArrayList<MarkerOptions> getMarkers();

}
