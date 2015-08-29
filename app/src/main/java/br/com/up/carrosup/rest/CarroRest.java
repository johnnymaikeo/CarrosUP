package br.com.up.carrosup.rest;
import java.util.List;
import br.com.up.carrosup.domain.Carro;
import retrofit.http.GET;
import retrofit.http.Path;

public interface CarroRest {
    @GET("/carros/tipo/{tipo}")
    public List<Carro> getCarros(@Path("tipo") String tipo);
}