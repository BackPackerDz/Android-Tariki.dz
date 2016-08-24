package com.squalala.tariki.ui.map;

import com.squalala.tariki.interactors.MapInteractor;
import com.squalala.tariki.models.Event;

import java.util.List;

import hugo.weaving.DebugLog;

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
