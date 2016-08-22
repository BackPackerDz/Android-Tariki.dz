package com.squalala.tariki.data.api;


import com.squalala.tariki.models.Rowset;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
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
