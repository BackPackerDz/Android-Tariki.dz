package com.squalala.tariki.interactors;

import com.squalala.tariki.data.api.TarikiService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@Module
public class InteractorModule {

    @Provides
    public MapInteractor provideMapInteractor(TarikiService service){
        return new MapInteratorImpl(service);
    }

}
