package com.squalala.tariki.ui.map;

import com.squalala.tariki.ActivityScope;
import com.squalala.tariki.AppComponent;

import dagger.Component;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = MapModule.class
)
public interface MapComponent {
    void inject(MapsActivity activity);
}
