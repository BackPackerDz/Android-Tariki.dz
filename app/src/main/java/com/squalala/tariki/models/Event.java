package com.squalala.tariki.models;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * Copyright 2016 Fayçal KADDOURI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
