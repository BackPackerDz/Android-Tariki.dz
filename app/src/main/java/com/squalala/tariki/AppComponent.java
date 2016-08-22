package com.squalala.tariki;

import com.squalala.tariki.data.api.ApiModuleTariki;
import com.squalala.tariki.data.prefs.Session;
import com.squalala.tariki.interactors.InteractorModule;
import com.squalala.tariki.interactors.MapInteractor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                InteractorModule.class,
                ApiModuleTariki.class
        }
)
public interface AppComponent {
    void inject(App app);

    MapInteractor getMapInteractor();
    Session getSession();
}
