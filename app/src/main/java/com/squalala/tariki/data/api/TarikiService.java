package com.squalala.tariki.data.api;


import com.squalala.tariki.models.Rowset;

import retrofit2.http.GET;
import rx.Observable;

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
public interface TarikiService {

    @GET("Divers_FR.XML")
    Observable<Rowset> getDivers();

    @GET("Travaux_FR.XML")
    Observable<Rowset> getTravaux();

    @GET("Chute_Neige_FR.XML")
    Observable<Rowset> getChutDeNeige();

    @GET("Pluie_FR.XML")
    Observable<Rowset> getPluie();

    @GET("Erosion_Sol_FR.XML")
    Observable<Rowset> getSolErosion();

    @GET("Chausse_Glissante_FR.XML")
    Observable<Rowset> getChausseGlissante();

    @GET("Route_Verglacante_FR.XML")
    Observable<Rowset> getRouteVerglacante();

    @GET("Sable_empietement_FR.XML")
    Observable<Rowset> getSableEmpietement();

    @GET("Manifestation_FR.XML")
    Observable<Rowset> getManifestations();

    @GET("Chausse_degradee_FR.XML")
    Observable<Rowset> getChausseDegradee();

    @GET("Absence_plaques_signalisations_FR.XML")
    Observable<Rowset> getAbsencePlaqueSignalisations();

    @GET("Absence_eclairage_FR.XML")
    Observable<Rowset> getAbsenceEclairage();

    @GET("Point_noire_FR.XML")
    Observable<Rowset> getPointNoir();

    @GET("Retrecissement_FR.XML")
    Observable<Rowset> getRetrecissement();

    @GET("Traversee_chameaux_FR.XML")
    Observable<Rowset> getTraverseeChameaux();




}
