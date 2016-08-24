package com.squalala.tariki.interactors;

import com.squalala.tariki.data.api.TarikiService;
import com.squalala.tariki.models.Event;
import com.squalala.tariki.models.Rowset;
import com.squalala.tariki.ui.map.MapListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

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
public class MapInteratorImpl implements MapInteractor {

    private TarikiService service;

    private List<Event> events = new ArrayList<>();

    public MapInteratorImpl(TarikiService service) {
        this.service = service;
    }

    @Override
    public void loadEvents(final MapListener mapListener) {

        List<Observable<Rowset>> observables = new ArrayList<>();

        observables.add(service.getDivers());
        observables.add(service.getTravaux());
        observables.add(service.getTraverseeChameaux());
        observables.add(service.getRetrecissement());
        observables.add(service.getPointNoir());
        observables.add(service.getAbsenceEclairage());
        observables.add(service.getAbsencePlaqueSignalisations());
        observables.add(service.getChausseDegradee());
        observables.add(service.getManifestations());
        observables.add(service.getSableEmpietement());
        observables.add(service.getRouteVerglacante());
        observables.add(service.getChausseGlissante());
        observables.add(service.getSolErosion());
        observables.add(service.getPluie());
        observables.add(service.getChutDeNeige());

        Observable.merge(observables)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Rowset>() {

                           @Override
                           public void onCompleted() {
                               mapListener.onLoadEvents(events);
                           }

                           @Override
                           public void onError(Throwable e) {
                                e.printStackTrace();
                           }

                           @DebugLog
                           @Override
                           public void onNext(Rowset rowset) {
                               for (Event event: rowset.getEvents())
                                    if (event.getPosition() != null)
                                        events.add(event);
                           }
                       });

    }

}
