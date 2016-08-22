package com.squalala.tariki.ui.map;

import com.squalala.tariki.interactors.MapInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
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
