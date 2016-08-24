package com.squalala.tariki.ui.map;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import android.Manifest;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squalala.tariki.AppComponent;
import com.squalala.tariki.BaseActivity;
import com.squalala.tariki.Constants;
import com.squalala.tariki.R;
import com.squalala.tariki.custom.RadiusMarkerClusterer;
import com.squalala.tariki.models.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

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
public class MapsActivity extends BaseActivity
    implements MapsView {

    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @BindView(R.id.map)
    MapView map;

    private IMapController mapController;

    private RadiusMarkerClusterer poiMarkers;

    private Subscription updatableLocationSubscription;
    private Observable<Location> locationUpdatesObservable;

    private ReactiveLocationProvider locationProvider;

    private Marker myMarker;

    private Snackbar snackbar;

    @Inject
    MapPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
        final ITileSource tileSource = new XYTileSource("Mapnik", 3, 19, 256, ".png",
                new String[] {Constants.TILE_SOURCE});
        //mapView.setTileSource((new XYTileSource("localMapnik", Resource, 0, 18, 256, ".png",
        //  "http://tile.openstreetmap.org/")));
        tileProvider.setTileSource(tileSource);
        map.setTileProvider(tileProvider);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(15);

        poiMarkers = new RadiusMarkerClusterer(this);
        map.getOverlays().add(poiMarkers);

        Drawable poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_male, null);
        myMarker = new Marker(map);
        poiMarkers.add(myMarker);

        snackbar = Snackbar
                .make(findViewById(android.R.id.content), getString(R.string.chargement_en_cours), Snackbar.LENGTH_INDEFINITE);

        presenter.onCreate();



        setupLocationManager();
    }

    @Override
    public void showProgress() {
        snackbar.show();
    }

    @Override
    public void hideProgress() {
        snackbar.dismiss();
    }

    private void setupLocationManager() {

        locationProvider = new ReactiveLocationProvider(getApplicationContext());

        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);

        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return locationProvider.getUpdatedLocation(locationRequest);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        updatableLocationSubscription = locationUpdatesObservable

                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {e.printStackTrace(); }

                    @DebugLog
                    @Override
                    public void onNext(Location location) {
                        if (location != null)
                        {
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            Drawable poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_male, null);
                            myMarker.setPosition(geoPoint);
                            myMarker.setIcon(poiIcon);
                            myMarker.setTitle("Moi");
                            mapController.setZoom(18);
                            mapController.setCenter(geoPoint);
                        }
                    }
                });

    }

    @DebugLog
    @Override
    public void showMarkers(List<Event> events) {




        for (Event event: events)
        {
            Marker poiMarker = new Marker(map);
            poiMarker.setPosition(event.getPosition());
            poiMarker.setTitle(event.getCategory());
            poiMarker.setSnippet(event.getDescription());
            poiMarkers.add(poiMarker);

            Drawable poiIcon;


            switch (event.getCategory()) {

                case "Travaux":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.travaux, null);
                    break;

                case "Pluie":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.pluie, null);
                    break;

                case "Accident":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.accident, null);
                    break;

                case "Chaussée Glissante":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.glissant, null);
                    break;

                case "Chute de Neige":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.neige, null);
                    break;

                case "Faible visibilité":
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.eclairage, null);
                    break;

                default:
                    poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_kml_point, null);
            }


            poiMarker.setIcon(poiIcon);
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "osmdroid permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nLocation to show user location.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nStorage access to store map tiles.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } // else: We already have permissions, so handle as normal
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    Toast.makeText(MapsActivity.this, "All permissions granted", Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, "Storage permission is required to store map tiles to reduce data usage and for offline usage.", Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, "Location permission is required to show the user's location on map.", Toast.LENGTH_LONG).show();
                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(MapsActivity.this, "Storage permission is required to store map tiles to reduce data usage and for offline usage." +
                            "\nLocation permission is required to show the user's location on map.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMapComponent.builder()
                .appComponent(appComponent)
                .mapModule(new MapModule(this))
                .build()
                .inject(this);
    }


}
