package com.squalala.tariki.models;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@Root(name = "ROW")
public class Event {

    @Element(name = "TYPE_EVENT_L")
    private String typeEvent;

    @Element(name = "LIEU_DIT_L", required=false)
    private String locationName;

    @Element(name = "COORDONNE_X", required=false)
    private String longitude;

    @Element(name = "COORDONNE_Y", required=false)
    private String latitude;

    @Element(name ="LIB_CAUSE_FR")
    private String category;

    @Element(name="DESCRIPTION_L", required=false)
    private String description;

    public String getDescription() {
        return description;
    }

    public String getTypeEvent() {
        return typeEvent;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLongitude() {
        return Double.valueOf(longitude);
    }

    public double getLatitude() {
        return Double.valueOf(latitude);
    }

    public String getCategory() {
        return category;
    }


    public GeoPoint getPosition() {
        if (latitude != null && longitude != null)
            return new GeoPoint(getLatitude(), getLongitude());
        return null;
    }


}
