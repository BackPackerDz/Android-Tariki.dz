package com.squalala.tariki;

import android.app.Application;
import android.content.Context;

import com.squalala.tariki.data.prefs.Session;

import javax.inject.Singleton;

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
