package com.squalala.tariki.data.api;

import com.squalala.tariki.Constants;
import com.squalala.tariki.data.prefs.Session;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
@Module
public class ApiModuleTariki {


    @Provides
    @Singleton
    Retrofit provideRestAdapter(Session session) {

     //   LoggingInterceptor interceptor = new LoggingInterceptor(session);
        HttpLoggingInterceptor interceptorHttp = new HttpLoggingInterceptor();
        interceptorHttp.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constants.TIME_OUT_CONNECTION, TimeUnit.SECONDS); // connect timeout

   //     builder.interceptors().add(interceptor);
        builder.interceptors().add(interceptorHttp);
        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(Constants.API_URL_TARIKI)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    TarikiService provideTarikiService(Retrofit adapter) {
        return adapter.create(TarikiService.class);
    }
}
