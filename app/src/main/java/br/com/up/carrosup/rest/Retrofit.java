package br.com.up.carrosup.rest;
import retrofit.RestAdapter;
/**
 * Created by ricardo on 08/08/15.
 */
public class Retrofit {
    public static CarroRest getCarroRest() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://livrowebservices.com.br/rest/")
                .build();
        CarroRest service = restAdapter.create(CarroRest.class);
        return service;
    }
}