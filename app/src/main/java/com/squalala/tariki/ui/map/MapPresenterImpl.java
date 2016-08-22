package com.squalala.tariki.ui.map;

import com.squalala.tariki.interactors.MapInteractor;
import com.squalala.tariki.models.Event;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
public class MapPresenterImpl
        implements MapPresenter, MapListener {

    private MapsView view;
    private MapInteractor mapInteractor;

    public MapPresenterImpl(MapsView view, MapInteractor mapInteractor) {
        this.view = view;
        this.mapInteractor = mapInteractor;
    }

    @DebugLog
    @Override
    public void onLoadEvents(List<Event> events) {
        view.showMarkers(events);
        view.hideProgress();
    }

    @Override
    public void onCreate() {
        view.showProgress();
        mapInteractor.loadEvents(this);
    }




}
