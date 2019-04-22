package br.com.apptransescolar.API;

import java.util.List;

import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.Classes.Tio;
import br.com.apptransescolar.Classes.Tios;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static br.com.apptransescolar.API.URLs.URL_ASP;

public interface ITios {

    @GET("tio")
    Call<List<Tios>> getTios(@Query("id") int idPai);


    @GET("getTios.php")
    Call<List<Tio>> getAllTios(
            @Query("item_type") String item_type,
            @Query("key") String keyword
    );

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_ASP)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
