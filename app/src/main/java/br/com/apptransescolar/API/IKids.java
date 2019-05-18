package br.com.apptransescolar.API;

import java.util.List;

import br.com.apptransescolar.Classes.Kids;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static br.com.apptransescolar.API.URLs.URL_ASP;

public interface IKids {

    @GET("crianca")
    Call<List<Kids>> getKids(@Query("id") int idPai);

    @GET("getKids.php")
    Call<List<Kids>> getAllKids(
            @Query("item_type") String item_type,
            @Query("key") String keyword
    );

    @GET("getCriancas.php")
    Call<List<Kids>> getKids(
            @Query("item_type") String item_type,
            @Query("key") String keyword,
            @Query("idPais") String idPais
    );

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_ASP)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
