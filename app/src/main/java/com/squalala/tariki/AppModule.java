package com.squalala.tariki;

import android.app.Application;
import android.content.Context;

import com.squalala.tariki.data.prefs.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides @Singleton
    public Session provideSession() {
        return new Session(app.getApplicationContext());
    }




}
