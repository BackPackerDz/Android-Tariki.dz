package com.squalala.tariki.ui.map;

import com.squalala.tariki.interactors.MapInteractor;

import dagger.Module;
import dagger.Provides;

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
@Module
public class MapModule {

    private MapsView view;

    public MapModule(MapsView view) {
        this.view = view;
    }

    @Provides public MapsView provideView()  { return view; }

    @Provides
    MapPresenter provideMapPresenter(MapsView view, MapInteractor interactor) {
        return new MapPresenterImpl(view, interactor);
    }
}
