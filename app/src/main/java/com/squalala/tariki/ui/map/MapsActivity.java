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

/**
 * Created by Fayçal KADDOURI on 15/08/16.
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
           new Permission(getApplicationContext(),MapsActivity.this);
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


    

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMapComponent.builder()
                .appComponent(appComponent)
                .mapModule(new MapModule(this))
                .build()
                .inject(this);
    }


}
